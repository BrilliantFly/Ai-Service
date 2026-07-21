-- ============================================
-- 权限系统扩展脚本 - 部门管理与日志表
-- 日期: 20260330
-- ============================================

SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- 1. 删除旧表 (如果存在)
-- ----------------------------
DROP TABLE IF EXISTS `sys_log_operation`;
DROP TABLE IF EXISTS `sys_log_login`;
DROP TABLE IF EXISTS `sys_role_data_scope`;
DROP TABLE IF EXISTS `sys_dept_user`;
DROP TABLE IF EXISTS `sys_dept`;

-- ----------------------------
-- 2. 创建表结构
-- ----------------------------

-- 2.1 部门表
CREATE TABLE `sys_dept` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `parent_id` bigint(20) DEFAULT 0 COMMENT '父部门ID (0:顶级)',
  `dept_name` varchar(64) NOT NULL COMMENT '部门名称',
  `dept_code` varchar(64) DEFAULT NULL COMMENT '部门编码',
  `sort` int(11) DEFAULT 0 COMMENT '排序',
  `leader` varchar(64) DEFAULT NULL COMMENT '负责人',
  `phone` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `email` varchar(64) DEFAULT NULL COMMENT '邮箱',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态 (1:正常, 0:停用)',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除标志 (0:正常, 1:已删除)',
  PRIMARY KEY (`id`),
  KEY `idx_sys_dept_parent_id` (`parent_id`),
  KEY `idx_sys_dept_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='部门表';

-- 2.2 部门用户关联表
CREATE TABLE `sys_dept_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `dept_id` bigint(20) NOT NULL COMMENT '部门ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除标志 (0:正常, 1:已删除)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_dept_user` (`dept_id`, `user_id`),
  KEY `idx_sys_dept_user_dept_id` (`dept_id`),
  KEY `idx_sys_dept_user_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='部门用户关联表';

-- 2.3 角色数据权限配置表
CREATE TABLE `sys_role_data_scope` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `dept_id` bigint(20) DEFAULT NULL COMMENT '自定义部门ID',
  `data_scope_type` tinyint(1) NOT NULL COMMENT '数据范围类型 (1:全部, 2:本部门, 3:本部门及子部门, 4:仅本人, 5:自定义)',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_sys_role_data_scope_role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色数据权限配置表';

-- 2.4 登录日志表
CREATE TABLE `sys_log_login` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `username` varchar(64) DEFAULT NULL COMMENT '用户名',
  `ip` varchar(50) DEFAULT NULL COMMENT '登录IP地址',
  `address` varchar(255) DEFAULT NULL COMMENT '登录地址',
  `device` varchar(64) DEFAULT NULL COMMENT '设备类型',
  `browser` varchar(64) DEFAULT NULL COMMENT '浏览器类型',
  `os` varchar(64) DEFAULT NULL COMMENT '操作系统',
  `login_type` tinyint(1) DEFAULT 1 COMMENT '登录方式 (1:账号密码, 2:短信, 3:微信, 4:QQ, 5:其他)',
  `status` tinyint(1) DEFAULT 1 COMMENT '登录状态 (0:失败, 1:成功)',
  `msg` varchar(255) DEFAULT NULL COMMENT '提示消息',
  `login_time` datetime DEFAULT NULL COMMENT '登录时间',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_sys_log_login_user_id` (`user_id`),
  KEY `idx_sys_log_login_login_time` (`login_time`),
  KEY `idx_sys_log_login_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='登录日志表';

-- 2.5 操作日志表
CREATE TABLE `sys_log_operation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `username` varchar(64) DEFAULT NULL COMMENT '用户名',
  `module` varchar(64) DEFAULT NULL COMMENT '操作模块',
  `business_type` tinyint(1) DEFAULT 0 COMMENT '业务类型 (0:其它, 1:新增, 2:修改, 3:删除, 4:查询, 5:导出, 6:导入)',
  `method` varchar(100) DEFAULT NULL COMMENT '请求方法',
  `request_method` varchar(10) DEFAULT NULL COMMENT '请求方式 (GET/POST/PUT/DELETE)',
  `operator_type` tinyint(1) DEFAULT 1 COMMENT '操作类型 (1:PC网页, 2:手机网页, 3:APP, 4:小程序, 5:其他)',
  `request_url` varchar(255) DEFAULT NULL COMMENT '请求URL',
  `request_param` text COMMENT '请求参数',
  `response_result` text COMMENT '响应结果',
  `status` tinyint(1) DEFAULT 1 COMMENT '操作状态 (0:失败, 1:成功)',
  `error_msg` text COMMENT '错误消息',
  `operate_time` datetime DEFAULT NULL COMMENT '操作时间',
  `cost_time` int(11) DEFAULT 0 COMMENT '耗时(毫秒)',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_sys_log_operation_user_id` (`user_id`),
  KEY `idx_sys_log_operation_operate_time` (`operate_time`),
  KEY `idx_sys_log_operation_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志表';

SET FOREIGN_KEY_CHECKS = 1;
