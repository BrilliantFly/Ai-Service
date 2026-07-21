-- ============================================
-- Admin用户表初始化脚本
-- 日期: 20260322
-- 默认账号: admin
-- 默认密码: admin123 (MD5: 0192023a7bbd73250516f069df18b500)
-- ============================================

-- ----------------------------
-- 创建admin表 (使用la_前缀以匹配现有代码)
-- ----------------------------
DROP TABLE IF EXISTS `la_admin`;

CREATE TABLE `la_admin` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `root` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否超级管理员 0-否 1-是',
  `name` varchar(50) NOT NULL COMMENT '用户昵称',
  `avatar` varchar(255) DEFAULT NULL COMMENT '用户头像',
  `account` varchar(50) NOT NULL COMMENT '用户账号',
  `password` varchar(100) NOT NULL COMMENT '用户密码(MD5)',
  `multipoint_login` tinyint(1) NOT NULL DEFAULT 1 COMMENT '多端登录: 0=否 1=是',
  `disable` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否禁用: 0=否 1=是',
  `login_ip` varchar(50) DEFAULT NULL COMMENT '最后登录IP',
  `login_time` bigint(20) DEFAULT NULL COMMENT '最后登录时间',
  `create_time` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `update_time` bigint(20) DEFAULT NULL COMMENT '更新时间',
  `delete_time` bigint(20) DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_admin_account` (`account`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统管理员表';

-- ----------------------------
-- 插入默认管理员账号
-- 密码: admin123 (MD5: 0192023a7bbd73250516f069df18b500)
-- ----------------------------
INSERT INTO `la_admin` (`id`, `root`, `name`, `avatar`, `account`, `password`, `multipoint_login`, `disable`, `create_time`) 
VALUES (1, 1, '系统管理员', NULL, 'admin', '0192023a7bbd73250516f069df18b500', 1, 0, UNIX_TIMESTAMP());

-- ----------------------------
-- 验证查询
-- ----------------------------
SELECT * FROM la_admin;
