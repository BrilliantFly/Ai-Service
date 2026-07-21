-- ============================================================
-- 客户管理模块完整SQL脚本
-- 生成时间: 2026-03-23
-- 数据库: know_boot_v1
-- 说明: 包含客户表、企业表、跟进记录表
-- ============================================================

-- ----------------------------
-- 1. 客户主表
-- ----------------------------
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

-- ----------------------------
-- 2. 企业信息表
-- ----------------------------
DROP TABLE IF EXISTS `la_customer_company`;
CREATE TABLE `la_customer_company` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(100) NOT NULL COMMENT '企业名称',
  `industry` varchar(50) DEFAULT NULL COMMENT '行业分类',
  `scale` varchar(50) DEFAULT NULL COMMENT '企业规模',
  `business` text DEFAULT NULL COMMENT '主营业务',
  `established_date` date DEFAULT NULL COMMENT '成立时间',
  `capital` varchar(50) DEFAULT NULL COMMENT '注册资本',
  `address` varchar(255) DEFAULT NULL COMMENT '企业地址',
  `contact_name` varchar(50) DEFAULT NULL COMMENT '联系人姓名',
  `contact_phone` varchar(255) DEFAULT NULL COMMENT '联系人手机(AES加密)',
  `contact_position` varchar(50) DEFAULT NULL COMMENT '联系人职务',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_industry` (`industry`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='企业信息表';

-- ----------------------------
-- 3. 跟进记录表
-- ----------------------------
DROP TABLE IF EXISTS `la_customer_followup`;
CREATE TABLE `la_customer_followup` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `customer_id` bigint NOT NULL COMMENT '客户ID',
  `type` varchar(20) DEFAULT NULL COMMENT '跟进方式 电话/面谈/微信/邮件',
  `content` text DEFAULT NULL COMMENT '跟进内容',
  `result` varchar(50) DEFAULT NULL COMMENT '跟进结果',
  `next_time` datetime DEFAULT NULL COMMENT '下次跟进时间',
  `create_user` int DEFAULT NULL COMMENT '跟进人ID',
  `create_user_name` varchar(50) DEFAULT NULL COMMENT '跟进人姓名',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '跟进时间',
  PRIMARY KEY (`id`),
  KEY `idx_customer_id` (`customer_id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='跟进记录表';

-- ----------------------------
-- 4. 测试数据
-- ----------------------------

-- 企业测试数据 (需要先插入，因为客户表有关联)
INSERT INTO `la_customer_company` (`name`, `industry`, `scale`, `business`, `established_date`, `capital`, `address`, `contact_name`, `contact_phone`, `contact_position`) VALUES
('XXX科技有限公司', '互联网/IT', '100-500人', '软件开发、技术服务', '2020-01-15', '1000万', '深圳市南山区', '张三', 'MTM4MDAxMzgwMDA=', '产品总监'),
('YYY投资集团', '金融', '500-1000人', '投资管理、资产管理', '2015-06-20', '5亿', '北京市朝阳区', '李四', 'MTM5MDAxMzgwMDA=', '投资总监'),
('ZZZ制造有限公司', '制造业', '1000人以上', '机械设备制造', '2010-03-10', '2亿', '上海市浦东新区', '王五', 'MTM3MDAxMzgwMDA=', '总经理');

-- 客户测试数据
INSERT INTO `la_customer` (`name`, `gender`, `age`, `phone`, `email`, `address`, `education`, `occupation`, `position`, `customer_type`, `company_id`, `status`, `source`, `demand_level`, `value_score`, `demand_willingness`, `demand_budget`, `demand_decision`, `demand_priority`, `demand_tags`, `demand_desc`) VALUES
('张三', 1, 28, 'MTM4MDAxMzgwMDA=', 'dGVzdEBleGFtcGxlLmNvbQ==', '深圳市南山区', '本科', '互联网/IT', '产品经理', 1, 1, 2, '线上推广', 4, 4, 80, 60, 90, 'high', '["企业级","CRM系统"]', '需要一套CRM系统'),
('李四', 2, 35, 'MTM5MDAxMzgwMDA=', 'bGkuc2lAZXhhbXBsZS5jb20=', '北京市朝阳区', '硕士', '金融', '投资总监', 1, 2, 1, '转介绍', 3, 5, 50, 40, 70, 'medium', '["数据分析"]', '投资管理系统需求'),
('王五', 1, 42, 'MTM3MDAxMzgwMDA=', 'd2FuZ3d1QGV4YW1wbGUuY29t', '上海市浦东新区', '本科', '制造业', '总经理', 2, 3, 3, '地推', 5, 5, 95, 80, 100, 'high', '["ERP系统","OA办公"]', '全面数字化转型');

-- 跟进记录测试数据
INSERT INTO `la_customer_followup` (`customer_id`, `type`, `content`, `result`, `next_time`, `create_user`, `create_user_name`) VALUES
(1, '电话', '了解客户需求，使用Excel管理客户，希望系统化管理，预算10-20万', '待反馈', '2026-03-30 14:00:00', 1, '管理员'),
(1, '面谈', '产品演示，客户反馈良好', '考虑中', '2026-04-05 10:00:00', 1, '管理员'),
(2, '电话', '了解客户需求，主要关注数据分析功能', '需要进一步沟通', '2026-03-28 15:00:00', 1, '管理员'),
(3, '面谈', '全面数字化转型方案讨论，客户决策链清晰', '进入商务谈判', '2026-03-25 14:00:00', 1, '管理员');
