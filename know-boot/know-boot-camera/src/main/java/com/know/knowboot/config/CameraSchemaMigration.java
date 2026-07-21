package com.know.knowboot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

/**
 * 摄像头设备表结构迁移
 * <p>
 * 数据库中 camera_device 表使用的是旧版 schema (20260320)，
 * 列名为 name/ip/model 等，但 Java 实体类对应新版 schema (20260427)，
 * 列名为 device_name/ip_address/device_model 等。
 * <p>
 * 此组件检测并补齐缺失列，不删除旧列以保证数据安全。
 */
@Component
public class CameraSchemaMigration {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void migrateSchema() {
        System.out.println("[SchemaMigrate] 检查 camera_device 表结构...");
        try {
            // 检查 device_name 列是否存在（核心判断标志）
            List<Map<String, Object>> columns = jdbcTemplate.queryForList(
                "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS " +
                "WHERE TABLE_SCHEMA = (SELECT DATABASE()) AND TABLE_NAME = 'camera_device'"
            );
            boolean hasDeviceName = columns.stream()
                .anyMatch(c -> "device_name".equalsIgnoreCase((String) c.get("COLUMN_NAME")));

            if (hasDeviceName) {
                System.out.println("[SchemaMigrate] 表结构已是最新，无需迁移");
            } else {
                System.out.println("[SchemaMigrate] 检测到旧版表结构，开始迁移...");

                // 1. 添加/修改列（逐个执行，已存在的列会抛异常被跳过）
                String[][] alterSqls = {
                    // 重命名 name → device_name
                    {"ALTER TABLE camera_device CHANGE COLUMN name device_name VARCHAR(100) NOT NULL COMMENT '设备名称'"},
                    // 添加 device_code
                    {"ALTER TABLE camera_device ADD COLUMN device_code VARCHAR(100) DEFAULT NULL COMMENT '设备编号'"},
                    // 重命名 model → device_model
                    {"ALTER TABLE camera_device CHANGE COLUMN model device_model VARCHAR(50) DEFAULT NULL COMMENT '设备型号'"},
                    // 添加 ip_address，从 ip 复制数据
                    {"ALTER TABLE camera_device ADD COLUMN ip_address VARCHAR(50) DEFAULT NULL COMMENT '局域网IP'"},
                    // 添加 mac_address
                    {"ALTER TABLE camera_device ADD COLUMN mac_address VARCHAR(50) DEFAULT NULL COMMENT 'MAC地址'"},
                    // 添加 snapshot_url
                    {"ALTER TABLE camera_device ADD COLUMN snapshot_url VARCHAR(200) DEFAULT NULL COMMENT '快照地址'"},
                    // 添加 position
                    {"ALTER TABLE camera_device ADD COLUMN position VARCHAR(100) DEFAULT NULL COMMENT '安装位置'"},
                    // 添加 user_id
                    {"ALTER TABLE camera_device ADD COLUMN user_id BIGINT DEFAULT 1 COMMENT '所属用户'"},
                    // 添加 create_by
                    {"ALTER TABLE camera_device ADD COLUMN create_by BIGINT DEFAULT NULL COMMENT '创建者'"},
                    // 添加 update_by
                    {"ALTER TABLE camera_device ADD COLUMN update_by BIGINT DEFAULT NULL COMMENT '更新者'"}
                };

                for (String[] sql : alterSqls) {
                    try {
                        jdbcTemplate.execute(sql[0]);
                        System.out.println("[SchemaMigrate] OK: " + sql[0].substring(0, Math.min(80, sql[0].length())));
                    } catch (Exception e) {
                        System.out.println("[SchemaMigrate] SKIP: " + e.getMessage());
                    }
                }

                // 2. 迁移已有数据
                // 将 ip 复制到 ip_address
                try {
                    jdbcTemplate.execute("UPDATE camera_device SET ip_address = ip WHERE ip IS NOT NULL AND ip_address IS NULL");
                } catch (Exception e) { /* ignored */ }
                // 设置 user_id 默认值
                try {
                    jdbcTemplate.execute("UPDATE camera_device SET user_id = 1 WHERE user_id IS NULL");
                } catch (Exception e) { /* ignored */ }

                System.out.println("[SchemaMigrate] 表结构迁移完成");
            }

        } catch (Exception e) {
            System.err.println("[SchemaMigrate] 迁移失败: " + e.getMessage());
        }

        // 检查并创建 V2 中新增但 V1 中不存在的表
        createMissingTables();

        // 检查 camera_record 表结构，补齐缺失列
        migrateCameraRecord();
    }

