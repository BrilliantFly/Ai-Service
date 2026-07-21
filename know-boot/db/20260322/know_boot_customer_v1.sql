-- Customer Management System Database Schema
-- Run this script in MySQL to create the required tables

CREATE DATABASE IF NOT EXISTS know_boot_v1 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE know_boot_v1;

-- Customer Table
CREATE TABLE IF NOT EXISTS `la_customer` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(50) NOT NULL COMMENT '姓名',
  `gender` tinyint(1) DEFAULT 1 COMMENT '性别',
  `age` int DEFAULT NULL COMMENT '年龄',
  `phone` varchar(255) NOT NULL COMMENT '手机号',
  `email` varchar(255) DEFAULT NULL COMMENT '邮箱',
  `address` varchar(255) DEFAULT NULL COMMENT '地址',
  `education` varchar(50) DEFAULT NULL COMMENT '学历',
  `occupation` varchar(50) DEFAULT NULL COMMENT '职业',
  `position` varchar(50) DEFAULT NULL COMMENT '职位',
  `personality` varchar(50) DEFAULT NULL COMMENT '性格',
  `hobby` varchar(255) DEFAULT NULL COMMENT '兴趣爱好',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像URL',
  `customer_type` tinyint(1) DEFAULT 1 COMMENT '客户类型',
  `company_id` bigint DEFAULT NULL COMMENT '关联企业ID',
  `status` tinyint(1) DEFAULT 1 COMMENT '状态',
  `source` varchar(50) DEFAULT NULL COMMENT '来源渠道',
  `demand_level` tinyint(1) DEFAULT 3 COMMENT '需求层级',
  `value_score` tinyint(1) DEFAULT 3 COMMENT '价值评分',
  `demand_willingness` int DEFAULT 0 COMMENT '需求意愿',
  `demand_budget` int DEFAULT 0 COMMENT '预算金额',
  `demand_decision` int DEFAULT 0 COMMENT '决策权限',
  `demand_priority` varchar(20) DEFAULT 'medium' COMMENT '优先级',
  `demand_tags` varchar(500) DEFAULT NULL COMMENT '需求标签',
  `demand_desc` text DEFAULT NULL COMMENT '需求描述',
  `create_user` int DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` int DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  `deleted` tinyint(1) DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`),
  KEY `idx_status` (`status`),
  KEY `idx_deleted` (`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='客户表';

-- Customer Company Table
CREATE TABLE IF NOT EXISTS `la_customer_company` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(100) NOT NULL COMMENT '企业名称',
  `industry` varchar(50) DEFAULT NULL COMMENT '所属行业',
  `scale` varchar(50) DEFAULT NULL COMMENT '企业规模',
  `business` text DEFAULT NULL COMMENT '经营业务',
  `established_date` date DEFAULT NULL COMMENT '成立时间',
  `capital` varchar(50) DEFAULT NULL COMMENT '注册资本',
  `address` varchar(255) DEFAULT NULL COMMENT '企业地址',
  `contact_name` varchar(50) DEFAULT NULL COMMENT '联系人姓名',
  `contact_phone` varchar(255) DEFAULT NULL COMMENT '联系人手机',
  `contact_position` varchar(50) DEFAULT NULL COMMENT '联系人职位',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='企业信息表';

-- Customer Follow-up Table
CREATE TABLE IF NOT EXISTS `la_customer_followup` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `customer_id` bigint NOT NULL COMMENT '客户ID',
  `type` varchar(20) DEFAULT NULL COMMENT '跟进方式',
  `content` text DEFAULT NULL COMMENT '跟进内容',
  `result` varchar(50) DEFAULT NULL COMMENT '跟进结果',
  `next_time` datetime DEFAULT NULL COMMENT '下次跟进时间',
  `create_user` int DEFAULT NULL COMMENT '创建人ID',
  `create_user_name` varchar(50) DEFAULT NULL COMMENT '创建人姓名',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_customer_id` (`customer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='跟进记录表';
