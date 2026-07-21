-- ============================================
-- 菜单初始化脚本
-- 根据前端路由动态生成菜单
-- 日期: 20260324
-- ============================================

SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- 1. 插入菜单数据
-- ----------------------------

-- 系统设置父菜单 (id=1)
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `path`, `component`, `perms`, `icon`, `menu_type`, `sort`, `status`, `create_by`, `create_time`) 
VALUES (1, 0, '系统设置', '/system', NULL, 'system:view', 'system', 1, 14, 1, 'admin', UNIX_TIMESTAMP() * 1000)
ON DUPLICATE KEY UPDATE `menu_name` = '系统设置', `path` = '/system', `icon` = 'system', `sort` = 14;

-- 用户设置 (id=2)
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `path`, `component`, `perms`, `icon`, `menu_type`, `sort`, `status`, `create_by`, `create_time`) 
VALUES (2, 1, '用户设置', 'user', 'system/user', 'system:user:view', 'user', 2, 1, 1, 'admin', UNIX_TIMESTAMP() * 1000)
ON DUPLICATE KEY UPDATE `menu_name` = '用户设置', `path` = 'user', `component` = 'system/user';

-- 角色设置 (id=3)
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `path`, `component`, `perms`, `icon`, `menu_type`, `sort`, `status`, `create_by`, `create_time`) 
VALUES (3, 1, '角色设置', 'role', 'system/role', 'system:role:view', 'peoples', 2, 2, 1, 'admin', UNIX_TIMESTAMP() * 1000)
ON DUPLICATE KEY UPDATE `menu_name` = '角色设置', `path` = 'role', `component` = 'system/role';

-- 菜单设置 (id=4)
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `path`, `component`, `perms`, `icon`, `menu_type`, `sort`, `status`, `create_by`, `create_time`) 
VALUES (4, 1, '菜单设置', 'menu', 'system/menu', 'system:menu:view', 'menu', 2, 3, 1, 'admin', UNIX_TIMESTAMP() * 1000)
ON DUPLICATE KEY UPDATE `menu_name` = '菜单设置', `path` = 'menu', `component` = 'system/menu';

-- 部门设置 (id=5)
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `path`, `component`, `perms`, `icon`, `menu_type`, `sort`, `status`, `create_by`, `create_time`) 
VALUES (5, 1, '部门设置', 'dept', 'system/dept', 'system:dept:view', 'tree-table', 2, 4, 1, 'admin', UNIX_TIMESTAMP() * 1000)
ON DUPLICATE KEY UPDATE `menu_name` = '部门设置', `path` = 'dept', `component` = 'system/dept';

-- 岗位设置 (id=6)
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `path`, `component`, `perms`, `icon`, `menu_type`, `sort`, `status`, `create_by`, `create_time`) 
VALUES (6, 1, '岗位设置', 'job', 'system/job', 'system:job:view', 'job', 2, 5, 1, 'admin', UNIX_TIMESTAMP() * 1000)
ON DUPLICATE KEY UPDATE `menu_name` = '岗位设置', `path` = 'job', `component` = 'system/job';

-- 字典设置 (id=7)
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `path`, `component`, `perms`, `icon`, `menu_type`, `sort`, `status`, `create_by`, `create_time`) 
VALUES (7, 1, '字典设置', 'dict', 'system/dict', 'system:dict:view', 'dictionary', 2, 6, 1, 'admin', UNIX_TIMESTAMP() * 1000)
ON DUPLICATE KEY UPDATE `menu_name` = '字典设置', `path` = 'dict', `component` = 'system/dict';

-- 底部导航 (id=8)
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `path`, `component`, `perms`, `icon`, `menu_type`, `sort`, `status`, `create_by`, `create_time`) 
VALUES (8, 1, '底部导航', 'tabbar', 'system/tabbar', 'system:tabbar:view', 'tablet', 2, 7, 1, 'admin', UNIX_TIMESTAMP() * 1000)
ON DUPLICATE KEY UPDATE `menu_name` = '底部导航', `path` = 'tabbar', `component` = 'system/tabbar';

