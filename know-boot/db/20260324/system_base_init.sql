-- ============================================
-- 系统管理基础数据初始化
-- 只初始化核心表：用户、角色、角色菜单关联
-- ============================================

SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- 1. 确保 admin 用户存在
-- ----------------------------
-- admin 已存在，跳过

-- ----------------------------
-- 2. 角色数据
-- ----------------------------
INSERT INTO `sys_role` (`id`, `role_name`, `role_code`, `sort`, `status`, `create_by`, `create_time`) 
VALUES (1, '超级管理员', 'super_admin', 1, 1, 'admin', NOW())
ON DUPLICATE KEY UPDATE `role_name` = '超级管理员', `status` = 1;

-- ----------------------------
-- 3. 用户角色关联
-- ----------------------------
INSERT INTO `sys_user_role` (`user_id`, `role_id`, `create_by`, `create_time`) 
VALUES (1, 1, 'admin', NOW())
ON DUPLICATE KEY UPDATE `role_id` = 1;

-- ----------------------------
-- 4. 角色菜单关联 (super_admin 拥有所有菜单)
-- ----------------------------
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`, `create_by`, `create_time`)
SELECT 1, id, 'admin', NOW() FROM `sys_menu` WHERE `del_flag` = 0
ON DUPLICATE KEY UPDATE `create_by` = 'admin';

SET FOREIGN_KEY_CHECKS = 1;

-- ----------------------------
-- 验证
-- ----------------------------
SELECT '角色数据' AS info;
SELECT * FROM sys_role WHERE del_flag = 0;

SELECT '角色菜单关联' AS info;
SELECT COUNT(*) AS count FROM sys_role_menu WHERE del_flag = 0 AND role_id = 1;