-- ============================================================
-- 客户管理模块 SQL脚本
-- 生成时间: 2026-03-23
-- 数据库: know_boot_v1
-- ============================================================

-- 客户主表
DROP TABLE IF EXISTS `la_customer`;
CREATE TABLE `la_customer` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(50) NOT NULL COMMENT '姓名',
  `gender` tinyint(1) DEFAULT 1 COMMENT '性别 1男2女',
  `age` int DEFAULT NULL COMMENT '年龄',
  `phone` varchar(255) NOT NULL COMMENT '手机号(AES加密)',
  `email` varchar(255) DEFAULT NULL COMMENT '邮箱(AES加密)',
  `address` varchar(255) DEFAULT NULL COMMENT '地址',
  `education` varchar(50) DEFAULT NULL COMMENT '学历',
  `occupation` varchar(50) DEFAULT NULL COMMENT '职业',
  `position` varchar(50) DEFAULT NULL COMMENT '职务',
  `personality` varchar(50) DEFAULT NULL COMMENT '性格',
  `hobby` varchar(255) DEFAULT NULL COMMENT '兴趣爱好',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像URL',
  `customer_type` tinyint(1) DEFAULT 1 COMMENT '客户类型 1个人2企业',
  `company_id` bigint DEFAULT NULL COMMENT '关联企业ID',
  `status` tinyint(1) DEFAULT 1 COMMENT '状态 1潜在2意向3成交4流失',
  `source` varchar(50) DEFAULT NULL COMMENT '来源渠道',
  `demand_level` tinyint(1) DEFAULT 3 COMMENT '需求层级 1-5',
  `value_score` tinyint(1) DEFAULT 3 COMMENT '价值评分 1-5',
  `demand_willingness` int DEFAULT 0 COMMENT '需求意愿 0-100',
  `demand_budget` int DEFAULT 0 COMMENT '预算区间 0-100',
  `demand_decision` int DEFAULT 0 COMMENT '决策权限 0-100',
  `demand_priority` varchar(20) DEFAULT 'medium' COMMENT '优先级 low/medium/high',
  `demand_tags` varchar(500) DEFAULT NULL COMMENT '需求标签 JSON',
  `demand_desc` text DEFAULT NULL COMMENT '需求描述',
  `create_user` int DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  `deleted` tinyint(1) DEFAULT 0 COMMENT '是否删除 0否1是',
  PRIMARY KEY (`id`),
  KEY `idx_status` (`status`),
  KEY `idx_customer_type` (`customer_type`),
  KEY `idx_company_id` (`company_id`),
  KEY `idx_deleted` (`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='客户表';

-- 插入测试数据
INSERT INTO `la_customer` (`name`, `gender`, `age`, `phone`, `email`, `address`, `education`, `occupation`, `position`, `customer_type`, `status`, `source`, `demand_level`, `value_score`, `demand_willingness`, `demand_budget`, `demand_decision`, `demand_priority`, `demand_tags`, `demand_desc`, `create_time`) VALUES
('张三', 1, 28, 'MTM4MDAxMzgwMDA=', 'dGVzdEBleGFtcGxlLmNvbQ==', '广东省深圳市南山区', '本科', '互联网/IT', '产品经理', 1, 2, '线上推广', 4, 4, 80, 60, 90, 'high', '["企业级","CRM系统","数据分析"]', '需要一套CRM系统用于客户管理和销售跟进', NOW()),
('李四', 2, 35, 'MTM5MDAxMzgwMDA=', 'bGku c2lAZXhhbXBsZS5jb20=', '北京市朝阳区', '硕士', '金融', '投资总监', 1, 1, '转介绍', 3, 5, 50, 40, 70, 'medium', '["定制开发","数据分析"]', '希望有一套投资管理系统', NOW()),
('王五', 1, 42, 'MTM3MDAxMzgwMDA=', 'd2FuZ3d1QGV4YW1wbGUuY29t', '上海市浦东新区', '本科', '制造业', '总经理', 2, 3, '地推', 5, 5, 95, 80, 100, 'high', '["企业级","ERP系统","OA办公"]', '全面数字化转型需求', NOW());