    /**
     * 迁移 camera_record 表，补齐缺失列
     */
    private void migrateCameraRecord() {
        System.out.println("[SchemaMigrate] 检查 camera_record 表结构...");
        try {
            List<Map<String, Object>> columns = jdbcTemplate.queryForList(
                "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS " +
                "WHERE TABLE_SCHEMA = (SELECT DATABASE()) AND TABLE_NAME = 'camera_record'"
            );
            // 所有实体中存在的列定义：列名 -> SQL定义
            String[][] expectedColumns = {
                {"cloud_url", "VARCHAR(500) DEFAULT NULL COMMENT '云存储地址'"},
                {"create_by", "BIGINT DEFAULT NULL COMMENT '创建者'"},
                {"create_time", "BIGINT DEFAULT NULL COMMENT '创建时间'"},
                {"device_id", "BIGINT NOT NULL COMMENT '设备ID'"},
                {"duration", "INT DEFAULT NULL COMMENT '时长(秒)'"},
                {"end_time", "BIGINT DEFAULT NULL COMMENT '录像结束时间'"},
                {"file_path", "VARCHAR(500) DEFAULT NULL COMMENT '文件路径'"},
                {"file_size", "BIGINT DEFAULT NULL COMMENT '文件大小(字节)'"},
                {"record_type", "INT DEFAULT NULL COMMENT '录像类型'"},
                {"start_time", "BIGINT DEFAULT NULL COMMENT '录像开始时间'"},
                {"status", "INT DEFAULT 1 COMMENT '状态'"}
            };

            int added = 0;
            for (String[] col : expectedColumns) {
                String colName = col[0];
                String colDef = col[1];
                boolean exists = columns.stream()
                    .anyMatch(c -> colName.equalsIgnoreCase((String) c.get("COLUMN_NAME")));
                if (!exists) {
                    System.out.println("[SchemaMigrate] camera_record 缺少 " + colName + " 列，正在添加...");
                    try {
                        jdbcTemplate.execute(
                            "ALTER TABLE camera_record ADD COLUMN `" + colName + "` " + colDef
                        );
                        System.out.println("[SchemaMigrate] camera_record." + colName + " 列添加成功");
                        added++;
                    } catch (Exception ex) {
                        System.err.println("[SchemaMigrate] camera_record." + colName + " 添加失败: " + ex.getMessage());
                    }
                }
            }

            if (added == 0) {
                System.out.println("[SchemaMigrate] camera_record 表结构已是最新");
            }
        } catch (Exception e) {
            System.err.println("[SchemaMigrate] 检查/迁移 camera_record 失败: " + e.getMessage());
        }
    }

    /**
     * 创建 V2 schema 新增的表（camera_favorite, camera_snapshot）
     */
    private void createMissingTables() {
        System.out.println("[SchemaMigrate] 检查缺失表...");
        try {
            // 创建 camera_favorite 表
            if (!tableExists("camera_favorite")) {
                jdbcTemplate.execute(
                    "CREATE TABLE `camera_favorite` (" +
                    "  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键'," +
                    "  `device_id` bigint NOT NULL COMMENT '设备ID'," +
                    "  `user_id` bigint NOT NULL COMMENT '用户ID'," +
                    "  `sort` int DEFAULT 0 COMMENT '排序'," +
                    "  `create_time` bigint DEFAULT NULL COMMENT '创建时间'," +
                    "  PRIMARY KEY (`id`)," +
                    "  UNIQUE KEY `uk_device_user` (`device_id`, `user_id`)" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='设备收藏表'"
                );
                System.out.println("[SchemaMigrate] 已创建 camera_favorite 表");
            } else {
                System.out.println("[SchemaMigrate] camera_favorite 表已存在");
            }

            // 创建 camera_snapshot 表（V1 中名为 camera_screenshot，V2 改名）
            if (!tableExists("camera_snapshot")) {
                jdbcTemplate.execute(
                    "CREATE TABLE `camera_snapshot` (" +
                    "  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键'," +
                    "  `device_id` bigint NOT NULL COMMENT '设备ID'," +
                    "  `capture_time` bigint NOT NULL COMMENT '截图时间'," +
                    "  `file_path` varchar(500) DEFAULT NULL COMMENT '本地路径'," +
                    "  `cloud_url` varchar(500) DEFAULT NULL COMMENT '云存储地址'," +
                    "  `thumbnail` varchar(500) DEFAULT NULL COMMENT '缩略图路径'," +
                    "  `create_by` bigint DEFAULT NULL COMMENT '创建者'," +
                    "  `create_time` bigint DEFAULT NULL COMMENT '创建时间'," +
                    "  PRIMARY KEY (`id`)," +
                    "  KEY `idx_device_id` (`device_id`)," +
                    "  KEY `idx_capture_time` (`capture_time`)" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='截图记录表'"
                );
                System.out.println("[SchemaMigrate] 已创建 camera_snapshot 表");
            } else {
                System.out.println("[SchemaMigrate] camera_snapshot 表已存在");
            }
        } catch (Exception e) {
            System.err.println("[SchemaMigrate] 创建缺失表失败: " + e.getMessage());
        }
    }

    private boolean tableExists(String tableName) {
        try {
            Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES " +
                "WHERE TABLE_SCHEMA = (SELECT DATABASE()) AND TABLE_NAME = ?",
                Integer.class, tableName
            );
            return count != null && count > 0;
        } catch (Exception e) {
            return false;
        }
    }
}
