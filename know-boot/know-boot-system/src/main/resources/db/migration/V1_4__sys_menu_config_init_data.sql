-- ----------------------------
-- 菜单配置初始化数据
-- ----------------------------

-- TabBar 菜单（菜单类型 1=tabBar）
-- 图标使用 emoji 字符（服务端动态渲染，前端自动识别 emoji 与图片路径）
INSERT IGNORE INTO `sys_menu_config` (`menu_name`, `menu_code`, `menu_type`, `icon`, `selected_icon`, `path`, `is_show`, `is_big`, `sort`, `render_type`, `create_time`)
VALUES
('首页',   'home',    1, '🏠', '🏠', '/pages/index/index',     1, 0, 1, 1, NOW()),
('设备',   'device',  1, '📹', '📹', '/pages/camera/index',    1, 0, 2, 1, NOW()),
('计划',   'plan',    1, '📅', '📅', '/pages/plan/schedule/index', 1, 0, 3, 1, NOW()),
('客户',   'customer',1, '👥', '👥', '/pages/customer/info',   1, 0, 4, 1, NOW()),
('我的',   'profile', 1, '👤', '👤', '/pages/user/user',       1, 0, 5, 1, NOW());

-- 首页菜单（菜单类型 2=首页/功能菜单）
-- icon 字段存储 emoji 字符，前端快速菜单自动识别渲染
INSERT IGNORE INTO `sys_menu_config` (`menu_name`, `menu_code`, `menu_type`, `icon`, `path`, `is_show`, `sort`, `render_type`, `create_time`)
VALUES
('客户管理', 'customer',   2, '👥', '/pages/customer/info',                        1, 1, 1, NOW()),
('我的收藏', 'collection', 2, '⭐', '/pages/collection/collection',                1, 2, 1, NOW()),
('联系客服', 'service',    2, '💬', '/pages/customer_service/customer_service',    1, 3, 1, NOW());
