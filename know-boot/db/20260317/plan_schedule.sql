-- ===============================================
-- 日程计划管理系统数据库表结构
-- 创建时间: 2026-03-17
-- ===============================================

-- ----------------------------
-- 1. 日程分类表
-- ----------------------------
DROP TABLE IF EXISTS `plan_schedule_category`;
CREATE TABLE `plan_schedule_category` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(50) NOT NULL COMMENT '分类名称',
  `color` varchar(20) DEFAULT '#1890FF' COMMENT '颜色',
  `icon` varchar(50) DEFAULT NULL COMMENT '图标',
  `sort` int DEFAULT 0 COMMENT '排序',
  `user_id` bigint DEFAULT NULL COMMENT '用户ID',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` tinyint DEFAULT 0 COMMENT '删除标志(0:正常 1:删除)',
  PRIMARY KEY (`id`),
  INDEX `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='日程分类表';

-- ----------------------------
-- 2. 日程事件表
-- ----------------------------
DROP TABLE IF EXISTS `plan_schedule_event`;
CREATE TABLE `plan_schedule_event` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `title` varchar(200) NOT NULL COMMENT '日程标题',
  `content` text COMMENT '日程内容',
  `event_type` tinyint DEFAULT 1 COMMENT '日程类型(1:日程 2:待办)',
  `quadrant` tinyint DEFAULT 2 COMMENT '四象限(1:重要紧急 2:重要不紧急 3:紧急不重要 4:不紧急不重要)',
  `priority` tinyint DEFAULT 2 COMMENT '优先级(1:低 2:中 3:高)',
  `category_id` bigint DEFAULT NULL COMMENT '分类ID',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `is_all_day` tinyint DEFAULT 0 COMMENT '是否全天(0:否 1:是)',
  `is_repeat` tinyint DEFAULT 0 COMMENT '是否重复(0:否 1:是)',
  `repeat_type` tinyint DEFAULT NULL COMMENT '重复类型(1:每日 2:每周 3:每月 4:每年)',
  `repeat_rule` varchar(100) DEFAULT NULL COMMENT '重复规则JSON',
  `remind_time` varchar(200) DEFAULT NULL COMMENT '提醒时间(多个逗号分隔)',
  `location` varchar(200) DEFAULT NULL COMMENT '地点',
  `status` tinyint DEFAULT 0 COMMENT '状态(0:未完成 1:已完成)',
  `completed_time` datetime DEFAULT NULL COMMENT '完成时间',
  `user_id` bigint DEFAULT NULL COMMENT '用户ID',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` tinyint DEFAULT 0 COMMENT '删除标志(0:正常 1:删除)',
  PRIMARY KEY (`id`),
  INDEX `idx_user_id` (`user_id`),
  INDEX `idx_category_id` (`category_id`),
  INDEX `idx_start_time` (`start_time`),
  INDEX `idx_quadrant` (`quadrant`),
  INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='日程事件表';

-- ----------------------------
-- 3. 习惯表
-- ----------------------------
DROP TABLE IF EXISTS `plan_habit`;
CREATE TABLE `plan_habit` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(100) NOT NULL COMMENT '习惯名称',
  `description` text COMMENT '描述',
  `icon` varchar(50) DEFAULT NULL COMMENT '图标',
  `color` varchar(20) DEFAULT '#52C41A' COMMENT '颜色',
  `target_days` int DEFAULT 30 COMMENT '目标天数',
  `frequency_type` tinyint DEFAULT 1 COMMENT '频率类型(1:每天 2:每周 3:自定义)',
  `frequency_rule` varchar(100) DEFAULT NULL COMMENT '频率规则',
  `start_date` date DEFAULT NULL COMMENT '开始日期',
  `reminder_time` varchar(50) DEFAULT NULL COMMENT '提醒时间',
  `status` tinyint DEFAULT 0 COMMENT '状态(0:进行中 1:已完成 2:已放弃)',
  `current_days` int DEFAULT 0 COMMENT '当前连续天数',
  `total_days` int DEFAULT 0 COMMENT '累计打卡天数',
  `user_id` bigint DEFAULT NULL COMMENT '用户ID',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` tinyint DEFAULT 0 COMMENT '删除标志(0:正常 1:删除)',
  PRIMARY KEY (`id`),
  INDEX `idx_user_id` (`user_id`),
  INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='习惯表';

-- ----------------------------
-- 4. 打卡记录表
-- ----------------------------
DROP TABLE IF EXISTS `plan_habit_record`;
CREATE TABLE `plan_habit_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `habit_id` bigint NOT NULL COMMENT '习惯ID',
  `record_date` date NOT NULL COMMENT '打卡日期',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `images` varchar(500) DEFAULT NULL COMMENT '图片(多张逗号分隔)',
  `user_id` bigint DEFAULT NULL COMMENT '用户ID',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `uk_habit_date` (`habit_id`, `record_date`),
  INDEX `idx_user_id` (`user_id`),
  INDEX `idx_record_date` (`record_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='打卡记录表';

-- ----------------------------
-- 5. 预置默认分类数据
-- ----------------------------
INSERT INTO `plan_schedule_category` (`name`, `color`, `icon`, `sort`) VALUES 
('工作', '#1890FF', 'briefcase', 1),
('学习', '#52C41A', 'book', 2),
('生活', '#FAAD14', 'home', 3),
('健康', '#FF4D4F', 'heart', 4);

-- ----------------------------
-- 四象限说明
-- ----------------------------
-- 1: 重要且紧急 (#FF4D4F 红色)
-- 2: 重要不紧急 (#1890FF 蓝色)
-- 3: 紧急不重要 (#FAAD14 黄色)
-- 4: 不紧急不重要 (#8C8C8F 灰色)
