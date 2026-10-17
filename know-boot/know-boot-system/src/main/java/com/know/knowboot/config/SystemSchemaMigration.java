package com.know.knowboot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 系统(system)模块表结构迁移
 * <p>
 * 使用 @PostConstruct + JdbcTemplate 模式（与 PlanSchemaMigration/BizSchemaMigration 一致），
 * 不依赖 Flyway。负责创建「级联字典」相关表，并初始化级联字典类型、顶层种子数据以及菜单行。
 * <p>
 * 说明：级联字典独立于平铺字典 sys_dict，两者并行、互不影响。
 */
@Component
public class SystemSchemaMigration {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void migrateSchema() {
        System.out.println("[SystemSchemaMigrate] 检查系统(system)模块表结构...");
        try {
            createCascadeDictType();
            createCascadeDictData();
            seedPlanTypeCascade();
            seedIndustryTypeCascade();
            seedCascadeDictMenu();
            System.out.println("[SystemSchemaMigrate] 系统(system)模块表结构检查完成");
        } catch (Exception e) {
            System.err.println("[SystemSchemaMigrate] 迁移失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ============================
    // 1. 级联字典类型表
    // ============================
    private void createCascadeDictType() {
        if (!tableExists("sys_cascade_dict_type")) {
            jdbcTemplate.execute(
                "CREATE TABLE `sys_cascade_dict_type` (" +
                "  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键'," +
                "  `type_name` varchar(100) NOT NULL COMMENT '类型名称'," +
                "  `type_code` varchar(100) NOT NULL COMMENT '类型编码'," +
                "  `status` tinyint DEFAULT 1 COMMENT '状态(0:停用 1:启用)'," +
                "  `remark` varchar(255) DEFAULT NULL COMMENT '备注'," +
                "  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人'," +
                "  `create_time` bigint DEFAULT NULL COMMENT '创建时间'," +
                "  `update_by` varchar(64) DEFAULT NULL COMMENT '更新人'," +
                "  `update_time` bigint DEFAULT NULL COMMENT '更新时间'," +
                "  `del_flag` tinyint DEFAULT 0 COMMENT '删除标志(0:正常 1:删除)'," +
                "  PRIMARY KEY (`id`)," +
                "  UNIQUE KEY `uk_cascade_type_code` (`type_code`)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='级联字典类型表'"
            );
            System.out.println("[SystemSchemaMigrate] 已创建 sys_cascade_dict_type 表");
        }
    }

    // ============================
    // 2. 级联字典数据表
    // ============================
    private void createCascadeDictData() {
        if (!tableExists("sys_cascade_dict_data")) {
            jdbcTemplate.execute(
                "CREATE TABLE `sys_cascade_dict_data` (" +
                "  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键'," +
                "  `dict_type_id` bigint NOT NULL COMMENT '所属级联字典类型ID'," +
                "  `parent_id` bigint DEFAULT 0 COMMENT '父级ID(0:顶级)'," +
                "  `name` varchar(100) NOT NULL COMMENT '显示名'," +
                "  `value` varchar(100) NOT NULL COMMENT '编码'," +
                "  `icon` varchar(100) DEFAULT NULL COMMENT '图标'," +
                "  `color` varchar(20) DEFAULT NULL COMMENT '颜色'," +
                "  `sort` int DEFAULT 0 COMMENT '排序'," +
                "  `status` tinyint DEFAULT 1 COMMENT '状态(0:停用 1:启用)'," +
                "  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人'," +
                "  `create_time` bigint DEFAULT NULL COMMENT '创建时间'," +
                "  `update_by` varchar(64) DEFAULT NULL COMMENT '更新人'," +
                "  `update_time` bigint DEFAULT NULL COMMENT '更新时间'," +
                "  `del_flag` tinyint DEFAULT 0 COMMENT '删除标志(0:未删除 1:已删除)'," +
                "  PRIMARY KEY (`id`)," +
                "  UNIQUE KEY `uk_type_value` (`dict_type_id`, `value`)," +
                "  KEY `idx_type_parent` (`dict_type_id`, `parent_id`)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='级联字典数据表'"
            );
            System.out.println("[SystemSchemaMigrate] 已创建 sys_cascade_dict_data 表");
        }
    }

    // ============================
    // 2. 级联字典种子数据（计划类型）
    // ============================
    private void seedPlanTypeCascade() {
        try {
            if (!tableExists("sys_cascade_dict_type") || !tableExists("sys_cascade_dict_data")) {
                return;
            }
            Long typeId = null;
            java.util.List<java.util.Map<String, Object>> rows = jdbcTemplate.queryForList(
                "SELECT id FROM sys_cascade_dict_type WHERE type_code = ? LIMIT 1", "plan_type");
            if (!rows.isEmpty()) {
                typeId = ((Number) rows.get(0).get("id")).longValue();
            }
            if (typeId == null) {
                long now = System.currentTimeMillis();
                jdbcTemplate.update(
                    "INSERT INTO sys_cascade_dict_type (type_name, type_code, status, remark, create_by, create_time, update_time, del_flag) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, 0)",
                    "计划类型", "plan_type", 1, "计划类型级联字典（原 plan_type 迁移为顶层节点）", "system", now, now);
                typeId = jdbcTemplate.queryForObject(
                    "SELECT id FROM sys_cascade_dict_type WHERE type_code = ? LIMIT 1", Long.class, "plan_type");
                System.out.println("[SystemSchemaMigrate] 已初始化级联字典类型 plan_type, id=" + typeId);
            }

            Long dataCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM sys_cascade_dict_data WHERE dict_type_id = ?", Long.class, typeId);
            if (dataCount != null && dataCount == 0) {
                long now = System.currentTimeMillis();
                insertCascadeNode(typeId, 0L, "生活习惯", "life", "🏃", "#67C23A", 1, now);
                insertCascadeNode(typeId, 0L, "认知提升", "cognition", "📚", "#409EFF", 2, now);
                insertCascadeNode(typeId, 0L, "工作技能", "skill", "💼", "#E6A23C", 3, now);
                insertCascadeNode(typeId, 0L, "项目", "project", "📦", "#909399", 4, now);
                insertCascadeNode(typeId, 0L, "兴趣爱好", "hobby", "🎨", "#F56C6C", 5, now);
                insertCascadeNode(typeId, 0L, "学习", "study", "🎵", "#8E44AD", 6, now);
                System.out.println("[SystemSchemaMigrate] 已初始化计划类型级联种子数据");
            }
        } catch (Exception e) {
            System.out.println("[SystemSchemaMigrate] 初始化级联字典数据: " + e.getMessage());
        }
    }

    private void insertCascadeNode(Long typeId, Long parentId, String name, String value,
                                   String icon, String color, int sort, long now) {
        jdbcTemplate.update(
            "INSERT INTO sys_cascade_dict_data " +
            "(dict_type_id, parent_id, name, value, icon, color, sort, status, create_by, create_time, update_time, del_flag) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, 1, ?, ?, ?, 0)",
            typeId, parentId, name, value, icon, color, sort, "system", now, now);
    }

    // ============================
    // 2-1. 级联字典种子数据（行业类型）
    // ============================
    private void seedIndustryTypeCascade() {
        try {
            if (!tableExists("sys_cascade_dict_type") || !tableExists("sys_cascade_dict_data")) {
                return;
            }
            Long typeId = null;
            java.util.List<java.util.Map<String, Object>> rows = jdbcTemplate.queryForList(
                "SELECT id FROM sys_cascade_dict_type WHERE type_code = ? LIMIT 1", "industry_type");
            if (!rows.isEmpty()) {
                typeId = ((Number) rows.get(0).get("id")).longValue();
            }
            if (typeId == null) {
                long now = System.currentTimeMillis();
                jdbcTemplate.update(
                    "INSERT INTO sys_cascade_dict_type (type_name, type_code, status, remark, create_by, create_time, update_time, del_flag) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, 0)",
                    "行业类型", "industry_type", 1, "行业类型级联字典（依据《国民经济行业分类》GB/T 4754 门类）", "system", now, now);
                typeId = jdbcTemplate.queryForObject(
                    "SELECT id FROM sys_cascade_dict_type WHERE type_code = ? LIMIT 1", Long.class, "industry_type");
                System.out.println("[SystemSchemaMigrate] 已初始化级联字典类型 industry_type, id=" + typeId);
            }

            Long dataCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM sys_cascade_dict_data WHERE dict_type_id = ?", Long.class, typeId);
            if (dataCount != null && dataCount == 0) {
                long now = System.currentTimeMillis();
                // 《国民经济行业分类》GB/T 4754-2017 门类（顶层，共 20 项）
                insertCascadeNode(typeId, 0L, "农、林、牧、渔业", "agriculture", "🌾", "#67C23A", 1, now);
                insertCascadeNode(typeId, 0L, "采矿业", "mining", "⛏️", "#909399", 2, now);
                insertCascadeNode(typeId, 0L, "制造业", "manufacturing", "🏭", "#409EFF", 3, now);
                insertCascadeNode(typeId, 0L, "电力、热力、燃气及水生产和供应业", "utilities", "💡", "#E6A23C", 4, now);
                insertCascadeNode(typeId, 0L, "建筑业", "construction", "🏗️", "#909399", 5, now);
                insertCascadeNode(typeId, 0L, "批发和零售业", "wholesale_retail", "🛒", "#409EFF", 6, now);
                insertCascadeNode(typeId, 0L, "交通运输、仓储和邮政业", "transport", "🚚", "#409EFF", 7, now);
                insertCascadeNode(typeId, 0L, "住宿和餐饮业", "accommodation_catering", "🏨", "#E6A23C", 8, now);
                insertCascadeNode(typeId, 0L, "信息传输、软件和信息技术服务业", "it", "💻", "#409EFF", 9, now);
                insertCascadeNode(typeId, 0L, "金融业", "finance", "💰", "#E6A23C", 10, now);
                insertCascadeNode(typeId, 0L, "房地产业", "real_estate", "🏠", "#909399", 11, now);
                insertCascadeNode(typeId, 0L, "租赁和商务服务业", "leasing_business", "🏢", "#909399", 12, now);
                insertCascadeNode(typeId, 0L, "科学研究和技术服务业", "scientific_tech", "🔬", "#409EFF", 13, now);
                insertCascadeNode(typeId, 0L, "水利、环境和公共设施管理业", "environment_public", "🌿", "#67C23A", 14, now);
                insertCascadeNode(typeId, 0L, "居民服务、修理和其他服务业", "resident_service", "🛠️", "#67C23A", 15, now);
                insertCascadeNode(typeId, 0L, "教育", "education", "🎓", "#409EFF", 16, now);
                insertCascadeNode(typeId, 0L, "卫生和社会工作", "health_social", "🏥", "#F56C6C", 17, now);
                insertCascadeNode(typeId, 0L, "文化、体育和娱乐业", "culture_sports_entertainment", "🎬", "#F56C6C", 18, now);
                insertCascadeNode(typeId, 0L, "公共管理、社会保障和社会组织", "public_management", "🏛️", "#909399", 19, now);
                insertCascadeNode(typeId, 0L, "国际组织", "international_org", "🌐", "#8E44AD", 20, now);
                System.out.println("[SystemSchemaMigrate] 已初始化行业类型级联种子数据");
            }
        } catch (Exception e) {
            System.out.println("[SystemSchemaMigrate] 初始化行业类型级联数据: " + e.getMessage());
        }
    }

    // ============================
    // 3. 菜单接入（系统设置 → 级联字典）
    // ============================
    private void seedCascadeDictMenu() {
        try {
            if (!tableExists("sys_menu")) {
                return;
            }
            String path = "/system/cascade-dict";
            Long exists = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM sys_menu WHERE path = ?", Long.class, path);
            if (exists != null && exists > 0) {
                return;
            }
            Long parentId = null;
            java.util.List<java.util.Map<String, Object>> parents = jdbcTemplate.queryForList(
                "SELECT id FROM sys_menu WHERE parent_id = 0 AND path = '/system' ORDER BY id LIMIT 1");
            if (!parents.isEmpty()) {
                parentId = ((Number) parents.get(0).get("id")).longValue();
            }
            if (parentId == null) {
                System.out.println("[SystemSchemaMigrate] 未找到系统设置父级菜单，跳过级联字典菜单初始化");
                return;
            }
            // 接在「字典管理」(sort=6) 之后
            Integer maxSort = jdbcTemplate.queryForObject(
                "SELECT COALESCE(MAX(sort), 0) FROM sys_menu WHERE parent_id = ?", Integer.class, parentId);
            int sort = (maxSort == null ? 0 : maxSort) + 1;
            jdbcTemplate.update(
                "INSERT INTO sys_menu (parent_id, menu_name, path, component, perms, icon, menu_type, sort, status, " +
                "tenant_id, create_by, create_time, update_time, del_flag) " +
                "VALUES (?, ?, ?, ?, ?, NULL, 2, ?, 1, NULL, ?, NOW(), NOW(), 0)",
                parentId, "级联字典", path, "CascadeDict", "", sort, "system");
            System.out.println("[SystemSchemaMigrate] 已初始化级联字典菜单, parentId=" + parentId + ", sort=" + sort);
        } catch (Exception e) {
            System.out.println("[SystemSchemaMigrate] 初始化级联字典菜单: " + e.getMessage());
        }
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
