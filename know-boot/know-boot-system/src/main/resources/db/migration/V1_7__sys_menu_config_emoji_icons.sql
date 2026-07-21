-- ----------------------------
-- 菜单配置图标升级：静态图片 → emoji 字符图标
-- 前端已适配：custom-tab-bar 自动识别 emoji（非图片路径）并渲染为大号文本
-- ----------------------------

-- TabBar 菜单：替换旧图片路径为 emoji
UPDATE `sys_menu_config`
SET `icon` = '🏠', `selected_icon` = '🏠'
WHERE `menu_code` = 'home' AND `menu_type` = 1;

UPDATE `sys_menu_config`
SET `icon` = '👤', `selected_icon` = '👤'
WHERE `menu_code` = 'profile' AND `menu_type` = 1;

-- 原"文章" tab 改为"计划"
UPDATE `sys_menu_config`
SET `menu_name` = '计划', `menu_code` = 'plan', `icon` = '📅', `selected_icon` = '📅', `path` = '/pages/plan/schedule/index', `sort` = 3
WHERE `menu_code` = 'article' AND `menu_type` = 1;

-- TabBar 新增"设备"（如果不存在）
INSERT IGNORE INTO `sys_menu_config` (`menu_name`, `menu_code`, `menu_type`, `icon`, `selected_icon`, `path`, `is_show`, `is_big`, `sort`, `render_type`, `create_time`)
VALUES ('设备', 'device', 1, '📹', '📹', '/pages/camera/index', 1, 0, 2, 1, NOW());

-- TabBar 新增"客户"（如果不存在）
INSERT IGNORE INTO `sys_menu_config` (`menu_name`, `menu_code`, `menu_type`, `icon`, `selected_icon`, `path`, `is_show`, `is_big`, `sort`, `render_type`, `create_time`)
VALUES ('客户', 'customer', 1, '👥', '👥', '/pages/customer/info', 1, 0, 4, 1, NOW());

-- 首页菜单：补充 emoji 图标
UPDATE `sys_menu_config` SET `icon` = '👥' WHERE `menu_code` = 'customer' AND `menu_type` = 2 AND (`icon` IS NULL OR `icon` = '');
UPDATE `sys_menu_config` SET `icon` = '⭐' WHERE `menu_code` = 'collection' AND `menu_type` = 2 AND (`icon` IS NULL OR `icon` = '');
UPDATE `sys_menu_config` SET `icon` = '💬' WHERE `menu_code` = 'service' AND `menu_type` = 2 AND (`icon` IS NULL OR `icon` = '');
UPDATE `sys_menu_config` SET `icon` = '📹' WHERE `menu_code` = 'camera' AND `menu_type` = 2 AND (`icon` IS NULL OR `icon` = '');
UPDATE `sys_menu_config` SET `icon` = '🎯' WHERE `menu_code` = 'habit' AND `menu_type` = 2 AND (`icon` IS NULL OR `icon` = '');
UPDATE `sys_menu_config` SET `icon` = '📅' WHERE `menu_code` = 'schedule' AND `menu_type` = 2 AND (`icon` IS NULL OR `icon` = '');
