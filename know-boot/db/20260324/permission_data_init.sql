-- ============================================
-- 权限管理表结构初始化
-- ============================================

SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- 1. 权限表
-- ----------------------------
CREATE TABLE IF NOT EXISTS `sys_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `parent_id` bigint(20) DEFAULT 0 COMMENT '父权限ID',
  `name` varchar(100) NOT NULL COMMENT '权限名称',
  `code` varchar(100) NOT NULL COMMENT '权限编码(user:add)',
  `permission_type` varchar(20) DEFAULT 'button' COMMENT '权限类型(button/api/data)',
  `resource_type` varchar(20) DEFAULT 'button' COMMENT '资源类型(button/menu/api)',
  `path` varchar(255) DEFAULT NULL COMMENT '路由/接口路径',
  `component` varchar(255) DEFAULT NULL COMMENT '前端组件路径',
  `icon` varchar(50) DEFAULT NULL COMMENT '图标',
  `sort` int(11) DEFAULT 0 COMMENT '排序',
  `status` tinyint(1) DEFAULT 1 COMMENT '状态(0禁用/1启用)',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(1) DEFAULT 0 COMMENT '删除标志',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='权限表';

-- ----------------------------
-- 2. 角色-权限关联表
-- ----------------------------
CREATE TABLE IF NOT EXISTS `sys_role_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `permission_id` bigint(20) NOT NULL COMMENT '权限ID',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_role_id` (`role_id`),
  KEY `idx_permission_id` (`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色-权限关联表';

-- ----------------------------
-- 3. 菜单-权限关联表
-- ----------------------------
CREATE TABLE IF NOT EXISTS `sys_menu_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `menu_id` bigint(20) NOT NULL COMMENT '菜单ID',
  `permission_id` bigint(20) NOT NULL COMMENT '权限ID',
  PRIMARY KEY (`id`),
  KEY `idx_menu_id` (`menu_id`),
  KEY `idx_permission_id` (`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜单-权限关联表';

-- ----------------------------
-- 4. 数据权限配置表
-- ----------------------------
CREATE TABLE IF NOT EXISTS `sys_data_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `dept_ids` varchar(500) DEFAULT NULL COMMENT '可见部门ID列表',
  `data_scope` tinyint(1) DEFAULT 1 COMMENT '数据范围(1全部/2本部门/3本部门及子部门/4仅本人)',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='数据权限配置表';

-- ----------------------------
-- 5. 操作日志表
-- ----------------------------
CREATE TABLE IF NOT EXISTS `sys_oper_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `title` varchar(100) DEFAULT '' COMMENT '操作模块',
  `business_type` tinyint(4) DEFAULT 0 COMMENT '业务类型',
  `method` varchar(100) DEFAULT '' COMMENT '方法名',
  `request_method` varchar(10) DEFAULT '' COMMENT '请求方式',
  `operator_type` tinyint(4) DEFAULT 0 COMMENT '操作类型',
  `oper_name` varchar(50) DEFAULT '' COMMENT '操作人',
  `oper_ip` varchar(50) DEFAULT '' COMMENT '操作IP',
  `oper_location` varchar(255) DEFAULT '' COMMENT '操作位置',
  `oper_url` varchar(255) DEFAULT '' COMMENT '请求URL',
  `oper_param` text COMMENT '请求参数',
  `json_result` text COMMENT '返回结果',
  `status` tinyint(4) DEFAULT 0 COMMENT '状态(0正常/1异常)',
  `error_msg` text COMMENT '错误信息',
  `oper_time` datetime DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志表';

