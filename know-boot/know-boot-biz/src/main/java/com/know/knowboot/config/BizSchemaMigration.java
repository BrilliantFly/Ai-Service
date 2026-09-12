package com.know.knowboot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 业务(biz)模块表结构迁移
 * <p>
 * 在应用启动时自动检查并创建业务相关的数据库表（客户信息、行业调研），
 * 以及初始化行业主数据、示例客户等基础数据。
 * <p>
 * 使用 @PostConstruct + JdbcTemplate 模式（与 PlanSchemaMigration 一致），
 * 不依赖 Flyway，避免与现有数据库状态冲突。全部表统一 biz_ 前缀。
 */
@Component
public class BizSchemaMigration {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void migrateSchema() {
        System.out.println("[BizSchemaMigrate] 检查业务(biz)模块表结构...");
        try {
            createBizCustomer();
            createBizCustomerCompany();
            createBizCustomerProfile();
            createBizCustomerFollowup();
            createBizCustomerIndustry();

            createBizIndustry();
            createBizIndustryProduct();
            createBizIndustryEnterprise();
            createBizIndustryMarket();

            createBizIndustryEnterpriseRel();
            createBizIndustryProductRel();
            createBizEnterpriseProductRel();
            createBizIndustryMarketRel();
            migrateIndustryRelData();

            seedIndustry();
            seedCustomer();
            seedCustomerIndustry();
            seedCustomerProfile();

            System.out.println("[BizSchemaMigrate] 业务(biz)模块表结构检查完成");
        } catch (Exception e) {
            System.err.println("[BizSchemaMigrate] 迁移失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ============================
    // 1. 客户主表
    // ============================
    private void createBizCustomer() {
        if (!tableExists("biz_customer")) {
            jdbcTemplate.execute(
                "CREATE TABLE `biz_customer` (" +
                "  `id` bigint NOT NULL COMMENT '主键'," +
                "  `name` varchar(64) NOT NULL COMMENT '客户姓名'," +
                "  `gender` tinyint DEFAULT NULL COMMENT '性别(0:未知 1:男 2:女)'," +
                "  `age` int DEFAULT NULL COMMENT '年龄'," +
                "  `phone` varchar(128) DEFAULT NULL COMMENT '手机号(AES加密)'," +
                "  `email` varchar(128) DEFAULT NULL COMMENT '邮箱(AES加密)'," +
                "  `address` varchar(255) DEFAULT NULL COMMENT '地址'," +
                "  `region_code` varchar(64) DEFAULT NULL COMMENT '区域编码(行政区划，用于按区域统计)'," +
                "  `education` varchar(64) DEFAULT NULL COMMENT '学历(小学/初中/高中/大专/本科/硕士/博士)'," +
                "  `education_raw` varchar(255) DEFAULT NULL COMMENT '教育背景详情(学校/专业，用于认知层度分析)'," +
                "  `occupation` varchar(64) DEFAULT NULL COMMENT '职业'," +
                "  `position` varchar(64) DEFAULT NULL COMMENT '职务/职位'," +
                "  `personality` varchar(255) DEFAULT NULL COMMENT '性格(外向/内向/理性/感性等)'," +
                "  `hobby` varchar(255) DEFAULT NULL COMMENT '兴趣爱好'," +
                "  `values_text` varchar(512) DEFAULT NULL COMMENT '价值观(核心信念/关注点)'," +
                "  `lifestyle` varchar(512) DEFAULT NULL COMMENT '衣食住行(消费习惯/生活品质信号)'," +
                "  `marital_status` varchar(16) DEFAULT NULL COMMENT '婚姻状况(未婚/已婚/离异/保密)'," +
                "  `family_situation` varchar(512) DEFAULT NULL COMMENT '家庭情况(成员构成/子女情况等)'," +
                "  `customer_type` tinyint DEFAULT 1 COMMENT '客户类型(1:个人 2:企业)'," +
                "  `company_id` bigint DEFAULT NULL COMMENT '所属企业ID(biz_customer_company.id)'," +
                "  `status` tinyint DEFAULT 1 COMMENT '状态(1:潜在 2:意向 3:成交 4:流失)'," +
                "  `source` varchar(64) DEFAULT NULL COMMENT '来源(线上推广/转介绍/展会/陌拜)'," +
                "  `demand_level` int DEFAULT NULL COMMENT '需求层级(1-5)'," +
                "  `value_score` int DEFAULT NULL COMMENT '价值评分(1-5)'," +
                "  `demand_willingness` tinyint DEFAULT NULL COMMENT '需求意愿(0-100)'," +
                "  `demand_budget` decimal(12,2) DEFAULT NULL COMMENT '需求预算'," +
                "  `demand_decision` varchar(64) DEFAULT NULL COMMENT '决策角色(使用者/把关者/决策者)'," +
                "  `demand_priority` tinyint DEFAULT NULL COMMENT '需求优先级(1-5)'," +
                "  `demand_tags` varchar(255) DEFAULT NULL COMMENT '需求标签(JSON数组)'," +
                "  `demand_desc` varchar(512) DEFAULT NULL COMMENT '需求描述'," +
                "  `create_by` bigint DEFAULT NULL COMMENT '创建人'," +
                "  `create_time` bigint DEFAULT NULL COMMENT '创建时间'," +
                "  `update_by` bigint DEFAULT NULL COMMENT '更新人'," +
                "  `update_time` bigint DEFAULT NULL COMMENT '更新时间'," +
                "  `deleted` tinyint DEFAULT 0 COMMENT '删除标记(0:正常 1:删除)'," +
                "  `delete_time` bigint DEFAULT 0 COMMENT '删除时间(毫秒时间戳)'," +
                "  PRIMARY KEY (`id`)," +
                "  KEY `idx_customer_name` (`name`)," +
                "  KEY `idx_customer_status` (`status`, `deleted`)," +
                "  KEY `idx_customer_type` (`customer_type`, `deleted`)," +
                "  KEY `idx_customer_company` (`company_id`)," +
                "  KEY `idx_customer_region` (`region_code`, `deleted`)," +
                "  KEY `idx_customer_phone` (`phone`)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客户主表'"
            );
            System.out.println("[BizSchemaMigrate] 已创建 biz_customer 表");
        }
    }

    // ============================
    // 2. 客户所属企业表
    // ============================
    private void createBizCustomerCompany() {
        if (!tableExists("biz_customer_company")) {
            jdbcTemplate.execute(
                "CREATE TABLE `biz_customer_company` (" +
                "  `id` bigint NOT NULL COMMENT '主键'," +
                "  `name` varchar(128) NOT NULL COMMENT '企业名称'," +
                "  `industry` varchar(64) DEFAULT NULL COMMENT '所属行业分类'," +
                "  `scale` varchar(32) DEFAULT NULL COMMENT '企业规模(初创/小型/中型/大型/集团)'," +
                "  `business` varchar(512) DEFAULT NULL COMMENT '主要业务'," +
                "  `main_products` varchar(512) DEFAULT NULL COMMENT '主要产品/服务'," +
                "  `established_date` date DEFAULT NULL COMMENT '成立时间'," +
                "  `capital` varchar(64) DEFAULT NULL COMMENT '注册资本'," +
                "  `address` varchar(255) DEFAULT NULL COMMENT '地址'," +
                "  `market_performance` varchar(512) DEFAULT NULL COMMENT '市场表现(营收概况/增长态势)'," +
                "  `competitive_advantage` varchar(512) DEFAULT NULL COMMENT '竞争优势(技术/渠道/品牌)'," +
                "  `contact_name` varchar(64) DEFAULT NULL COMMENT '联系人姓名'," +
                "  `contact_phone` varchar(128) DEFAULT NULL COMMENT '联系人手机(AES加密)'," +
                "  `contact_position` varchar(64) DEFAULT NULL COMMENT '联系人职务'," +
                "  `create_by` bigint DEFAULT NULL COMMENT '创建人'," +
                "  `create_time` bigint DEFAULT NULL COMMENT '创建时间'," +
                "  `update_by` bigint DEFAULT NULL COMMENT '更新人'," +
                "  `update_time` bigint DEFAULT NULL COMMENT '更新时间'," +
                "  `deleted` tinyint DEFAULT 0 COMMENT '删除标记(0:正常 1:删除)'," +
                "  `delete_time` bigint DEFAULT 0 COMMENT '删除时间(毫秒时间戳)'," +
                "  PRIMARY KEY (`id`)," +
                "  KEY `idx_company_name` (`name`)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客户企业表'"
            );
            System.out.println("[BizSchemaMigrate] 已创建 biz_customer_company 表");
        }
    }

    // ============================
    // 3. 客户深度画像表(1:1)
    // ============================
    private void createBizCustomerProfile() {
        if (!tableExists("biz_customer_profile")) {
            jdbcTemplate.execute(
                "CREATE TABLE `biz_customer_profile` (" +
                "  `id` bigint NOT NULL COMMENT '主键'," +
                "  `customer_id` bigint NOT NULL COMMENT '客户ID(biz_customer.id)'," +
                "  `dynamic_info` text DEFAULT NULL COMMENT '动态信息(人性观察/心理学特征/读心术信号；制度对客户的影响)'," +
                "  `value_level` int DEFAULT NULL COMMENT '需求层次(1生理 2安全 3社交 4尊重 5自我实现)'," +
                "  `value_expect` varchar(512) DEFAULT NULL COMMENT '客户期望(对产品或服务的核心期待)'," +
                "  `value_interest` varchar(512) DEFAULT NULL COMMENT '利益点(客户的利益诉求/关注维度)'," +
                "  `strategy` varchar(1024) DEFAULT NULL COMMENT '应对策略(沟通定位/切入角度)'," +
                "  `talk_script` text DEFAULT NULL COMMENT '话术设计(开场/痛点/方案/成交话术)'," +
                "  `analysis` text DEFAULT NULL COMMENT '分析(综合判断/下一步动作)'," +
                "  `create_by` bigint DEFAULT NULL COMMENT '创建人'," +
                "  `create_time` bigint DEFAULT NULL COMMENT '创建时间'," +
                "  `update_by` bigint DEFAULT NULL COMMENT '更新人'," +
                "  `update_time` bigint DEFAULT NULL COMMENT '更新时间'," +
                "  `del_flag` tinyint DEFAULT 0 COMMENT '删除标记(0:正常 1:删除)'," +
                "  `delete_time` bigint DEFAULT 0 COMMENT '删除时间'," +
                "  PRIMARY KEY (`id`)," +
                "  UNIQUE KEY `uk_customer_profile` (`customer_id`)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客户深度画像表'"
            );
            System.out.println("[BizSchemaMigrate] 已创建 biz_customer_profile 表");
        }
    }

    // ============================
    // 4. 客户跟进记录表
    // ============================
    private void createBizCustomerFollowup() {
        if (!tableExists("biz_customer_followup")) {
            jdbcTemplate.execute(
                "CREATE TABLE `biz_customer_followup` (" +
                "  `id` bigint NOT NULL COMMENT '主键'," +
                "  `customer_id` bigint NOT NULL COMMENT '客户ID(biz_customer.id)'," +
                "  `type` varchar(16) DEFAULT NULL COMMENT '跟进方式(电话/面谈/微信/邮件)'," +
                "  `content` text DEFAULT NULL COMMENT '跟进内容'," +
                "  `result` varchar(512) DEFAULT NULL COMMENT '跟进结果'," +
                "  `next_time` bigint DEFAULT NULL COMMENT '下次跟进时间(毫秒时间戳)'," +
                "  `create_user` bigint DEFAULT NULL COMMENT '创建人ID'," +
                "  `create_user_name` varchar(64) DEFAULT NULL COMMENT '创建人姓名'," +
                "  `create_time` bigint DEFAULT NULL COMMENT '创建时间'," +
                "  PRIMARY KEY (`id`)," +
                "  KEY `idx_followup_customer` (`customer_id`, `create_time`)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客户跟进记录表'"
            );
            System.out.println("[BizSchemaMigrate] 已创建 biz_customer_followup 表");
        }
    }

    // ============================
    // 5. 客户-行业多对多关联表
    // ============================
    private void createBizCustomerIndustry() {
        if (!tableExists("biz_customer_industry")) {
            jdbcTemplate.execute(
                "CREATE TABLE `biz_customer_industry` (" +
                "  `id` bigint NOT NULL COMMENT '主键'," +
                "  `customer_id` bigint NOT NULL COMMENT '客户ID(biz_customer.id)'," +
                "  `industry_id` bigint NOT NULL COMMENT '行业ID(biz_industry.id，同模块 industry 子域)'," +
                "  `relation_type` tinyint DEFAULT 1 COMMENT '关系类型(1:主营行业 2:关联行业 3:潜在行业)'," +
                "  `is_main` tinyint DEFAULT 0 COMMENT '是否主营行业(0:否 1:是，每个客户至多一个)'," +
                "  `remark` varchar(255) DEFAULT NULL COMMENT '关联备注(如\"客户在该行业的角色\")'," +
                "  `create_by` bigint DEFAULT NULL COMMENT '创建人'," +
                "  `create_time` bigint DEFAULT NULL COMMENT '创建时间'," +
                "  `update_by` bigint DEFAULT NULL COMMENT '更新人'," +
                "  `update_time` bigint DEFAULT NULL COMMENT '更新时间'," +
                "  `del_flag` tinyint DEFAULT 0 COMMENT '删除标记(0:正常 1:删除)'," +
                "  `delete_time` bigint DEFAULT 0 COMMENT '删除时间'," +
                "  PRIMARY KEY (`id`)," +
                "  UNIQUE KEY `uk_customer_industry` (`customer_id`, `industry_id`)," +
                "  KEY `idx_ci_industry` (`industry_id`, `del_flag`)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客户-行业关联表'"
            );
            System.out.println("[BizSchemaMigrate] 已创建 biz_customer_industry 表");
        }
    }

    // ============================
    // 6. 行业主表
    // ============================
    private void createBizIndustry() {
        if (!tableExists("biz_industry")) {
            jdbcTemplate.execute(
                "CREATE TABLE `biz_industry` (" +
                "  `id` bigint NOT NULL COMMENT '主键'," +
                "  `industry_name` varchar(128) NOT NULL COMMENT '行业名称'," +
                "  `definition` varchar(1024) DEFAULT NULL COMMENT '行业定义（边界说明）'," +
                "  `technology` varchar(1024) DEFAULT NULL COMMENT '核心技术（行业需要的技术/设计能力）'," +
                "  `industry_code` varchar(32) DEFAULT NULL COMMENT '行业编码（自定义，便于引用）'," +
                "  `tags` varchar(256) DEFAULT NULL COMMENT '标签(逗号分隔)'," +
                "  `upstream_chain` varchar(1024) DEFAULT NULL COMMENT '上游产业链（原材料/供应商）'," +
                "  `midstream_chain` varchar(1024) DEFAULT NULL COMMENT '中游产业链（产品制造商/集成商）'," +
                "  `downstream_channel` varchar(1024) DEFAULT NULL COMMENT '下游销售渠道'," +
                "  `downstream_marketing` varchar(1024) DEFAULT NULL COMMENT '下游营销方式'," +
                "  `development_overview` varchar(1024) DEFAULT NULL COMMENT '行业发展概况'," +
                "  `market_size` varchar(128) DEFAULT NULL COMMENT '市场规模（可含单位）'," +
                "  `growth_potential` varchar(128) DEFAULT NULL COMMENT '增长潜力/增速'," +
                "  `dynamic_info` text DEFAULT NULL COMMENT '动态信息（社会/文化/行业/市场变化；制度影响）'," +
                "  `value_info` text DEFAULT NULL COMMENT '价值信息（行业价值/机会评估）'," +
                "  `industry_resources` varchar(1024) DEFAULT NULL COMMENT '行业资源（关键资源/人脉/资质）'," +
                "  `strategy` text DEFAULT NULL COMMENT '如何把握（布局策略/竞争打法）'," +
                "  `gross_profit` decimal(12,2) DEFAULT NULL COMMENT '毛利润（销售收入-销售成本）'," +
                "  `gross_margin` decimal(5,2) DEFAULT NULL COMMENT '毛利率（%）'," +
                "  `net_profit` decimal(12,2) DEFAULT NULL COMMENT '净利润（总收入-总费用）'," +
                "  `net_margin` decimal(5,2) DEFAULT NULL COMMENT '净利率（%）'," +
                "  `visibility` tinyint DEFAULT 1 COMMENT '可见性(0:私有 1:公开 2:系统预置)'," +
                "  `sort` int DEFAULT 0 COMMENT '排序(越小越前)'," +
                "  `create_by` bigint DEFAULT NULL COMMENT '创建人'," +
                "  `create_time` bigint DEFAULT NULL COMMENT '创建时间'," +
                "  `update_by` bigint DEFAULT NULL COMMENT '更新人'," +
                "  `update_time` bigint DEFAULT NULL COMMENT '更新时间'," +
                "  `del_flag` tinyint DEFAULT 0 COMMENT '删除标记(0:正常 1:删除)'," +
                "  `delete_time` bigint DEFAULT 0 COMMENT '删除时间'," +
                "  `delete_by` bigint DEFAULT NULL COMMENT '删除人'," +
                "  PRIMARY KEY (`id`)," +
                "  KEY `idx_industry_name` (`industry_name`)," +
                "  KEY `idx_industry_code` (`industry_code`)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='行业主表'"
            );
            System.out.println("[BizSchemaMigrate] 已创建 biz_industry 表");
        }
    }

    // ============================
    // 7. 行业产品表
    // ============================
    private void createBizIndustryProduct() {
        if (!tableExists("biz_industry_product")) {
            jdbcTemplate.execute(
                "CREATE TABLE `biz_industry_product` (" +
                "  `id` bigint NOT NULL COMMENT '主键'," +
                "  `category` varchar(64) DEFAULT NULL COMMENT '产品分类'," +
                "  `product_name` varchar(128) NOT NULL COMMENT '产品名称'," +
                "  `product_concept` text DEFAULT NULL COMMENT '产品概念（核心定义）'," +
                "  `consumer_insight` text DEFAULT NULL COMMENT '消费者洞察（市场关联点）'," +
                "  `benefit_promise` varchar(512) DEFAULT NULL COMMENT '利益承诺（给客户的利益）'," +
                "  `support_point` varchar(512) DEFAULT NULL COMMENT '支撑点（承诺的依据）'," +
                "  `core_product` varchar(512) DEFAULT NULL COMMENT '核心产品（核心价值层）'," +
                "  `basic_product` varchar(512) DEFAULT NULL COMMENT '基础产品（基本效用层）'," +
                "  `additional_product` varchar(512) DEFAULT NULL COMMENT '附加产品（服务/售后/增值层）'," +
                "  `potential_product` varchar(512) DEFAULT NULL COMMENT '潜在产品（未来延伸层）'," +
                "  `life_cycle` varchar(32) DEFAULT NULL COMMENT '产品生命周期(导入期/成长期/成熟期/衰退期)'," +
                "  `upstream_chain` varchar(1024) DEFAULT NULL COMMENT '上游（原材料）'," +
                "  `midstream_chain` varchar(1024) DEFAULT NULL COMMENT '中游（产品制造商）'," +
                "  `downstream_channel` varchar(1024) DEFAULT NULL COMMENT '下游渠道'," +
                "  `downstream_marketing` varchar(1024) DEFAULT NULL COMMENT '下游营销方式'," +
                "  `create_by` bigint DEFAULT NULL COMMENT '创建人'," +
                "  `create_time` bigint DEFAULT NULL COMMENT '创建时间'," +
                "  `update_by` bigint DEFAULT NULL COMMENT '更新人'," +
                "  `update_time` bigint DEFAULT NULL COMMENT '更新时间'," +
                "  `del_flag` tinyint DEFAULT 0 COMMENT '删除标记(0:正常 1:删除)'," +
                "  `delete_time` bigint DEFAULT 0 COMMENT '删除时间'," +
                "  `delete_by` bigint DEFAULT NULL COMMENT '删除人'," +
                "  PRIMARY KEY (`id`)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='行业产品表'"
            );
            System.out.println("[BizSchemaMigrate] 已创建 biz_industry_product 表");
        }
    }

    // ============================
    // 8. 行业企业表
    // ============================
    private void createBizIndustryEnterprise() {
        if (!tableExists("biz_industry_enterprise")) {
            jdbcTemplate.execute(
                "CREATE TABLE `biz_industry_enterprise` (" +
                "  `id` bigint NOT NULL COMMENT '主键'," +
                "  `enterprise_type` varchar(32) DEFAULT NULL COMMENT '企业/平台类型(如:生产商/经销商/平台/SaaS服务商)'," +
                "  `enterprise_name` varchar(128) NOT NULL COMMENT '企业/平台名称'," +
                "  `established_date` date DEFAULT NULL COMMENT '成立时间'," +
                "  `registered_capital` varchar(64) DEFAULT NULL COMMENT '注册资本'," +
                "  `paid_capital` varchar(64) DEFAULT NULL COMMENT '实缴资本'," +
                "  `scale` varchar(50) DEFAULT NULL COMMENT '企业规模(人数)'," +
                "  `insured_count` int DEFAULT NULL COMMENT '参保人数'," +
                "  `is_listed` tinyint DEFAULT 0 COMMENT '是否上市(0:否 1:是)'," +
                "  `main_business` varchar(1024) DEFAULT NULL COMMENT '主要业务'," +
                "  `core_technology` varchar(512) DEFAULT NULL COMMENT '核心技术'," +
                "  `products` varchar(1024) DEFAULT NULL COMMENT '产品/服务'," +
                "  `market_performance` varchar(1024) DEFAULT NULL COMMENT '市场表现(营收/市占率)'," +
                "  `competitors` varchar(1024) DEFAULT NULL COMMENT '主要竞争对手'," +
                "  `advantage` varchar(512) DEFAULT NULL COMMENT '竞争优势'," +
                "  `disadvantage` varchar(512) DEFAULT NULL COMMENT '竞争不足'," +
                "  `upstream_chain` varchar(1024) DEFAULT NULL COMMENT '上游（原材料）'," +
                "  `midstream_chain` varchar(1024) DEFAULT NULL COMMENT '中游（产品制造商）'," +
                "  `downstream_channel` varchar(1024) DEFAULT NULL COMMENT '下游渠道'," +
                "  `downstream_marketing` varchar(1024) DEFAULT NULL COMMENT '下游营销方式'," +
                "  `create_by` bigint DEFAULT NULL COMMENT '创建人'," +
                "  `create_time` bigint DEFAULT NULL COMMENT '创建时间'," +
                "  `update_by` bigint DEFAULT NULL COMMENT '更新人'," +
                "  `update_time` bigint DEFAULT NULL COMMENT '更新时间'," +
                "  `del_flag` tinyint DEFAULT 0 COMMENT '删除标记(0:正常 1:删除)'," +
                "  `delete_time` bigint DEFAULT 0 COMMENT '删除时间'," +
                "  `delete_by` bigint DEFAULT NULL COMMENT '删除人'," +
                "  PRIMARY KEY (`id`)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='行业企业表'"
            );
            System.out.println("[BizSchemaMigrate] 已创建 biz_industry_enterprise 表");
        }
    }

    // ============================
    // 9. 行业市场表
    // ============================
    private void createBizIndustryMarket() {
        if (!tableExists("biz_industry_market")) {
            jdbcTemplate.execute(
                "CREATE TABLE `biz_industry_market` (" +
                "  `id` bigint NOT NULL COMMENT '主键'," +
                "  `demand` text DEFAULT NULL COMMENT '市场需求（客群规模/痛点）'," +
                "  `opportunity` text DEFAULT NULL COMMENT '商机（可切入的机会点）'," +
                "  `value_proposition` varchar(512) DEFAULT NULL COMMENT '价值主张（为客户创造什么价值）'," +
                "  `customer_segment` varchar(512) DEFAULT NULL COMMENT '客户细分'," +
                "  `channel` varchar(512) DEFAULT NULL COMMENT '渠道通路'," +
                "  `customer_relation` varchar(512) DEFAULT NULL COMMENT '客户关系（如何建立/维护）'," +
                "  `revenue_source` varchar(512) DEFAULT NULL COMMENT '收入来源'," +
                "  `key_resource` varchar(512) DEFAULT NULL COMMENT '关键资源'," +
                "  `key_partner` varchar(512) DEFAULT NULL COMMENT '关键伙伴'," +
                "  `key_activity` varchar(512) DEFAULT NULL COMMENT '关键活动'," +
                "  `cost_structure` varchar(512) DEFAULT NULL COMMENT '成本结构'," +
                "  `value_evaluation` text DEFAULT NULL COMMENT '价值评价'," +
                "  `value_distribution` text DEFAULT NULL COMMENT '价值分配（产业链利润分配）'," +
                "  `competition_method` text DEFAULT NULL COMMENT '竞争手段（行业/产品/企业三层）'," +
                "  `promo_channel` text DEFAULT NULL COMMENT '推广引流（企业/产品两个维度）'," +
                "  `create_by` bigint DEFAULT NULL COMMENT '创建人'," +
                "  `create_time` bigint DEFAULT NULL COMMENT '创建时间'," +
                "  `update_by` bigint DEFAULT NULL COMMENT '更新人'," +
                "  `update_time` bigint DEFAULT NULL COMMENT '更新时间'," +
                "  `del_flag` tinyint DEFAULT 0 COMMENT '删除标记(0:正常 1:删除)'," +
                "  `delete_time` bigint DEFAULT 0 COMMENT '删除时间'," +
                "  `delete_by` bigint DEFAULT NULL COMMENT '删除人'," +
                "  PRIMARY KEY (`id`)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='行业市场表'"
            );
            System.out.println("[BizSchemaMigrate] 已创建 biz_industry_market 表");
        }
    }

    // ============================
    // 种子数据 - 行业主数据
    // ============================
    private void seedIndustry() {
        try {
            Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM biz_industry", Integer.class);
            if (count != null && count == 0) {
                jdbcTemplate.execute("INSERT INTO biz_industry (id, industry_name, definition, industry_code, sort, visibility, create_time) VALUES " +
                    "(1, '互联网/IT', '以信息技术为核心的软件、硬件、网络服务行业', 'IT', 1, 2, 1757520000000), " +
                    "(2, '人工智能', '涵盖机器学习、计算机视觉、NLP 等的智能技术产业', 'AI', 2, 2, 1757520000000), " +
                    "(3, '金融', '银行、证券、保险、投资等金融服务行业', 'FIN', 3, 2, 1757520000000), " +
                    "(4, '制造业', '以机械、电子、化工等为主的实体制造行业', 'MFG', 4, 2, 1757520000000), " +
                    "(5, '医疗健康', '医药、器械、医疗服务、健康管理行业', 'MED', 5, 2, 1757520000000)");
                System.out.println("[BizSchemaMigrate] 已初始化行业主数据");
            }
        } catch (Exception e) {
            System.out.println("[BizSchemaMigrate] 初始化行业主数据: " + e.getMessage());
        }
    }

    // ============================
    // 种子数据 - 示例客户
    // ============================
    private void seedCustomer() {
        try {
            Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM biz_customer", Integer.class);
            if (count != null && count == 0) {
                jdbcTemplate.execute("INSERT INTO biz_customer (id, name, gender, age, phone, email, address, customer_type, status, source, company_id, demand_level, value_score, create_time) VALUES " +
                    "(1, '张三', 1, 30, '13800138000', 'zhangsan@example.com', '广东省深圳市南山区', 1, 2, '线上推广', NULL, 4, 4, 1757520000000), " +
                    "(2, '李四', 1, 35, '13900139000', 'lisi@example.com', '上海市浦东新区', 1, 1, '转介绍', NULL, 3, 3, 1757520000000), " +
                    "(3, '王五', 2, 28, '13700137000', 'wangwu@example.com', '北京市朝阳区', 2, 3, '展会', 1, 5, 5, 1757520000000)");
                System.out.println("[BizSchemaMigrate] 已初始化示例客户数据");
            }
        } catch (Exception e) {
            System.out.println("[BizSchemaMigrate] 初始化示例客户数据: " + e.getMessage());
        }
    }

    // ============================
    // 种子数据 - 客户-行业关联
    // ============================
    private void seedCustomerIndustry() {
        try {
            Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM biz_customer_industry", Integer.class);
            if (count != null && count == 0) {
                jdbcTemplate.execute("INSERT INTO biz_customer_industry (id, customer_id, industry_id, relation_type, is_main, create_time) VALUES " +
                    "(1, 1, 1, 1, 1, 1757520000000), " +
                    "(2, 1, 2, 2, 0, 1757520000000), " +
                    "(3, 2, 3, 1, 1, 1757520000000), " +
                    "(4, 3, 4, 1, 1, 1757520000000)");
                System.out.println("[BizSchemaMigrate] 已初始化客户-行业关联数据");
            }
        } catch (Exception e) {
            System.out.println("[BizSchemaMigrate] 初始化客户-行业关联数据: " + e.getMessage());
        }
    }

    // ============================
    // 种子数据 - 客户深度画像
    // ============================
    private void seedCustomerProfile() {
        try {
            Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM biz_customer_profile", Integer.class);
            if (count != null && count == 0) {
                jdbcTemplate.execute("INSERT INTO biz_customer_profile (id, customer_id, dynamic_info, value_level, value_expect, create_time) VALUES " +
                    "(1, 1, '重视长期合作关系', 4, '系统性解决方案', 1757520000000), " +
                    "(2, 2, '决策偏保守', 3, '降低运营成本', 1757520000000), " +
                    "(3, 3, '企业扩张期，需求综合', 5, '一站式服务', 1757520000000)");
                System.out.println("[BizSchemaMigrate] 已初始化客户深度画像数据");
            }
        } catch (Exception e) {
            System.out.println("[BizSchemaMigrate] 初始化客户深度画像数据: " + e.getMessage());
        }
    }

    private boolean tableExists(String tableName) {
        try {
            Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES " +
                "WHERE TABLE_SCHEMA = (SELECT DATABASE()) AND TABLE_NAME = ?",
                Integer.class, tableName);
            return count != null && count > 0;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 检查列是否存在，如果不存在则添加
     */
    private void ensureColumnExists(String tableName, String columnName, String columnDefinition) {
        try {
            Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS " +
                "WHERE TABLE_SCHEMA = (SELECT DATABASE()) AND TABLE_NAME = ? AND COLUMN_NAME = ?",
                Integer.class, tableName, columnName);
            if (count == null || count == 0) {
                jdbcTemplate.execute(
                    "ALTER TABLE `" + tableName + "` ADD COLUMN `" + columnName + "` " + columnDefinition);
                System.out.println("[BizSchemaMigrate] 已添加缺失列 " + tableName + "." + columnName);
            }
        } catch (Exception e) {
            System.out.println("[BizSchemaMigrate] 检查列 " + tableName + "." + columnName + ": " + e.getMessage());
        }
    }

    // ============================
    // 10. 行业-企业 多对多关联表
    // ============================
    private void createBizIndustryEnterpriseRel() {
        if (!tableExists("biz_industry_enterprise_rel")) {
            jdbcTemplate.execute(
                "CREATE TABLE `biz_industry_enterprise_rel` (" +
                "  `id` bigint NOT NULL COMMENT '主键'," +
                "  `industry_id` bigint NOT NULL COMMENT '行业ID(biz_industry.id)'," +
                "  `enterprise_id` bigint NOT NULL COMMENT '企业ID(biz_industry_enterprise.id)'," +
                "  `remark` varchar(255) DEFAULT NULL COMMENT '关联备注'," +
                "  `create_by` bigint DEFAULT NULL COMMENT '创建人'," +
                "  `create_time` bigint DEFAULT NULL COMMENT '创建时间'," +
                "  `update_by` bigint DEFAULT NULL COMMENT '更新人'," +
                "  `update_time` bigint DEFAULT NULL COMMENT '更新时间'," +
                "  `del_flag` tinyint DEFAULT 0 COMMENT '删除标记(0:正常 1:删除)'," +
                "  `delete_time` bigint DEFAULT 0 COMMENT '删除时间'," +
                "  PRIMARY KEY (`id`)," +
                "  UNIQUE KEY `uk_ie_rel` (`industry_id`, `enterprise_id`)," +
                "  KEY `idx_ie_ent` (`enterprise_id`, `del_flag`)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='行业-企业关联表'"
            );
            System.out.println("[BizSchemaMigrate] 已创建 biz_industry_enterprise_rel 表");
        }
    }

    // ============================
    // 11. 行业-产品 多对多关联表
    // ============================
    private void createBizIndustryProductRel() {
        if (!tableExists("biz_industry_product_rel")) {
            jdbcTemplate.execute(
                "CREATE TABLE `biz_industry_product_rel` (" +
                "  `id` bigint NOT NULL COMMENT '主键'," +
                "  `industry_id` bigint NOT NULL COMMENT '行业ID(biz_industry.id)'," +
                "  `product_id` bigint NOT NULL COMMENT '产品ID(biz_industry_product.id)'," +
                "  `remark` varchar(255) DEFAULT NULL COMMENT '关联备注'," +
                "  `create_by` bigint DEFAULT NULL COMMENT '创建人'," +
                "  `create_time` bigint DEFAULT NULL COMMENT '创建时间'," +
                "  `update_by` bigint DEFAULT NULL COMMENT '更新人'," +
                "  `update_time` bigint DEFAULT NULL COMMENT '更新时间'," +
                "  `del_flag` tinyint DEFAULT 0 COMMENT '删除标记(0:正常 1:删除)'," +
                "  `delete_time` bigint DEFAULT 0 COMMENT '删除时间'," +
                "  PRIMARY KEY (`id`)," +
                "  UNIQUE KEY `uk_ip_rel` (`industry_id`, `product_id`)," +
                "  KEY `idx_ip_prod` (`product_id`, `del_flag`)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='行业-产品关联表'"
            );
            System.out.println("[BizSchemaMigrate] 已创建 biz_industry_product_rel 表");
        }
    }

    // ============================
    // 12. 企业-产品 多对多关联表
    // ============================
    private void createBizEnterpriseProductRel() {
        if (!tableExists("biz_enterprise_product_rel")) {
            jdbcTemplate.execute(
                "CREATE TABLE `biz_enterprise_product_rel` (" +
                "  `id` bigint NOT NULL COMMENT '主键'," +
                "  `enterprise_id` bigint NOT NULL COMMENT '企业ID(biz_industry_enterprise.id)'," +
                "  `product_id` bigint NOT NULL COMMENT '产品ID(biz_industry_product.id)'," +
                "  `remark` varchar(255) DEFAULT NULL COMMENT '关联备注'," +
                "  `create_by` bigint DEFAULT NULL COMMENT '创建人'," +
                "  `create_time` bigint DEFAULT NULL COMMENT '创建时间'," +
                "  `update_by` bigint DEFAULT NULL COMMENT '更新人'," +
                "  `update_time` bigint DEFAULT NULL COMMENT '更新时间'," +
                "  `del_flag` tinyint DEFAULT 0 COMMENT '删除标记(0:正常 1:删除)'," +
                "  `delete_time` bigint DEFAULT 0 COMMENT '删除时间'," +
                "  PRIMARY KEY (`id`)," +
                "  UNIQUE KEY `uk_ep_rel` (`enterprise_id`, `product_id`)," +
                "  KEY `idx_ep_prod` (`product_id`, `del_flag`)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='企业-产品关联表'"
            );
            System.out.println("[BizSchemaMigrate] 已创建 biz_enterprise_product_rel 表");
        }
    }

    // ============================
    // 13. 行业-市场 多对多关联表
    // ============================
    private void createBizIndustryMarketRel() {
        if (!tableExists("biz_industry_market_rel")) {
            jdbcTemplate.execute(
                "CREATE TABLE `biz_industry_market_rel` (" +
                "  `id` bigint NOT NULL COMMENT '主键'," +
                "  `industry_id` bigint NOT NULL COMMENT '行业ID(biz_industry.id)'," +
                "  `market_id` bigint NOT NULL COMMENT '市场ID(biz_industry_market.id)'," +
                "  `remark` varchar(255) DEFAULT NULL COMMENT '关联备注'," +
                "  `create_by` bigint DEFAULT NULL COMMENT '创建人'," +
                "  `create_time` bigint DEFAULT NULL COMMENT '创建时间'," +
                "  `update_by` bigint DEFAULT NULL COMMENT '更新人'," +
                "  `update_time` bigint DEFAULT NULL COMMENT '更新时间'," +
                "  `del_flag` tinyint DEFAULT 0 COMMENT '删除标记(0:正常 1:删除)'," +
                "  `delete_time` bigint DEFAULT 0 COMMENT '删除时间'," +
                "  PRIMARY KEY (`id`)," +
                "  UNIQUE KEY `uk_im_rel` (`industry_id`, `market_id`)," +
                "  KEY `idx_im_mkt` (`market_id`, `del_flag`)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='行业-市场关联表'"
            );
            System.out.println("[BizSchemaMigrate] 已创建 biz_industry_market_rel 表");
        }
    }

    // ============================
    // 14. 行业关联数据迁移(industry_id → 中间表 M:N)
    // ============================
    private void migrateIndustryRelData() {
        migrateBizIndustryProductRel();
        migrateBizIndustryEnterpriseRel();
        migrateBizIndustryMarketRel();
    }

    private void migrateBizIndustryProductRel() {
        try {
            if (columnExists("biz_industry_product", "industry_id")) {
                jdbcTemplate.execute(
                    "INSERT IGNORE INTO biz_industry_product_rel (id, industry_id, product_id, remark, create_time, del_flag) " +
                    "SELECT (UNIX_TIMESTAMP() * 1000 + (@r := @r + 1)), t.industry_id, t.id, NULL, 1757520000000, 0 " +
                    "FROM biz_industry_product t, (SELECT @r := 0) AS init " +
                    "WHERE t.del_flag = 0"
                );
                jdbcTemplate.execute("ALTER TABLE `biz_industry_product` DROP COLUMN `industry_id`");
                System.out.println("[BizSchemaMigrate] 已迁移 biz_industry_product 关联并移除 industry_id 列");
            }
        } catch (Exception e) {
            System.out.println("[BizSchemaMigrate] 迁移 biz_industry_product 关联: " + e.getMessage());
        }
    }

    private void migrateBizIndustryEnterpriseRel() {
        try {
            if (columnExists("biz_industry_enterprise", "industry_id")) {
                jdbcTemplate.execute(
                    "INSERT IGNORE INTO biz_industry_enterprise_rel (id, industry_id, enterprise_id, remark, create_time, del_flag) " +
                    "SELECT (UNIX_TIMESTAMP() * 1000 + (@r := @r + 1)), t.industry_id, t.id, NULL, 1757520000000, 0 " +
                    "FROM biz_industry_enterprise t, (SELECT @r := 0) AS init " +
                    "WHERE t.del_flag = 0"
                );
                jdbcTemplate.execute("ALTER TABLE `biz_industry_enterprise` DROP COLUMN `industry_id`");
                System.out.println("[BizSchemaMigrate] 已迁移 biz_industry_enterprise 关联并移除 industry_id 列");
            }
        } catch (Exception e) {
            System.out.println("[BizSchemaMigrate] 迁移 biz_industry_enterprise 关联: " + e.getMessage());
        }
    }

    private void migrateBizIndustryMarketRel() {
        try {
            if (indexExists("biz_industry_market", "uk_market_industry")) {
                jdbcTemplate.execute("ALTER TABLE `biz_industry_market` DROP INDEX `uk_market_industry`");
                System.out.println("[BizSchemaMigrate] 已移除 biz_industry_market.uk_market_industry 唯一索引");
            }
            if (columnExists("biz_industry_market", "industry_id")) {
                jdbcTemplate.execute(
                    "INSERT IGNORE INTO biz_industry_market_rel (id, industry_id, market_id, remark, create_time, del_flag) " +
                    "SELECT (UNIX_TIMESTAMP() * 1000 + (@r := @r + 1)), t.industry_id, t.id, NULL, 1757520000000, 0 " +
                    "FROM biz_industry_market t, (SELECT @r := 0) AS init " +
                    "WHERE t.del_flag = 0"
                );
                jdbcTemplate.execute("ALTER TABLE `biz_industry_market` DROP COLUMN `industry_id`");
                System.out.println("[BizSchemaMigrate] 已迁移 biz_industry_market 关联并移除 industry_id 列");
            }
        } catch (Exception e) {
            System.out.println("[BizSchemaMigrate] 迁移 biz_industry_market 关联: " + e.getMessage());
        }
    }

    private boolean columnExists(String tableName, String columnName) {
        try {
            Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS " +
                "WHERE TABLE_SCHEMA = (SELECT DATABASE()) AND TABLE_NAME = ? AND COLUMN_NAME = ?",
                Integer.class, tableName, columnName);
            return count != null && count > 0;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean indexExists(String tableName, String indexName) {
        try {
            Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM INFORMATION_SCHEMA.STATISTICS " +
                "WHERE TABLE_SCHEMA = (SELECT DATABASE()) AND TABLE_NAME = ? AND INDEX_NAME = ?",
                Integer.class, tableName, indexName);
            return count != null && count > 0;
        } catch (Exception e) {
            return false;
        }
    }
}