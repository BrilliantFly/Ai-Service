package com.know.knowboot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

/**
 * 计划管理模块表结构迁移
 * <p>
 * 在应用启动时自动检查并创建计划管理相关的数据库表，
 * 以及初始化四象限、计划类型、日程分类等基础数据。
 * <p>
 * 使用 @PostConstruct + JdbcTemplate 模式（与 CameraSchemaMigration 一致），
 * 不依赖 Flyway，避免与现有数据库状态冲突。
 */
@Component
public class PlanSchemaMigration {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void migrateSchema() {
        System.out.println("[PlanSchemaMigrate] 检查计划模块表结构...");
        try {
            createPlanQuadrant();
            createPlanType();
            createPlanInfo();
            createPlanScheduleCategory();
            createPlanScheduleEvent();
            createPlanHabit();
            createPlanHabitRecord();
            createPlanFocusSession();
            createPlanHomeConfig();

            // 修复已有表可能缺失的列（兼容初始版本建表）
            ensureColumnExists("plan_schedule_event", "event_type", "tinyint DEFAULT 1 COMMENT '日程类型(1:日程 2:待办 3:提醒)' AFTER `content`");
            ensureColumnExists("plan_schedule_event", "tags", "varchar(500) DEFAULT NULL COMMENT '标签(逗号分隔)' AFTER `content`");
            ensureColumnExists("plan_schedule_event", "subtasks", "text DEFAULT NULL COMMENT '子任务JSON' AFTER `tags`");
            ensureColumnExists("plan_schedule_event", "note", "varchar(500) DEFAULT NULL COMMENT '备注' AFTER `subtasks`");
            ensureColumnExists("plan_schedule_event", "progress", "int DEFAULT 0 COMMENT '完成进度(0-100)' AFTER `note`");
            ensureColumnExists("plan_schedule_event", "priority", "tinyint DEFAULT 2 COMMENT '优先级(1:低 2:中 3:高)' AFTER `quadrant`");
            ensureColumnExists("plan_schedule_event", "category_id", "bigint DEFAULT NULL COMMENT '分类ID' AFTER `priority`");
            ensureColumnExists("plan_schedule_event", "plan_id", "bigint DEFAULT NULL COMMENT '关联计划ID' AFTER `category_id`");
            ensureColumnExists("plan_schedule_event", "is_all_day", "tinyint DEFAULT 0 COMMENT '是否全天(0:否 1:是)' AFTER `end_time`");
            ensureColumnExists("plan_schedule_event", "is_repeat", "tinyint DEFAULT 0 COMMENT '是否重复(0:否 1:是)' AFTER `is_all_day`");
            ensureColumnExists("plan_schedule_event", "repeat_type", "tinyint DEFAULT NULL COMMENT '重复类型(1:每日 2:每周 3:每月 4:每年)' AFTER `is_repeat`");
            ensureColumnExists("plan_schedule_event", "repeat_rule", "varchar(100) DEFAULT NULL COMMENT '重复规则JSON' AFTER `repeat_type`");
            ensureColumnExists("plan_schedule_event", "remind_time", "varchar(200) DEFAULT NULL COMMENT '提醒时间(逗号分隔多个)' AFTER `repeat_rule`");
            ensureColumnExists("plan_schedule_event", "remind_minutes", "int DEFAULT 15 COMMENT '提前提醒分钟数' AFTER `remind_time`");
            ensureColumnExists("plan_schedule_event", "completed_time", "bigint DEFAULT NULL COMMENT '完成时间' AFTER `status`");
            ensureColumnExists("plan_schedule_event", "delete_time", "bigint DEFAULT NULL COMMENT '删除时间' AFTER `update_time`");
            ensureColumnExists("plan_schedule_event", "color", "varchar(20) DEFAULT NULL COMMENT '自定义颜色' AFTER `completed_time`");
            ensureColumnExists("plan_habit", "frequency_type", "tinyint DEFAULT 1 COMMENT '频率类型(1:每天 2:每周 3:自定义)' AFTER `target_days`");
            ensureColumnExists("plan_habit", "frequency_rule", "varchar(100) DEFAULT NULL COMMENT '频率规则' AFTER `frequency_type`");
            ensureColumnExists("plan_habit", "start_date", "bigint DEFAULT NULL COMMENT '开始日期(时间戳)' AFTER `frequency_rule`");
            ensureColumnExists("plan_habit", "category", "varchar(50) DEFAULT NULL COMMENT '分类' AFTER `color`");
            ensureColumnExists("plan_habit", "target_value", "int DEFAULT 1 COMMENT '目标值' AFTER `category`");
            ensureColumnExists("plan_habit", "target_unit", "varchar(20) DEFAULT NULL COMMENT '目标单位' AFTER `target_value`");
            ensureColumnExists("plan_habit", "tracking_type", "varchar(20) DEFAULT 'boolean' COMMENT '打卡方式(boolean/numeric)' AFTER `target_unit`");
            ensureColumnExists("plan_habit", "note", "varchar(500) DEFAULT NULL COMMENT '备注' AFTER `reminder_time`");
            ensureColumnExists("plan_habit", "motto", "varchar(100) DEFAULT NULL COMMENT '激励语' AFTER `note`");
            ensureColumnExists("plan_habit", "time_period", "varchar(20) DEFAULT 'all' COMMENT '时间段(all/morning/noon/afternoon/evening)' AFTER `motto`");
            ensureColumnExists("plan_habit", "allow_backfill", "tinyint(1) DEFAULT 1 COMMENT '是否允许补卡' AFTER `time_period`");
            ensureColumnExists("plan_habit", "end_date", "bigint DEFAULT NULL COMMENT '结束日期(时间戳)' AFTER `start_date`");
            ensureColumnExists("plan_habit", "rest_days", "varchar(50) DEFAULT NULL COMMENT '休息日(逗号分隔，0-6)' AFTER `end_date`");
            ensureColumnExists("plan_habit", "second_reminder", "varchar(50) DEFAULT NULL COMMENT '第二提醒时间(HH:mm)' AFTER `reminder_time`");
            ensureColumnExists("plan_habit", "plan_id", "bigint DEFAULT NULL COMMENT '关联计划ID' AFTER `total_days`");
            ensureColumnExists("plan_habit", "delete_time", "bigint DEFAULT NULL COMMENT '删除时间' AFTER `update_time`");
            ensureColumnExists("plan_schedule_category", "delete_time", "bigint DEFAULT NULL COMMENT '删除时间' AFTER `update_time`");


            ensureBigintTimestampColumn("plan_habit", "start_date", "bigint DEFAULT NULL COMMENT 'start date timestamp'");
            ensureBigintTimestampColumn("plan_habit", "end_date", "bigint DEFAULT NULL COMMENT 'end date timestamp'");
            ensureBigintTimestampColumn("plan_habit_record", "record_date", "bigint NOT NULL COMMENT 'record date timestamp'");

            seedQuadrantData();
            seedPlanTypeData();
            seedScheduleCategoryData();

            System.out.println("[PlanSchemaMigrate] 计划模块表结构检查完成");
        } catch (Exception e) {
            System.err.println("[PlanSchemaMigrate] 迁移失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ============================
    // 1. 四象限表
    // ============================
    private void createPlanQuadrant() {
        if (!tableExists("plan_quadrant")) {
            jdbcTemplate.execute(
                "CREATE TABLE `plan_quadrant` (" +
                "  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键'," +
                "  `name` varchar(20) NOT NULL COMMENT '象限名称'," +
                "  `code` varchar(20) NOT NULL COMMENT '象限编码(Q1/Q2/Q3/Q4)'," +
                "  `color` varchar(20) NOT NULL COMMENT '显示颜色'," +
                "  `description` varchar(200) DEFAULT NULL COMMENT '描述'," +
                "  `sort` int DEFAULT 0 COMMENT '排序'," +
                "  `status` tinyint DEFAULT 1 COMMENT '状态(0:禁用 1:启用)'," +
                "  `create_time` bigint DEFAULT NULL COMMENT '创建时间'," +
                "  `update_time` bigint DEFAULT NULL COMMENT '更新时间'," +
                "  `delete_time` bigint DEFAULT NULL COMMENT '删除时间'," +
                "  PRIMARY KEY (`id`)," +
                "  UNIQUE KEY `uk_code` (`code`)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='四象限表'"
            );
            System.out.println("[PlanSchemaMigrate] 已创建 plan_quadrant 表");
        }
    }

    // ============================
    // 2. 计划类型表
    // ============================
    private void createPlanType() {
        if (!tableExists("plan_type")) {
            jdbcTemplate.execute(
                "CREATE TABLE `plan_type` (" +
                "  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键'," +
                "  `type_name` varchar(50) NOT NULL COMMENT '类型名称'," +
                "  `type_code` varchar(50) NOT NULL COMMENT '类型编码'," +
                "  `icon` varchar(100) DEFAULT NULL COMMENT '图标'," +
                "  `color` varchar(20) DEFAULT NULL COMMENT '颜色'," +
                "  `sort` int DEFAULT 0 COMMENT '排序'," +
                "  `status` tinyint DEFAULT 1 COMMENT '状态(0:禁用 1:启用)'," +
                "  PRIMARY KEY (`id`)," +
                "  UNIQUE KEY `uk_type_code` (`type_code`)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='计划类型表'"
            );
            System.out.println("[PlanSchemaMigrate] 已创建 plan_type 表");
        }
    }

    // ============================
    // 3. 计划表
    // ============================
    private void createPlanInfo() {
        if (!tableExists("plan_info")) {
            jdbcTemplate.execute(
                "CREATE TABLE `plan_info` (" +
                "  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键'," +
                "  `plan_name` varchar(100) NOT NULL COMMENT '计划名称'," +
                "  `plan_type` varchar(50) NOT NULL COMMENT '计划分类(对应plan_type.type_code)'," +
                "  `category_id` bigint DEFAULT NULL COMMENT '分类ID'," +
                "  `target_effect` varchar(500) DEFAULT NULL COMMENT '目标效果'," +
                "  `quadrant_id` bigint DEFAULT 2 COMMENT '所属象限(默认重要不紧急)'," +
                "  `priority` int DEFAULT 0 COMMENT '优先级 0-10'," +
                "  `status` tinyint DEFAULT 0 COMMENT '状态: 0-待开始, 1-进行中, 2-已完成, 3-已取消'," +
                "  `progress` int DEFAULT 0 COMMENT '进度百分比 0-100'," +
                "  `plan_start_time` bigint DEFAULT NULL COMMENT '计划开始时间'," +
                "  `plan_end_time` bigint DEFAULT NULL COMMENT '计划结束时间'," +
                "  `actual_start_time` bigint DEFAULT NULL COMMENT '实际开始时间'," +
                "  `actual_end_time` bigint DEFAULT NULL COMMENT '实际结束时间'," +
                "  `leader_id` bigint DEFAULT NULL COMMENT '负责人ID'," +
                "  `participant_ids` varchar(500) DEFAULT NULL COMMENT '参与人ID列表(逗号分隔)'," +
                "  `parent_id` bigint DEFAULT NULL COMMENT '父计划ID(支持WBS拆解)'," +
                "  `remark` varchar(500) DEFAULT NULL COMMENT '备注'," +
                "  `create_by` bigint DEFAULT NULL COMMENT '创建人'," +
                "  `create_time` bigint DEFAULT NULL COMMENT '创建时间'," +
                "  `update_by` bigint DEFAULT NULL COMMENT '更新人'," +
                "  `update_time` bigint DEFAULT NULL COMMENT '更新时间'," +
                "  `delete_time` bigint DEFAULT NULL COMMENT '删除时间'," +
                "  PRIMARY KEY (`id`)," +
                "  KEY `idx_parent_id` (`parent_id`)," +
                "  KEY `idx_plan_type` (`plan_type`)," +
                "  KEY `idx_quadrant_id` (`quadrant_id`)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='计划表'"
            );
            System.out.println("[PlanSchemaMigrate] 已创建 plan_info 表");
        }
    }

    // ============================
    // 4. 日程分类表
    // ============================
    private void createPlanScheduleCategory() {
        if (!tableExists("plan_schedule_category")) {
            jdbcTemplate.execute(
                "CREATE TABLE `plan_schedule_category` (" +
                "  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键'," +
                "  `name` varchar(50) NOT NULL COMMENT '分类名称'," +
                "  `color` varchar(20) DEFAULT '#1890FF' COMMENT '颜色'," +
                "  `icon` varchar(50) DEFAULT NULL COMMENT '图标'," +
                "  `sort` int DEFAULT 0 COMMENT '排序'," +
                "  `user_id` bigint DEFAULT NULL COMMENT '用户ID'," +
                "  `create_by` bigint DEFAULT NULL COMMENT '创建人'," +
                "  `create_time` bigint DEFAULT NULL COMMENT '创建时间'," +
                "  `update_by` bigint DEFAULT NULL COMMENT '更新人'," +
                "  `update_time` bigint DEFAULT NULL COMMENT '更新时间'," +
                "  `delete_time` bigint DEFAULT NULL COMMENT '删除时间'," +
                "  PRIMARY KEY (`id`)," +
                "  KEY `idx_user_id` (`user_id`)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='日程分类表'"
            );
            System.out.println("[PlanSchemaMigrate] 已创建 plan_schedule_category 表");
        }
    }

    // ============================
    // 5. 日程事件表
    // ============================
    private void createPlanScheduleEvent() {
        if (!tableExists("plan_schedule_event")) {
            jdbcTemplate.execute(
                "CREATE TABLE `plan_schedule_event` (" +
                "  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键'," +
                "  `title` varchar(200) NOT NULL COMMENT '日程标题'," +
                "  `content` text COMMENT '日程内容'," +
                "  `tags` varchar(500) DEFAULT NULL COMMENT '标签(逗号分隔)'," +
                "  `subtasks` text COMMENT '子任务JSON'," +
                "  `note` varchar(500) DEFAULT NULL COMMENT '备注'," +
                "  `progress` int DEFAULT 0 COMMENT '完成进度(0-100)'," +
                "  `event_type` tinyint DEFAULT 1 COMMENT '日程类型(1:日程 2:待办 3:提醒)'," +
                "  `quadrant` tinyint DEFAULT 2 COMMENT '四象限(1:重要紧急 2:重要不紧急 3:紧急不重要 4:不紧急不重要)'," +
                "  `priority` tinyint DEFAULT 2 COMMENT '优先级(1:低 2:中 3:高)'," +
                "  `category_id` bigint DEFAULT NULL COMMENT '分类ID'," +
                "  `plan_id` bigint DEFAULT NULL COMMENT '关联计划ID'," +
                "  `start_time` bigint DEFAULT NULL COMMENT '开始时间'," +
                "  `end_time` bigint DEFAULT NULL COMMENT '结束时间'," +
                "  `is_all_day` tinyint DEFAULT 0 COMMENT '是否全天(0:否 1:是)'," +
                "  `is_repeat` tinyint DEFAULT 0 COMMENT '是否重复(0:否 1:是)'," +
                "  `repeat_type` tinyint DEFAULT NULL COMMENT '重复类型(1:每日 2:每周 3:每月 4:每年)'," +
                "  `repeat_rule` varchar(100) DEFAULT NULL COMMENT '重复规则JSON'," +
                "  `remind_time` varchar(200) DEFAULT NULL COMMENT '提醒时间(逗号分隔多个)'," +
                "  `remind_minutes` int DEFAULT 15 COMMENT '提前提醒分钟数'," +
                "  `location` varchar(200) DEFAULT NULL COMMENT '地点'," +
                "  `status` tinyint DEFAULT 0 COMMENT '状态(0:未完成 1:已完成)'," +
                "  `completed_time` bigint DEFAULT NULL COMMENT '完成时间'," +
                "  `color` varchar(20) DEFAULT NULL COMMENT '自定义颜色'," +
                "  `user_id` bigint DEFAULT NULL COMMENT '用户ID'," +
                "  `create_by` bigint DEFAULT NULL COMMENT '创建人'," +
                "  `create_time` bigint DEFAULT NULL COMMENT '创建时间'," +
                "  `update_by` bigint DEFAULT NULL COMMENT '更新人'," +
                "  `update_time` bigint DEFAULT NULL COMMENT '更新时间'," +
                "  `delete_time` bigint DEFAULT NULL COMMENT '删除时间'," +
                "  PRIMARY KEY (`id`)," +
                "  KEY `idx_user_id` (`user_id`)," +
                "  KEY `idx_category_id` (`category_id`)," +
                "  KEY `idx_start_time` (`start_time`)," +
                "  KEY `idx_quadrant` (`quadrant`)," +
                "  KEY `idx_status` (`status`)," +
                "  KEY `idx_plan_id` (`plan_id`)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='日程事件表'"
            );
            System.out.println("[PlanSchemaMigrate] 已创建 plan_schedule_event 表");
        }
    }

    // ============================
    // 6. 习惯表
    // ============================
    private void createPlanHabit() {
        if (!tableExists("plan_habit")) {
            jdbcTemplate.execute(
                "CREATE TABLE `plan_habit` (" +
                "  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键'," +
                "  `name` varchar(100) NOT NULL COMMENT '习惯名称'," +
                "  `description` text COMMENT '描述'," +
                "  `icon` varchar(50) DEFAULT NULL COMMENT '图标'," +
                "  `color` varchar(20) DEFAULT '#52C41A' COMMENT '颜色'," +
                "  `category` varchar(50) DEFAULT NULL COMMENT '分类'," +
                "  `target_value` int DEFAULT 1 COMMENT '目标值'," +
                "  `target_unit` varchar(20) DEFAULT NULL COMMENT '目标单位'," +
                "  `tracking_type` varchar(20) DEFAULT 'boolean' COMMENT '打卡方式(boolean/numeric)'," +
                "  `target_days` int DEFAULT 30 COMMENT '目标天数'," +
                "  `frequency_type` tinyint DEFAULT 1 COMMENT '频率类型(1:每天 2:每周 3:自定义)'," +
                "  `frequency_rule` varchar(100) DEFAULT NULL COMMENT '频率规则'," +
                "  `start_date` bigint DEFAULT NULL COMMENT '开始日期(时间戳)'," +
                "  `end_date` bigint DEFAULT NULL COMMENT '结束日期(时间戳)'," +
                "  `rest_days` varchar(50) DEFAULT NULL COMMENT '休息日(逗号分隔，0-6)'," +
                "  `reminder_time` varchar(50) DEFAULT NULL COMMENT '提醒时间(HH:mm)'," +
                "  `second_reminder` varchar(50) DEFAULT NULL COMMENT '第二提醒时间(HH:mm)'," +
                "  `note` varchar(500) DEFAULT NULL COMMENT '备注'," +
                "  `motto` varchar(100) DEFAULT NULL COMMENT '激励语'," +
                "  `time_period` varchar(20) DEFAULT 'all' COMMENT '时间段(all/morning/noon/afternoon/evening)'," +
                "  `allow_backfill` tinyint(1) DEFAULT 1 COMMENT '是否允许补卡'," +
                "  `status` tinyint DEFAULT 0 COMMENT '状态(0:进行中 1:已完成 2:已放弃)'," +
                "  `current_days` int DEFAULT 0 COMMENT '当前连续天数'," +
                "  `total_days` int DEFAULT 0 COMMENT '累计打卡天数'," +
                "  `plan_id` bigint DEFAULT NULL COMMENT '关联计划ID'," +
                "  `user_id` bigint DEFAULT NULL COMMENT '用户ID'," +
                "  `create_by` bigint DEFAULT NULL COMMENT '创建人'," +
                "  `create_time` bigint DEFAULT NULL COMMENT '创建时间'," +
                "  `update_by` bigint DEFAULT NULL COMMENT '更新人'," +
                "  `update_time` bigint DEFAULT NULL COMMENT '更新时间'," +
                "  `delete_time` bigint DEFAULT NULL COMMENT '删除时间'," +
                "  PRIMARY KEY (`id`)," +
                "  KEY `idx_user_id` (`user_id`)," +
                "  KEY `idx_status` (`status`)," +
                "  KEY `idx_plan_id` (`plan_id`)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='习惯表'"
            );
            System.out.println("[PlanSchemaMigrate] 已创建 plan_habit 表");
        }
    }

    // ============================
    // 7. 打卡记录表
    // ============================
    private void createPlanHabitRecord() {
        if (!tableExists("plan_habit_record")) {
            jdbcTemplate.execute(
                "CREATE TABLE `plan_habit_record` (" +
                "  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键'," +
                "  `habit_id` bigint NOT NULL COMMENT '习惯ID'," +
                "  `record_date` bigint NOT NULL COMMENT '打卡日期(时间戳)'," +
                "  `remark` varchar(200) DEFAULT NULL COMMENT '备注'," +
                "  `images` varchar(500) DEFAULT NULL COMMENT '图片(多张逗号分隔)'," +
                "  `user_id` bigint DEFAULT NULL COMMENT '用户ID'," +
                "  `create_by` bigint DEFAULT NULL COMMENT '创建人'," +
                "  `create_time` bigint DEFAULT NULL COMMENT '创建时间'," +
                "  PRIMARY KEY (`id`)," +
                "  UNIQUE KEY `uk_habit_date` (`habit_id`, `record_date`)," +
                "  KEY `idx_user_id` (`user_id`)," +
                "  KEY `idx_record_date` (`record_date`)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='打卡记录表'"
            );
            System.out.println("[PlanSchemaMigrate] 已创建 plan_habit_record 表");
        }
    }

    // ============================
    // 8. 首页配置表
    // ============================
    private void createPlanFocusSession() {
        if (!tableExists("plan_focus_session")) {
            jdbcTemplate.execute(
                "CREATE TABLE `plan_focus_session` (" +
                "  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键'," +
                "  `user_id` bigint DEFAULT NULL COMMENT '用户ID'," +
                "  `phase` tinyint DEFAULT 0 COMMENT '阶段类型(0:专注 1:短休息 2:长休息)'," +
                "  `duration` int DEFAULT 0 COMMENT '时长(秒)'," +
                "  `start_time` bigint DEFAULT NULL COMMENT '开始时间'," +
                "  `end_time` bigint DEFAULT NULL COMMENT '结束时间'," +
                "  `remark` varchar(200) DEFAULT NULL COMMENT '备注'," +
                "  `create_by` bigint DEFAULT NULL COMMENT '创建人'," +
                "  `create_time` bigint DEFAULT NULL COMMENT '创建时间'," +
                "  PRIMARY KEY (`id`)," +
                "  KEY `idx_user_id` (`user_id`)," +
                "  KEY `idx_start_time` (`start_time`)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='番茄专注记录表'"
            );
            System.out.println("[PlanSchemaMigrate] 已创建 plan_focus_session 表");
        }
    }

    private void createPlanHomeConfig() {
        if (!tableExists("plan_home_config")) {
            jdbcTemplate.execute(
                "CREATE TABLE `plan_home_config` (" +
                "  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键'," +
                "  `config_type` varchar(50) NOT NULL COMMENT '配置类型(banner:轮播图 notice:通知 menu:滚动菜单 grid:九宫格)'," +
                "  `title` varchar(100) DEFAULT NULL COMMENT '标题'," +
                "  `content` text COMMENT '配置内容JSON'," +
                "  `icon` varchar(100) DEFAULT NULL COMMENT '图标'," +
                "  `link` varchar(200) DEFAULT NULL COMMENT '链接地址'," +
                "  `sort` int DEFAULT 0 COMMENT '排序'," +
                "  `status` tinyint DEFAULT 1 COMMENT '状态(0:禁用 1:启用)'," +
                "  `role_id` varchar(32) DEFAULT NULL COMMENT '角色ID(null表示全局)'," +
                "  `create_by` bigint DEFAULT NULL COMMENT '创建人'," +
                "  `create_time` bigint DEFAULT NULL COMMENT '创建时间'," +
                "  `update_by` bigint DEFAULT NULL COMMENT '更新人'," +
                "  `update_time` bigint DEFAULT NULL COMMENT '更新时间'," +
                "  PRIMARY KEY (`id`)," +
                "  KEY `idx_config_type` (`config_type`)," +
                "  KEY `idx_role_id` (`role_id`)," +
                "  KEY `idx_status` (`status`)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='首页配置表'"
            );
            System.out.println("[PlanSchemaMigrate] 已创建 plan_home_config 表");
        }
    }

    // ============================
    // 种子数据
    // ============================

    private void seedQuadrantData() {
        try {
            Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM plan_quadrant", Integer.class);
            if (count != null && count == 0) {
                long now = System.currentTimeMillis();
                jdbcTemplate.execute("INSERT INTO plan_quadrant (name, code, color, description, sort, status, create_time, update_time) VALUES " +
                    "('重要且紧急', 'Q1', '#FF6B6B', '需要立即处理', 1, 1, " + now + ", " + now + "), " +
                    "('重要不紧急', 'Q2', '#4ECDC4', '需要规划安排', 2, 1, " + now + ", " + now + "), " +
                    "('不重要紧急', 'Q3', '#FFE66D', '可委托他人', 3, 1, " + now + ", " + now + "), " +
                    "('不重要不紧急', 'Q4', '#95A5A6', '尽量避免', 4, 1, " + now + ", " + now + ")");
                System.out.println("[PlanSchemaMigrate] 已初始化四象限种子数据");
            }
        } catch (Exception e) {
            System.out.println("[PlanSchemaMigrate] 初始化四象限数据: " + e.getMessage());
        }
    }

    private void seedPlanTypeData() {
        try {
            Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM plan_type", Integer.class);
            if (count != null && count == 0) {
                jdbcTemplate.execute("INSERT INTO plan_type (type_name, type_code, icon, color, sort, status) VALUES " +
                    "('生活习惯', 'life', '🏃', '#67C23A', 1, 1), " +
                    "('认知提升', 'cognition', '📚', '#409EFF', 2, 1), " +
                    "('工作技能', 'skill', '💼', '#E6A23C', 3, 1), " +
                    "('项目', 'project', '📦', '#909399', 4, 1), " +
                    "('兴趣爱好', 'hobby', '🎨', '#F56C6C', 5, 1), " +
                    "('学习', 'study', '🎵', '#8E44AD', 6, 1)");
                System.out.println("[PlanSchemaMigrate] 已初始化计划类型种子数据");
            }
        } catch (Exception e) {
            System.out.println("[PlanSchemaMigrate] 初始化计划类型数据: " + e.getMessage());
        }
    }

    private void seedScheduleCategoryData() {
        try {
            Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM plan_schedule_category", Integer.class);
            if (count != null && count == 0) {
                long now = System.currentTimeMillis();
                jdbcTemplate.execute("INSERT INTO plan_schedule_category (name, color, icon, sort, create_time) VALUES " +
                    "('工作', '#1890FF', 'briefcase', 1, " + now + "), " +
                    "('学习', '#52C41A', 'book', 2, " + now + "), " +
                    "('生活', '#FAAD14', 'home', 3, " + now + "), " +
                    "('健康', '#FF4D4F', 'heart', 4, " + now + ")");
                System.out.println("[PlanSchemaMigrate] 已初始化日程分类种子数据");
            }
        } catch (Exception e) {
            System.out.println("[PlanSchemaMigrate] 初始化日程分类数据: " + e.getMessage());
        }
    }

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

    /**
     * 检查列是否存在，如果不存在则添加
     */
    private void ensureColumnExists(String tableName, String columnName, String columnDefinition) {
        try {
            Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS " +
                "WHERE TABLE_SCHEMA = (SELECT DATABASE()) AND TABLE_NAME = ? AND COLUMN_NAME = ?",
                Integer.class, tableName, columnName);
            if (count == null || count == 0) {
                jdbcTemplate.execute(
                    "ALTER TABLE `" + tableName + "` ADD COLUMN `" + columnName + "` " + columnDefinition);
                System.out.println("[PlanSchemaMigrate] 已添加缺失列 " + tableName + "." + columnName);
            }
        } catch (Exception e) {
            System.out.println("[PlanSchemaMigrate] 检查列 " + tableName + "." + columnName + ": " + e.getMessage());
        }
    }

    private void ensureBigintTimestampColumn(String tableName, String columnName, String columnDefinition) {
        try {
            String dataType = jdbcTemplate.queryForObject(
                "SELECT DATA_TYPE FROM INFORMATION_SCHEMA.COLUMNS " +
                "WHERE TABLE_SCHEMA = (SELECT DATABASE()) AND TABLE_NAME = ? AND COLUMN_NAME = ?",
                String.class, tableName, columnName);
            if ("bigint".equalsIgnoreCase(dataType)) {
                return;
            }

            jdbcTemplate.execute(
                "ALTER TABLE `" + tableName + "` MODIFY COLUMN `" + columnName + "` " + columnDefinition);
            jdbcTemplate.execute(
                "UPDATE `" + tableName + "` SET `" + columnName + "` = CASE " +
                    "WHEN `" + columnName + "` IS NULL THEN NULL " +
                    "WHEN `" + columnName + "` BETWEEN 10000101000000 AND 99991231235959 " +
                        "THEN UNIX_TIMESTAMP(STR_TO_DATE(CAST(`" + columnName + "` AS CHAR), '%Y%m%d%H%i%s')) * 1000 " +
                    "WHEN `" + columnName + "` BETWEEN 10000101 AND 99991231 " +
                        "THEN UNIX_TIMESTAMP(STR_TO_DATE(CAST(`" + columnName + "` AS CHAR), '%Y%m%d')) * 1000 " +
                    "WHEN `" + columnName + "` BETWEEN 1000000000 AND 9999999999 " +
                        "THEN `" + columnName + "` * 1000 " +
                    "ELSE `" + columnName + "` END " +
                "WHERE `" + columnName + "` IS NOT NULL AND `" + columnName + "` < 1000000000000");
            System.out.println("[PlanSchemaMigrate] Fixed timestamp column " + tableName + "." + columnName);
        } catch (Exception e) {
            System.out.println("[PlanSchemaMigrate] Fix timestamp column " + tableName + "." + columnName + ": " + e.getMessage());
        }
    }
}
