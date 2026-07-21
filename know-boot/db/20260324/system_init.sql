-- ============================================
-- 系统管理模块初始化数据
-- 日期: 20260324
-- ============================================

SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- 1. 用户数据 (已有 admin)
-- ----------------------------

-- 新增测试用户
INSERT INTO `sys_user` (`id`, `username`, `realname`, `password`, `phone`, `email`, `status`, `create_by`, `create_time`) 
VALUES (2, 'test', '测试用户', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '13800138000', 'test@know.com', 1, 'admin', NOW()),
(3, 'operator', '操作员', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '13800138001', 'operator@know.com', 1, 'admin', NOW())
ON DUPLICATE KEY UPDATE `status` = 1;

-- ----------------------------
-- 2. 角色数据
-- ----------------------------

INSERT INTO `sys_role` (`id`, `role_name`, `role_code`, `sort`, `status`, `create_by`, `create_time`) 
VALUES (1, '超级管理员', 'super_admin', 1, 1, 'admin', NOW()),
(2, '普通用户', 'common_user', 2, 1, 'admin', NOW()),
(3, '访客', 'guest', 3, 1, 'admin', NOW())
ON DUPLICATE KEY UPDATE `role_name` = VALUES(`role_name`), `sort` = VALUES(`sort`), `status` = VALUES(`status`);

-- ----------------------------
-- 3. 用户角色关联
-- ----------------------------

INSERT INTO `sys_user_role` (`user_id`, `role_id`, `create_by`, `create_time`) 
VALUES (1, 1, 'admin', NOW()),  -- admin -> super_admin
(2, 2, 'admin', NOW()),  -- test -> common_user
(3, 3, 'admin', NOW())  -- operator -> guest
ON DUPLICATE KEY UPDATE `create_by` = 'admin';

-- ----------------------------
-- 4. 角色菜单关联 (super_admin 拥有所有菜单)
-- ----------------------------

INSERT INTO `sys_role_menu` (`role_id`, `menu_id`, `create_by`, `create_time`)
SELECT 1, id, 'admin', NOW() FROM `sys_menu` WHERE `del_flag` = 0
ON DUPLICATE KEY UPDATE `create_by` = 'admin';

-- ----------------------------
-- 5. 部门数据
-- ----------------------------

INSERT INTO `sys_dept` (`id`, `parent_id`, `dept_name`, `sort`, `status`, `create_by`, `create_time`) 
VALUES (1, 0, '总公司', 1, 1, 'admin', NOW()),
(2, 1, '研发部', 1, 1, 'admin', NOW()),
(3, 1, '销售部', 2, 1, 'admin', NOW()),
(4, 1, '运营部', 3, 1, 'admin', NOW()),
(5, 2, '前端组', 1, 1, 'admin', NOW()),
(6, 2, '后端组', 2, 1, 'admin', NOW())
ON DUPLICATE KEY UPDATE `dept_name` = VALUES(`dept_name`);

-- ----------------------------
-- 6. 岗位数据
-- ----------------------------

INSERT INTO `sys_job` (`id`, `job_name`, `code`, `sort`, `status`, `create_by`, `create_time`) 
VALUES (1, 'CEO', 'CEO', 1, 1, 'admin', NOW()),
(2, 'CTO', 'CTO', 2, 1, 'admin', NOW()),
(3, '开发工程师', 'developer', 3, 1, 'admin', NOW()),
(4, '测试工程师', 'tester', 4, 1, 'admin', NOW()),
(5, '产品经理', 'pm', 5, 1, 'admin', NOW())
ON DUPLICATE KEY UPDATE `job_name` = VALUES(`job_name`);

-- ----------------------------
-- 7. 字典类型
-- ----------------------------

INSERT INTO `sys_dict_type` (`id`, `dict_name`, `dict_type`, `status`, `create_by`, `create_time`) 
VALUES (1, '用户状态', 'user_status', 1, 'admin', NOW()),
(2, '菜单类型', 'menu_type', 1, 'admin', NOW()),
(3, '系统开关', 'sys_switch', 1, 'admin', NOW())
ON DUPLICATE KEY UPDATE `dict_name` = VALUES(`dict_name`);

-- ----------------------------
-- 8. 字典数据
-- ----------------------------

INSERT INTO `sys_dict` (`id`, `dict_type_id`, `dict_label`, `dict_value`, `sort`, `status`, `create_by`, `create_time`) 
VALUES 
-- 用户状态
(1, 1, '正常', '1', 1, 1, 'admin', NOW()),
(2, 1, '冻结', '2', 2, 1, 'admin', NOW()),
-- 菜单类型
(3, 2, '目录', '1', 1, 1, 'admin', NOW()),
(4, 2, '菜单', '2', 2, 1, 'admin', NOW()),
(5, 2, '按钮', '3', 3, 1, 'admin', NOW()),
-- 系统开关
(6, 3, '开启', '1', 1, 1, 'admin', NOW()),
(7, 3, '关闭', '0', 2, 1, 'admin', NOW())
ON DUPLICATE KEY UPDATE `dict_label` = VALUES(`dict_label`);

-- ----------------------------
-- 9. 底部导航
-- ----------------------------

INSERT INTO `sys_tabbar` (`id`, `name`, `icon`, `url`, `sort`, `status`, `create_by`, `create_time`) 
VALUES (1, '首页', 'HomeOutlined', '/home', 1, 1, 'admin', NOW()),
(2, '计划', 'ProjectOutlined', '/plan/list', 2, 1, 'admin', NOW()),
(3, '我的', 'UserOutlined', '/my', 3, 1, 'admin', NOW())
ON DUPLICATE KEY UPDATE `name` = VALUES(`name`);

SET FOREIGN_KEY_CHECKS = 1;

-- ----------------------------
-- 验证结果
-- ----------------------------

SELECT '=== 用户数据 ===' AS info;
SELECT id, username, realname, status FROM sys_user WHERE del_flag = 0;

SELECT '=== 角色数据 ===' AS info;
SELECT id, role_name, role_code, status FROM sys_role WHERE del_flag = 0;

SELECT '=== 菜单数据 ===' AS info;
SELECT id, parent_id, menu_name, menu_type, status FROM sys_menu WHERE del_flag = 0 ORDER BY sort;

SELECT '=== 角色菜单关联 ===' AS info;
SELECT rm.role_id, rm.menu_id FROM sys_role_menu rm WHERE rm.del_flag = 0;