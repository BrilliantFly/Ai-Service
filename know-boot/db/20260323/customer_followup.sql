-- ============================================================
-- 跟进记录表 SQL脚本
-- 生成时间: 2026-03-23
-- 数据库: know_boot_v1
-- ============================================================

-- 跟进记录表
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

-- 插入测试数据
INSERT INTO `la_customer_followup` (`customer_id`, `type`, `content`, `result`, `next_time`, `create_user`, `create_user_name`, `create_time`) VALUES
(1, '电话', '电话沟通，了解客户需求：\n1. 目前客户使用Excel管理客户\n2. 希望有系统化管理\n3. 预算10-20万\n4. 决策周期1个月', '待反馈', '2026-03-30 14:00:00', 1, '管理员', '2026-03-22 14:30:00'),
(1, '面谈', '产品演示，客户反馈良好，对CRM系统功能表示认可，提出定制化需求', '考虑中', '2026-04-05 10:00:00', 1, '管理员', '2026-03-18 10:00:00'),
(1, '微信', '初次接触，发送产品资料和报价方案，客户表示有兴趣', '已收到', '2026-03-25 09:00:00', 1, '管理员', '2026-03-15 16:00:00'),
(2, '电话', '了解客户需求，主要关注数据分析功能', '需要进一步沟通', '2026-03-28 15:00:00', 1, '管理员', '2026-03-21 15:00:00'),
(3, '面谈', '全面数字化转型方案讨论，客户决策链清晰，预算充足', '进入商务谈判', '2026-03-25 14:00:00', 1, '管理员', '2026-03-20 14:00:00');
