-- ============================================
-- 系统管理数据初始化 - 完整版
-- 用于测试岗位/字典/底部导航等功能
-- ============================================

SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- 1. 检查并创建岗位表 (sys_job)
-- ----------------------------
CREATE TABLE IF NOT EXISTS `sys_job` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `job_name` varchar(100) NOT NULL COMMENT '岗位名称',
  `code` varchar(50) DEFAULT NULL COMMENT '岗位编码',
  `sort` int(11) DEFAULT 0 COMMENT '排序',
  `status` tinyint(1) DEFAULT 1 COMMENT '状态',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
  `update_time` bigint(20) DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(1) DEFAULT 0 COMMENT '删除标志',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='岗位表';

-- ----------------------------
-- 2. 检查并创建字典类型表 (sys_dict_type)
-- ----------------------------
CREATE TABLE IF NOT EXISTS `sys_dict_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `dict_name` varchar(100) NOT NULL COMMENT '字典名称',
  `dict_type` varchar(100) NOT NULL COMMENT '字典类型',
  `status` tinyint(1) DEFAULT 1 COMMENT '状态',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
  `update_time` bigint(20) DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(1) DEFAULT 0 COMMENT '删除标志',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_dict_type` (`dict_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='字典类型表';

-- ----------------------------
-- 3. 检查并创建字典数据表 (sys_dict)
-- ----------------------------
CREATE TABLE IF NOT EXISTS `sys_dict` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `dict_type_id` bigint(20) NOT NULL COMMENT '字典类型ID',
  `dict_label` varchar(100) NOT NULL COMMENT '字典标签',
  `dict_value` varchar(100) NOT NULL COMMENT '字典值',
  `sort` int(11) DEFAULT 0 COMMENT '排序',
  `status` tinyint(1) DEFAULT 1 COMMENT '状态',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
  `update_time` bigint(20) DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(1) DEFAULT 0 COMMENT '删除标志',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='字典数据表';

-- ----------------------------
-- 4. 检查并创建底部导航表 (sys_tabbar)
-- ----------------------------
CREATE TABLE IF NOT EXISTS `sys_tabbar` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `icon` varchar(50) DEFAULT NULL COMMENT '图标',
  `url` varchar(255) DEFAULT NULL COMMENT '链接',
  `sort` int(11) DEFAULT 0 COMMENT '排序',
  `status` tinyint(1) DEFAULT 1 COMMENT '状态',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
  `update_time` bigint(20) DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(1) DEFAULT 0 COMMENT '删除标志',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='底部导航表';

-- ----------------------------
-- 5. 插入岗位数据
-- ----------------------------
DELETE FROM sys_job WHERE del_flag = 0;
INSERT INTO `sys_job` (`id`, `job_name`, `code`, `sort`, `status`, `create_by`, `create_time`) 
VALUES 
(1, '首席执行官', 'CEO', 1, 1, 'system', UNIX_TIMESTAMP() * 1000),
(2, '技术总监', 'CTO', 2, 1, 'system', UNIX_TIMESTAMP() * 1000),
(3, '研发工程师', 'DEV', 3, 1, 'system', UNIX_TIMESTAMP() * 1000),
(4, '测试工程师', 'QA', 4, 1, 'system', UNIX_TIMESTAMP() * 1000),
(5, '产品经理', 'PM', 5, 1, 'system', UNIX_TIMESTAMP() * 1000);

-- ----------------------------
-- 6. 插入字典类型数据
-- ----------------------------
DELETE FROM sys_dict_type WHERE del_flag = 0;
INSERT INTO `sys_dict_type` (`id`, `dict_name`, `dict_type`, `status`, `create_by`, `create_time`) 
VALUES 
(1, '用户状态', 'user_status', 1, 'system', UNIX_TIMESTAMP() * 1000),
(2, '菜单类型', 'menu_type', 1, 'system', UNIX_TIMESTAMP() * 1000),
(3, '系统开关', 'sys_switch', 1, 'system', UNIX_TIMESTAMP() * 1000);

-- ----------------------------
-- 7. 插入字典数据
-- ----------------------------
DELETE FROM sys_dict WHERE del_flag = 0;
INSERT INTO `sys_dict` (`id`, `dict_type_id`, `dict_label`, `dict_value`, `sort`, `status`, `create_by`, `create_time`) 
VALUES 
(1, 1, '正常', '1', 1, 1, 'system', UNIX_TIMESTAMP() * 1000),
(2, 1, '冻结', '2', 2, 1, 'system', UNIX_TIMESTAMP() * 1000),
(3, 2, '目录', '1', 1, 1, 'system', UNIX_TIMESTAMP() * 1000),
(4, 2, '菜单', '2', 2, 1, 'system', UNIX_TIMESTAMP() * 1000),
(5, 2, '按钮', '3', 3, 1, 'system', UNIX_TIMESTAMP() * 1000),
(6, 3, '开启', '1', 1, 1, 'system', UNIX_TIMESTAMP() * 1000),
(7, 3, '关闭', '0', 2, 1, 'system', UNIX_TIMESTAMP() * 1000);

-- ----------------------------
-- 8. 插入底部导航数据
-- ----------------------------
DELETE FROM sys_tabbar WHERE del_flag = 0;
INSERT INTO `sys_tabbar` (`id`, `name`, `icon`, `url`, `sort`, `status`, `create_by`, `create_time`) 
VALUES 
(1, '首页', 'HomeOutlined', '/home', 1, 1, 'system', UNIX_TIMESTAMP() * 1000),
(2, '计划', 'ProjectOutlined', '/plan/list', 2, 1, 'system', UNIX_TIMESTAMP() * 1000),
(3, '我的', 'UserOutlined', '/my', 3, 1, 'system', UNIX_TIMESTAMP() * 1000);

SET FOREIGN_KEY_CHECKS = 1;

-- ----------------------------
-- 验证结果
-- ----------------------------
SELECT '>>> 岗位数据 <<<' AS info; SELECT * FROM sys_job WHERE del_flag = 0;
SELECT '>>> 字典类型 <<<' AS info; SELECT * FROM sys_dict_type WHERE del_flag = 0;
SELECT '>>> 字典数据 <<<' AS info; SELECT * FROM sys_dict WHERE del_flag = 0;
SELECT '>>> 底部导航 <<<' AS info; SELECT * FROM sys_tabbar WHERE del_flag = 0;