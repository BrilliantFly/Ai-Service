-- ============================================
-- 租户数据初始化
-- ============================================

-- ----------------------------
-- 租户表
-- ----------------------------
CREATE TABLE IF NOT EXISTS `sys_tenant` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(100) NOT NULL COMMENT '租户名称',
  `code` varchar(50) NOT NULL COMMENT '租户编码',
  `logo` varchar(255) DEFAULT NULL COMMENT 'logo地址',
  `status` tinyint(1) DEFAULT 1 COMMENT '状态 (1:正常, 0:禁用)',
  `expire_time` datetime DEFAULT NULL COMMENT '过期时间',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(1) DEFAULT 0 COMMENT '删除标志',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='租户表';

-- ----------------------------
-- 插入租户数据
-- ----------------------------
INSERT INTO `sys_tenant` (`name`, `code`, `status`, `expire_time`, `create_time`) VALUES
('默认租户', 'DEFAULT', 1, '2030-12-31 23:59:59', NOW()),
('开发租户', 'DEV', 1, '2030-12-31 23:59:59', NOW()),
('测试租户', 'TEST', 1, '2027-12-31 23:59:59', NOW());

-- ----------------------------
-- 验证结果
-- ----------------------------
SELECT '>>> 租户数据 <<<' AS info;
SELECT id, name, code, status, expire_time FROM sys_tenant WHERE del_flag = 0;