-- ----------------------------
-- 6. 登录日志表
-- ----------------------------
CREATE TABLE IF NOT EXISTS `sys_login_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(50) DEFAULT '' COMMENT '用户名',
  `ipaddr` varchar(50) DEFAULT '' COMMENT '登录IP',
  `login_location` varchar(255) DEFAULT '' COMMENT '登录位置',
  `browser` varchar(50) DEFAULT '' COMMENT '浏览器',
  `os` varchar(50) DEFAULT '' COMMENT '操作系统',
  `status` tinyint(4) DEFAULT 0 COMMENT '状态(0失败/1成功)',
  `msg` varchar(255) DEFAULT '' COMMENT '提示消息',
  `login_time` datetime DEFAULT NULL COMMENT '登录时间',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统访问记录表';

SET FOREIGN_KEY_CHECKS = 1;

-- ----------------------------
-- 初始化权限数据
-- ----------------------------
INSERT INTO `sys_permission` (`id`, `parent_id`, `name`, `code`, `permission_type`, `resource_type`, `sort`, `status`, `create_by`, `create_time`) VALUES
-- 用户管理权限
(1, 0, '用户管理', 'system:user', 'menu', 'menu', 1, 1, 'system', NOW()),
(101, 1, '用户列表', 'system:user:list', 'button', 'button', 1, 1, 'system', NOW()),
(102, 1, '新增用户', 'system:user:add', 'button', 'button', 2, 1, 'system', NOW()),
(103, 1, '编辑用户', 'system:user:edit', 'button', 'button', 3, 1, 'system', NOW()),
(104, 1, '删除用户', 'system:user:delete', 'button', 'button', 4, 1, 'system', NOW()),
(105, 1, '重置密码', 'system:user:resetPwd', 'button', 'button', 5, 1, 'system', NOW()),
-- 角色管理权限
(2, 0, '角色管理', 'system:role', 'menu', 'menu', 2, 1, 'system', NOW()),
(201, 2, '角色列表', 'system:role:list', 'button', 'button', 1, 1, 'system', NOW()),
(202, 2, '新增角色', 'system:role:add', 'button', 'button', 2, 1, 'system', NOW()),
(203, 2, '编辑角色', 'system:role:edit', 'button', 'button', 3, 1, 'system', NOW()),
(204, 2, '删除角色', 'system:role:delete', 'button', 'button', 4, 1, 'system', NOW()),
(205, 2, '分配权限', 'system:role:permission', 'button', 'button', 5, 1, 'system', NOW()),
-- 菜单管理权限
(3, 0, '菜单管理', 'system:menu', 'menu', 'menu', 3, 1, 'system', NOW()),
(301, 3, '菜单列表', 'system:menu:list', 'button', 'button', 1, 1, 'system', NOW()),
(302, 3, '新增菜单', 'system:menu:add', 'button', 'button', 2, 1, 'system', NOW()),
(303, 3, '编辑菜单', 'system:menu:edit', 'button', 'button', 3, 1, 'system', NOW()),
(304, 3, '删除菜单', 'system:menu:delete', 'button', 'button', 4, 1, 'system', NOW()),
-- 部门管理权限
(4, 0, '部门管理', 'system:dept', 'menu', 'menu', 4, 1, 'system', NOW()),
(401, 4, '部门列表', 'system:dept:list', 'button', 'button', 1, 1, 'system', NOW()),
(402, 4, '新增部门', 'system:dept:add', 'button', 'button', 2, 1, 'system', NOW()),
(403, 4, '编辑部门', 'system:dept:edit', 'button', 'button', 3, 1, 'system', NOW()),
(404, 4, '删除部门', 'system:dept:delete', 'button', 'button', 4, 1, 'system', NOW()),
-- 租户管理权限
(5, 0, '租户管理', 'system:tenant', 'menu', 'menu', 5, 1, 'system', NOW()),
(501, 5, '租户列表', 'system:tenant:list', 'button', 'button', 1, 1, 'system', NOW()),
(502, 5, '新增租户', 'system:tenant:add', 'button', 'button', 2, 1, 'system', NOW()),
(503, 5, '编辑租户', 'system:tenant:edit', 'button', 'button', 3, 1, 'system', NOW()),
(504, 5, '删除租户', 'system:tenant:delete', 'button', 'button', 4, 1, 'system', NOW()),
-- 字典管理权限
(6, 0, '字典管理', 'system:dict', 'menu', 'menu', 6, 1, 'system', NOW()),
(601, 6, '字典列表', 'system:dict:list', 'button', 'button', 1, 1, 'system', NOW()),
(602, 6, '新增字典', 'system:dict:add', 'button', 'button', 2, 1, 'system', NOW()),
(603, 6, '编辑字典', 'system:dict:edit', 'button', 'button', 3, 1, 'system', NOW()),
(604, 6, '删除字典', 'system:dict:delete', 'button', 'button', 4, 1, 'system', NOW()),
-- 日志管理权限
(7, 0, '日志管理', 'system:log', 'menu', 'menu', 7, 1, 'system', NOW()),
(701, 7, '登录日志', 'system:log:login', 'button', 'button', 1, 1, 'system', NOW()),
(702, 7, '操作日志', 'system:log:oper', 'button', 'button', 2, 1, 'system', NOW()),
(703, 7, '删除日志', 'system:log:delete', 'button', 'button', 3, 1, 'system', NOW());

-- ----------------------------
-- 验证结果
-- ----------------------------
SELECT '>>> 权限数据初始化完成 <<<' AS info;
SELECT id, parent_id, name, code, permission_type FROM sys_permission WHERE del_flag = 0 OR del_flag IS NULL ORDER BY sort;