-- ============================================
-- 系统管理模块完整初始化数据
-- 适用于验收测试
-- 执行方式: source 或复制粘贴执行
-- ============================================

SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- 1. 角色数据 (sys_role)
-- ----------------------------
INSERT INTO `sys_role` (`id`, `role_name`, `role_code`, `sort`, `status`, `create_by`, `create_time`) 
VALUES (1, '超级管理员', 'super_admin', 1, 1, 'system', UNIX_TIMESTAMP() * 1000),
(2, '普通用户', 'common_user', 2, 1, 'system', UNIX_TIMESTAMP() * 1000),
(3, '访客', 'guest', 3, 1, 'system', UNIX_TIMESTAMP() * 1000)
ON DUPLICATE KEY UPDATE `role_name` = VALUES(`role_name`), `status` = 1;

-- ----------------------------
-- 2. 用户角色关联 (sys_user_role)
-- ----------------------------
INSERT INTO `sys_user_role` (`user_id`, `role_id`, `create_by`, `create_time`) 
VALUES (1, 1, 'system', UNIX_TIMESTAMP() * 1000)
ON DUPLICATE KEY UPDATE `role_id` = 1;

-- ----------------------------
-- 3. 角色菜单关联 (sys_role_menu) - 需要先确保菜单存在
-- ----------------------------
-- 先清理超级管理员的菜单关联
DELETE FROM sys_role_menu WHERE role_id = 1 AND del_flag = 0;
-- 重新插入
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`, `create_by`, `create_time`)
SELECT 1, id, 'system', UNIX_TIMESTAMP() * 1000 FROM `sys_menu` WHERE `del_flag` = 0;

-- ----------------------------
-- 4. 部门数据 (sys_dept)
-- ----------------------------
DELETE FROM sys_dept WHERE del_flag = 0;
INSERT INTO `sys_dept` (`id`, `parent_id`, `dept_name`, `sort`, `status`, `create_by`, `create_time`) 
VALUES (1, 0, '总公司', 1, 1, 'system', UNIX_TIMESTAMP() * 1000),
(2, 1, '研发部', 1, 1, 'system', UNIX_TIMESTAMP() * 1000),
(3, 1, '销售部', 2, 1, 'system', UNIX_TIMESTAMP() * 1000),
(4, 1, '运营部', 3, 1, 'system', UNIX_TIMESTAMP() * 1000),
(5, 2, '前端研发组', 1, 1, 'system', UNIX_TIMESTAMP() * 1000),
(6, 2, '后端研发组', 2, 1, 'system', UNIX_TIMESTAMP() * 1000);

-- ----------------------------
-- 5. 岗位数据 (sys_job)
-- ----------------------------
DELETE FROM sys_job WHERE del_flag = 0;
INSERT INTO `sys_job` (`id`, `job_name`, `code`, `sort`, `status`, `create_by`, `create_time`) 
VALUES (1, '首席执行官', 'CEO', 1, 1, 'system', UNIX_TIMESTAMP() * 1000),
(2, '技术总监', 'CTO', 2, 1, 'system', UNIX_TIMESTAMP() * 1000),
(3, '研发工程师', 'DEV', 3, 1, 'system', UNIX_TIMESTAMP() * 1000),
(4, '测试工程师', 'QA', 4, 1, 'system', UNIX_TIMESTAMP() * 1000),
(5, '产品经理', 'PM', 5, 1, 'system', UNIX_TIMESTAMP() * 1000);

-- ----------------------------
-- 6. 字典类型 (sys_dict_type)
-- ----------------------------
DELETE FROM sys_dict_type WHERE del_flag = 0;
INSERT INTO `sys_dict_type` (`id`, `dict_name`, `dict_type`, `status`, `create_by`, `create_time`) 
VALUES (1, '用户状态', 'user_status', 1, 'system', UNIX_TIMESTAMP() * 1000),
(2, '菜单类型', 'menu_type', 1, 'system', UNIX_TIMESTAMP() * 1000),
(3, '系统开关', 'sys_switch', 1, 'system', UNIX_TIMESTAMP() * 1000);

-- ----------------------------
-- 7. 字典数据 (sys_dict)
-- ----------------------------
DELETE FROM sys_dict WHERE del_flag = 0;
INSERT INTO `sys_dict` (`id`, `dict_type_id`, `dict_label`, `dict_value`, `sort`, `status`, `create_by`, `create_time`) 
VALUES (1, 1, '正常', '1', 1, 1, 'system', UNIX_TIMESTAMP() * 1000),
(2, 1, '冻结', '2', 2, 1, 'system', UNIX_TIMESTAMP() * 1000),
(3, 2, '目录', '1', 1, 1, 'system', UNIX_TIMESTAMP() * 1000),
(4, 2, '菜单', '2', 2, 1, 'system', UNIX_TIMESTAMP() * 1000),
(5, 2, '按钮', '3', 3, 1, 'system', UNIX_TIMESTAMP() * 1000),
(6, 3, '开启', '1', 1, 1, 'system', UNIX_TIMESTAMP() * 1000),
(7, 3, '关闭', '0', 2, 1, 'system', UNIX_TIMESTAMP() * 1000);

-- ----------------------------
-- 8. 底部导航 (sys_tabbar)
-- ----------------------------
DELETE FROM sys_tabbar WHERE del_flag = 0;
INSERT INTO `sys_tabbar` (`id`, `name`, `icon`, `url`, `sort`, `status`, `create_by`, `create_time`) 
VALUES (1, '首页', 'HomeOutlined', '/home', 1, 1, 'system', UNIX_TIMESTAMP() * 1000),
(2, '计划', 'ProjectOutlined', '/plan/list', 2, 1, 'system', UNIX_TIMESTAMP() * 1000),
(3, '我的', 'UserOutlined', '/my', 3, 1, 'system', UNIX_TIMESTAMP() * 1000);

SET FOREIGN_KEY_CHECKS = 1;

-- ----------------------------
-- 验证查询
-- ----------------------------
SELECT '>>> 角色数量:' AS info; SELECT COUNT(*) AS cnt FROM sys_role WHERE del_flag = 0;
SELECT '>>> 用户角色:' AS info; SELECT * FROM sys_user_role WHERE del_flag = 0;
SELECT '>>> 菜单数量:' AS info; SELECT COUNT(*) AS cnt FROM sys_menu WHERE del_flag = 0;
SELECT '>>> 角色菜单:' AS info; SELECT COUNT(*) AS cnt FROM sys_role_menu WHERE del_flag = 0;
SELECT '>>> 部门:' AS info; SELECT * FROM sys_dept WHERE del_flag = 0;
SELECT '>>> 岗位:' AS info; SELECT * FROM sys_job WHERE del_flag = 0;
SELECT '>>> 字典类型:' AS info; SELECT * FROM sys_dict_type WHERE del_flag = 0;
SELECT '>>> 字典数据:' AS info; SELECT * FROM sys_dict WHERE del_flag = 0;
SELECT '>>> 底部导航:' AS info; SELECT * FROM sys_tabbar WHERE del_flag = 0;