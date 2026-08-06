-- ===============================================
-- 日程重复功能：为 plan_schedule_event 表增加重复结束日期字段
-- 创建时间: 2026-08-06
-- 用途: 支持重复日程设置结束日期(repeat_end_date)，配合后端动态展开重复实例
-- ===============================================

ALTER TABLE `plan_schedule_event`
    ADD COLUMN `repeat_end_date` bigint DEFAULT NULL COMMENT '重复结束日期(毫秒时间戳, NULL=无限重复)' AFTER `repeat_rule`;
