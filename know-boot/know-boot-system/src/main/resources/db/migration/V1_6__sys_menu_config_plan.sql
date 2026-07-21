-- ----------------------------
-- 计划管理模块菜单配置
-- 习惯管理、日程管理
-- ----------------------------

-- 首页菜单：计划管理（菜单类型 2=首页/功能菜单）
INSERT IGNORE INTO `sys_menu_config` (`menu_name`, `menu_code`, `menu_type`, `icon`, `path`, `is_show`, `sort`, `render_type`, `create_time`)
VALUES
('习惯管理', 'habit', 2, '🎯', '/pages/plan/habit/index', 1, 5, 1, NOW()),
('日程管理', 'schedule', 2, '📅', '/pages/plan/schedule/index', 1, 6, 1, NOW());
