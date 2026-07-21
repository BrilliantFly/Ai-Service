-- ============================================================
-- 企业信息表 SQL脚本
-- 生成时间: 2026-03-23
-- 数据库: know_boot_v1
-- ============================================================

-- 企业信息表
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

-- 插入测试数据
INSERT INTO `la_customer_company` (`name`, `industry`, `scale`, `business`, `established_date`, `capital`, `address`, `contact_name`, `contact_phone`, `contact_position`, `create_time`) VALUES
('XXX科技有限公司', '互联网/IT', '100-500人', '软件开发、技术服务、系统集成', '2020-01-15', '1000万', '广东省深圳市南山区科技园', '张三', 'MTM4MDAxMzgwMDA=', '产品总监', NOW()),
('YYY投资集团', '金融', '500-1000人', '投资管理、资产管理、金融咨询', '2015-06-20', '5亿', '北京市朝阳区金融街', '李四', 'MTM5MDAxMzgwMDA=', '投资总监', NOW()),
('ZZZ制造有限公司', '制造业', '1000人以上', '机械设备制造、精密加工', '2010-03-10', '2亿', '上海市浦东新区工业园', '王五', 'MTM3MDAxMzgwMDA=', '总经理', NOW());
