package com.know.knowboot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

/**
 * 知识库(knowledge)模块表结构与数据集初始化
 * <p>
 * 使用 @PostConstruct + JdbcTemplate 模式（与 SystemSchemaMigration / PlanSchemaMigration 一致），
 * 不依赖 Flyway。负责：
 * 1) 创建 {@code kb_uploaded_file} 表（HTML / MD 文件上传）
 * 2) 初始化 Vue 管理端「文件管理」菜单及按钮权限（挂到现有「知识管理」根菜单下）
 * 3) 授权给超级管理员角色（role_id = 1）
 * 4) 初始化 UniApp 首页快捷入口 sys_menu_config(file-upload)
 * <p>
 * 全部幂等：仅当表 / 菜单 / 授权 / 快捷入口不存在时才创建。
 * 对应设计文档：Ai-Design-V2/AI知识库菜单设计/kb_file_upload.sql
 */
@Component
public class KnowledgeSchemaMigration {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /** Vue 管理端「文件管理」菜单路径 */
    private static final String FILE_MENU_PATH = "/knowledge/file";
    /** 父级「知识管理」根菜单路径 */
    private static final String KNOWLEDGE_ROOT_PATH = "/knowledge";
    /** 超级管理员角色 ID */
    private static final Long ADMIN_ROLE_ID = 1L;

