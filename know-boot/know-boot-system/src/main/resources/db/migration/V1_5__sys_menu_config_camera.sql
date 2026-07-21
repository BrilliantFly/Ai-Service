-- ----------------------------
-- 摄像头模块菜单配置
-- ----------------------------

-- 首页菜单：摄像头管理（菜单类型 2=首页/功能菜单）
INSERT IGNORE INTO `sys_menu_config` (`menu_name`, `menu_code`, `menu_type`, `icon`, `path`, `is_show`, `sort`, `render_type`, `create_time`)
VALUES
('摄像头', 'camera', 2, '📹', '/pages/camera/index', 1, 4, 1, NOW());
