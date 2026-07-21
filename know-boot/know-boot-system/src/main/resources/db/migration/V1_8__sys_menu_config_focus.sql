-- ----------------------------
-- 番茄专注首页菜单
-- 菜单类型 2 = 首页/功能菜单
-- 排序放在习惯管理后面
-- ----------------------------

INSERT IGNORE INTO `sys_menu_config`
(`menu_name`, `menu_code`, `menu_type`, `icon`, `path`, `is_show`, `sort`, `render_type`, `create_time`)
VALUES
('番茄专注', 'focus', 2, '🍅', '/pages/plan/focus/index', 1, 7, 1, NOW());