    @PostConstruct
    public void migrateSchema() {
        System.out.println("[KnowledgeSchemaMigrate] 检查知识库(knowledge)模块表结构...");
        try {
            createKbUploadedFile();
            seedFileUploadMenu();
            seedFileUploadMenuConfig();
            System.out.println("[KnowledgeSchemaMigrate] 知识库(knowledge)模块表结构检查完成");
        } catch (Exception e) {
            System.out.println("[KnowledgeSchemaMigrate] " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ============================
    // 1. 建表: kb_uploaded_file
    // ============================
    private void createKbUploadedFile() {
        if (!tableExists("kb_uploaded_file")) {
            jdbcTemplate.execute(
                "CREATE TABLE `kb_uploaded_file` (" +
                "  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键'," +
                "  `original_name` varchar(255) NOT NULL COMMENT '原始文件名(用户上传时的名字)'," +
                "  `file_name` varchar(255) NOT NULL COMMENT '存储文件名(UUID+ext,防重名)'," +
                "  `file_path` varchar(500) NOT NULL COMMENT '文件存储相对路径'," +
                "  `file_ext` varchar(20) NOT NULL COMMENT '扩展名: html/htm/md/markdown'," +
                "  `file_content` longtext COMMENT '文件原始文本内容(UTF-8)'," +
                "  `file_size` bigint DEFAULT 0 COMMENT '文件大小(字节)'," +
                "  `mime_type` varchar(100) DEFAULT NULL COMMENT 'MIME 类型'," +
                "  `knowledge_base_id` bigint DEFAULT NULL COMMENT '归属知识库ID(可空)'," +
                "  `document_id` bigint DEFAULT NULL COMMENT '解析成功后对应的 kb_document.id'," +
                "  `parse_status` tinyint DEFAULT 0 COMMENT '解析状态: 0待解析 1成功 2失败'," +
                "  `parse_error` varchar(1000) DEFAULT NULL COMMENT '解析失败原因'," +
                "  `source_type` varchar(20) DEFAULT 'upload' COMMENT '来源类型: upload/import/system'," +
                "  `upload_platform` varchar(20) DEFAULT NULL COMMENT '上传端: vue/uniapp-h5/uniapp-app/uniapp-mp'," +
                "  `sort` int DEFAULT 0 COMMENT '排序'," +
                "  `status` tinyint DEFAULT 1 COMMENT '状态: 0禁用 1正常'," +
                "  `create_by` bigint DEFAULT NULL COMMENT '上传人ID'," +
                "  `create_time` bigint DEFAULT NULL COMMENT '上传时间(毫秒时间戳)'," +
                "  `update_time` bigint DEFAULT NULL COMMENT '更新时间(毫秒时间戳)'," +
                "  `delete_time` bigint DEFAULT 0 COMMENT '删除时间(毫秒,0=未删除)'," +
                "  PRIMARY KEY (`id`)," +
                "  KEY `idx_kb_id` (`knowledge_base_id`)," +
                "  KEY `idx_doc_id` (`document_id`)," +
                "  KEY `idx_create_by` (`create_by`)," +
                "  KEY `idx_parse` (`parse_status`)," +
                "  KEY `idx_create_time` (`create_time`)," +
                "  KEY `idx_source_type` (`source_type`)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='知识库上传文件表(HTML/MD)'"
            );
            System.out.println("[KnowledgeSchemaMigrate] 已创建 kb_uploaded_file 表");
        }
    }

    // ============================
    // 2. 菜单接入（知识管理 → 文件管理）
    // ============================
    private void seedFileUploadMenu() {
        if (!tableExists("sys_menu")) {
            return;
        }
        Long exists = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM sys_menu WHERE path = ? AND del_flag = 0", Long.class, FILE_MENU_PATH);
        if (exists != null && exists > 0) {
            // 菜单已存在，仅补齐角色授权（保证幂等）
            grantRoleToFileMenus();
            return;
        }

        // 查找「知识管理」根菜单（按 path 定位，避免硬编码 ID 冲突）
        Long parentId = null;
        List<Map<String, Object>> parents = jdbcTemplate.queryForList(
            "SELECT id FROM sys_menu WHERE parent_id = 0 AND path = ? ORDER BY id LIMIT 1", KNOWLEDGE_ROOT_PATH);
        if (!parents.isEmpty()) {
            parentId = ((Number) parents.get(0).get("id")).longValue();
        }
        if (parentId == null) {
            System.out.println("[KnowledgeSchemaMigrate] 未找到知识管理父级菜单，跳过文件管理菜单初始化");
            return;
        }

        // 接在现有子菜单之后
        Integer maxSort = jdbcTemplate.queryForObject(
            "SELECT COALESCE(MAX(sort), 0) FROM sys_menu WHERE parent_id = ?", Integer.class, parentId);
        int sort = (maxSort == null ? 0 : maxSort) + 1;

        jdbcTemplate.update(
            "INSERT INTO sys_menu (parent_id, menu_name, path, component, perms, icon, menu_type, sort, status, " +
            "tenant_id, create_by, create_time, update_time, del_flag) " +
            "VALUES (?, ?, ?, ?, ?, ?, 2, ?, 1, NULL, ?, NOW(), NOW(), 0)",
            parentId, "文件管理", FILE_MENU_PATH, "knowledge/file/index", "knowledge:file:view", "upload", sort, "system");

        Long menuId = jdbcTemplate.queryForObject(
            "SELECT id FROM sys_menu WHERE path = ? AND del_flag = 0 ORDER BY id DESC LIMIT 1", Long.class, FILE_MENU_PATH);
        System.out.println("[KnowledgeSchemaMigrate] 已初始化文件管理菜单, parentId=" + parentId + ", id=" + menuId + ", sort=" + sort);

        // 按钮权限（menu_type = 3）
        seedButtonPerm(menuId, "文件上传", "knowledge:file:upload", 1);
        seedButtonPerm(menuId, "文件删除", "knowledge:file:delete", 2);
        seedButtonPerm(menuId, "文件解析", "knowledge:file:parse", 3);

        grantRoleToFileMenus();
    }

    private void seedButtonPerm(Long parentMenuId, String name, String perms, int sort) {
        if (parentMenuId == null) {
            return;
        }
        Long exists = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM sys_menu WHERE perms = ? AND del_flag = 0", Long.class, perms);
        if (exists != null && exists > 0) {
            return;
        }
        jdbcTemplate.update(
            "INSERT INTO sys_menu (parent_id, menu_name, path, component, perms, icon, menu_type, sort, status, " +
            "tenant_id, create_by, create_time, update_time, del_flag) " +
            "VALUES (?, ?, NULL, NULL, ?, NULL, 3, ?, 1, NULL, ?, NOW(), NOW(), 0)",
            parentMenuId, name, perms, sort, "system");
    }

    private void grantRoleToFileMenus() {
        if (!tableExists("sys_role_menu")) {
            return;
        }
        jdbcTemplate.update(
            "INSERT INTO sys_role_menu (role_id, menu_id, create_by, create_time, del_flag) " +
            "SELECT ?, m.id, 'system', NOW(), 0 FROM sys_menu m " +
            "WHERE (m.path = ? OR m.perms LIKE 'knowledge:file:%') AND m.del_flag = 0 " +
            "  AND NOT EXISTS (SELECT 1 FROM sys_role_menu rm WHERE rm.role_id = ? AND rm.menu_id = m.id)",
            ADMIN_ROLE_ID, FILE_MENU_PATH, ADMIN_ROLE_ID);
    }

    // ============================
    // 3. UniApp 首页快捷入口（sys_menu_config）
    // ============================
    private void seedFileUploadMenuConfig() {
        if (!tableExists("sys_menu_config")) {
            return;
        }
        Long exists = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM sys_menu_config WHERE menu_code = ?", Long.class, "file-upload");
        if (exists != null && exists > 0) {
            return;
        }
        String renderConfig = "{\"iconClass\":\"premium-icon-g9\",\"title\":\"文件导入\",\"desc\":\"上传 HTML/MD 快速入库并预览\","
                + "\"tag\":\"导入\",\"thumbBg\":\"linear-gradient(135deg,#ecfdf5,#d1fae5)\",\"sections\":[\"quick\"]}";
        jdbcTemplate.update(
            "INSERT INTO sys_menu_config " +
            "(menu_name, menu_code, menu_type, icon, path, is_show, sort, render_type, render_config, create_time) " +
            "VALUES (?, ?, 2, ?, ?, 1, ?, 1, ?, NOW())",
            "文件导入", "file-upload", "\uD83D\uDCE4", "/pages/knowledge/file-upload/index", 13, renderConfig);
        System.out.println("[KnowledgeSchemaMigrate] 已初始化 UniApp 快捷入口 file-upload");
    }

    // ============================
    // 工具方法
    // ============================
    private boolean tableExists(String tableName) {
        try {
            Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES " +
                "WHERE TABLE_SCHEMA = (SELECT DATABASE()) AND TABLE_NAME = ?",
                Integer.class, tableName);
            return count != null && count > 0;
        } catch (Exception e) {
            return false;
        }
    }
}
