-- ============================================
-- 多租户权限管理系统初始化脚本
-- 日期: 20260317
-- ============================================

SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- 1. 删除旧表 (如果存在)
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
DROP TABLE IF EXISTS `sys_role_menu`;
DROP TABLE IF EXISTS `sys_menu`;
DROP TABLE IF EXISTS `sys_role`;
DROP TABLE IF EXISTS `sys_user_tenant`;
DROP TABLE IF EXISTS `sys_user`;
DROP TABLE IF EXISTS `sys_tenant`;

-- ----------------------------
-- 2. 创建表结构
-- ----------------------------

-- 2.1 租户信息表
CREATE TABLE `sys_tenant` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(100) NOT NULL COMMENT '租户名称',
  `code` varchar(50) NOT NULL COMMENT '租户编码',
  `logo` varchar(255) DEFAULT NULL COMMENT 'Logo',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态 (0:停用, 1:正常)',
  `expire_time` datetime DEFAULT NULL COMMENT '过期时间',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除标志 (0:正常, 1:已删除)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_sys_tenant_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='租户信息表';

-- 2.2 用户表
CREATE TABLE `sys_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(64) NOT NULL COMMENT '用户名',
  `realname` varchar(64) NOT NULL COMMENT '真实姓名',
  `password` varchar(255) NOT NULL COMMENT '密码 (BCrypt加密)',
  `salt` varchar(45) DEFAULT NULL COMMENT '密码盐 (BCrypt无需此字段)',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `email` varchar(64) DEFAULT NULL COMMENT '邮箱',
  `sex` tinyint(1) DEFAULT 0 COMMENT '性别 (0:未知, 1:男, 2:女)',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态 (1:正常, 2:冻结)',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '当前登录租户ID',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除标志',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_sys_user_username` (`username`),
  UNIQUE KEY `uk_sys_user_phone` (`phone`),
  UNIQUE KEY `uk_sys_user_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统用户表';

-- 2.3 用户租户关联表
CREATE TABLE `sys_user_tenant` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户ID',
  `is_admin` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否租户管理员 (0:否, 1:是)',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态 (1:正常)',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除标志',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_tenant` (`user_id`, `tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户租户关联表';

-- 2.4 角色表
CREATE TABLE `sys_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_name` varchar(64) NOT NULL COMMENT '角色名称',
  `role_code` varchar(64) NOT NULL COMMENT '角色编码',
  `sort` int(11) DEFAULT 0 COMMENT '排序',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态 (1:正常, 0:停用)',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID (空为系统级)',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除标志',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_sys_role_role_code` (`role_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- 2.5 菜单/权限表
CREATE TABLE `sys_menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `parent_id` bigint(20) DEFAULT 0 COMMENT '父菜单ID (0:根)',
  `menu_name` varchar(64) NOT NULL COMMENT '菜单名称',
  `path` varchar(255) DEFAULT NULL COMMENT '路由地址',
  `component` varchar(255) DEFAULT NULL COMMENT '组件路径',
  `perms` varchar(100) DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(64) DEFAULT NULL COMMENT '菜单图标',
  `menu_type` tinyint(1) NOT NULL DEFAULT 1 COMMENT '类型 (1:目录, 2:菜单, 3:按钮)',
  `sort` int(11) DEFAULT 0 COMMENT '排序',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态 (1:显示, 0:隐藏)',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID (空为系统级)',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除标志',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜单权限表';

-- 2.6 角色菜单关联表
CREATE TABLE `sys_role_menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `menu_id` bigint(20) NOT NULL COMMENT '菜单ID',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除标志',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_menu` (`role_id`, `menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色菜单关联表';

-- 2.7 用户角色关联表
CREATE TABLE `sys_user_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除标志',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_role` (`user_id`, `role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';

-- ----------------------------
-- 3. 插入基础数据
-- ----------------------------

-- 3.1 默认租户 (系统级)
INSERT INTO `sys_tenant` (`id`, `name`, `code`, `status`, `create_by`, `create_time`) 
VALUES (1, '系统租户', 'system', 1, 'admin', NOW());

-- 3.2 超级管理员角色 (系统级)
INSERT INTO `sys_role` (`id`, `role_name`, `role_code`, `status`, `create_by`, `create_time`) 
VALUES (1, '超级管理员', 'super_admin', 1, 'admin', NOW());

-- 3.3 管理员用户 (默认密码: admin123, BCrypt加密)
-- BCrypt Hash: $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy
INSERT INTO `sys_user` (`id`, `username`, `realname`, `password`, `status`, `create_by`, `create_time`) 
VALUES (1, 'admin', '系统管理员', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 1, 'admin', NOW());

-- 3.4 用户角色关联 (admin -> super_admin)
INSERT INTO `sys_user_role` (`user_id`, `role_id`, `create_by`, `create_time`) 
VALUES (1, 1, 'admin', NOW());

-- 3.5 用户租户关联 (admin -> system tenant)
INSERT INTO `sys_user_tenant` (`user_id`, `tenant_id`, `is_admin`, `status`, `create_by`, `create_time`) 
VALUES (1, 1, 1, 1, 'admin', NOW());

SET FOREIGN_KEY_CHECKS = 1;