-- 摄像头管理 (id=9)
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `path`, `component`, `perms`, `icon`, `menu_type`, `sort`, `status`, `create_by`, `create_time`) 
VALUES (9, 1, '摄像头管理', 'camera', 'system/camera', 'system:camera:view', 'camera', 2, 8, 1, 'admin', UNIX_TIMESTAMP() * 1000)
ON DUPLICATE KEY UPDATE `menu_name` = '摄像头管理', `path` = 'camera', `component` = 'system/camera';

-- Wi-Fi管理 (id=10)
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `path`, `component`, `perms`, `icon`, `menu_type`, `sort`, `status`, `create_by`, `create_time`) 
VALUES (10, 1, 'Wi-Fi管理', 'wifi', 'system/wifi', 'system:wifi:view', 'wifi', 2, 9, 1, 'admin', UNIX_TIMESTAMP() * 1000)
ON DUPLICATE KEY UPDATE `menu_name` = 'Wi-Fi管理', `path` = 'wifi', `component` = 'system/wifi';

-- ----------------------------
-- 2. 角色菜单关联
-- ----------------------------
DELETE FROM sys_role_menu WHERE role_id = 1 AND del_flag = 0;
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`, `create_by`, `create_time`) 
SELECT 1, id, 'admin', UNIX_TIMESTAMP() * 1000 FROM `sys_menu` WHERE `del_flag` = 0 AND id <= 10;

SET FOREIGN_KEY_CHECKS = 1;

SELECT '菜单初始化完成' AS result;
SELECT id, parent_id, menu_name, path, menu_type FROM sys_menu WHERE del_flag = 0 ORDER BY sort;
ON DUPLICATE KEY UPDATE `menu_name` = '底部导航', `path` = 'tabbar', `component` = 'system/tabbar';

-- 摄像头管理 (id=9)
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `path`, `component`, `perms`, `icon`, `menu_type`, `sort`, `status`, `create_by`, `create_time`) 
VALUES (9, 1, '摄像头管理', 'camera', 'system/camera', 'system:camera:view', 'camera', 2, 8, 1, 'admin', NOW())
ON DUPLICATE KEY UPDATE `menu_name` = '摄像头管理', `path` = 'camera', `component` = 'system/camera';

-- Wi-Fi管理 (id=10)
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `path`, `component`, `perms`, `icon`, `menu_type`, `sort`, `status`, `create_by`, `create_time`) 
VALUES (10, 1, 'Wi-Fi管理', 'wifi', 'system/wifi', 'system:wifi:view', 'wifi', 2, 9, 1, 'admin', NOW())
ON DUPLICATE KEY UPDATE `menu_name` = 'Wi-Fi管理', `path` = 'wifi', `component` = 'system/wifi';

-- ----------------------------
-- 2. 插入角色菜单关联数据
-- ----------------------------

-- 超级管理员角色拥有所有菜单权限 (role_id=1 -> menu_ids: 1-10)
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`, `create_by`, `create_time`) 
SELECT 1, id, 'admin', NOW() FROM `sys_menu` WHERE `id` >= 1 AND `id` <= 10 AND `del_flag` = 0
ON DUPLICATE KEY UPDATE `create_by` = 'admin';

SET FOREIGN_KEY_CHECKS = 1;

-- ----------------------------
-- 3. 验证结果
-- ----------------------------

-- 查看所有菜单
SELECT * FROM `sys_menu` WHERE `del_flag` = 0 ORDER BY `sort`;

-- 查看角色菜单关联
SELECT rm.*, r.role_name, m.menu_name 
FROM `sys_role_menu` rm
LEFT JOIN `sys_role` r ON rm.role_id = r.id
LEFT JOIN `sys_menu` m ON rm.menu_id = m.id
WHERE rm.del_flag = 0;