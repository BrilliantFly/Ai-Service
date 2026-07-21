/*
 Navicat Premium Data Transfer

 Source Server         : 39.98.108.121
 Source Server Type    : MySQL
 Source Server Version : 50740
 Source Host           : 39.98.108.121:3306
 Source Schema         : know_boot

 Target Server Type    : MySQL
 Target Server Version : 50740
 File Encoding         : 65001

 Date: 27/05/2023 23:31:19
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for base_config
-- ----------------------------
DROP TABLE IF EXISTS `base_config`;
CREATE TABLE `base_config`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `parent_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '父ID',
  `config_level` tinyint(2) NULL DEFAULT NULL COMMENT '层级（用于计算）',
  `leaf_node_flag` tinyint(2) NULL DEFAULT NULL COMMENT '是否叶子节点（0：否；1：是；用于计算）',
  `config_type` tinyint(2) NULL DEFAULT NULL COMMENT '配置类型（1：菜单；2：导航）',
  `page_type` tinyint(2) NULL DEFAULT NULL COMMENT '界面类型（1：首页；2：文章；3：资源；4：留言板；5：我的）',
  `title` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标题',
  `icon_info` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图标',
  `url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '路径',
  `content` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '内容',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `del_flag` int(1) NULL DEFAULT NULL COMMENT '删除状态',
  `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `show_flag` int(11) NULL DEFAULT NULL COMMENT '显示类型（0：否；1：是）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '基础配置表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of base_config
-- ----------------------------
INSERT INTO `base_config` VALUES ('1659750069836300290', NULL, NULL, NULL, 1, 1, '首页', NULL, '1', '1', NULL, 0, 'admin', '2023-05-20 00:00:00', 'admin', '2023-05-20 13:31:26', NULL);
INSERT INTO `base_config` VALUES ('1659750156595478530', '1659750069836300290', NULL, NULL, 1, 1, '基础', NULL, '1', '1', NULL, 0, 'admin', '2023-05-20 00:00:00', 'admin', '2023-05-20 13:31:39', NULL);
INSERT INTO `base_config` VALUES ('1659750855425245186', '1659750069836300290', NULL, NULL, 1, 1, '进阶', NULL, NULL, NULL, NULL, 0, 'admin', '2023-05-20 00:00:00', 'admin', '2023-05-20 13:31:47', NULL);
INSERT INTO `base_config` VALUES ('1659750931786743809', '1659750069836300290', NULL, NULL, 1, 1, '教程', NULL, NULL, NULL, NULL, 1, 'admin', '2023-05-20 10:40:33', NULL, NULL, NULL);
INSERT INTO `base_config` VALUES ('1659751478233251841', '1659750069836300290', NULL, NULL, 1, 1, '资源', NULL, NULL, NULL, NULL, 0, 'admin', '2023-05-20 10:42:44', NULL, NULL, NULL);
INSERT INTO `base_config` VALUES ('1659751716125786114', '1659750069836300290', NULL, NULL, 1, 1, '其他', NULL, NULL, NULL, NULL, 0, 'admin', '2023-05-20 00:00:00', 'admin', '2023-05-20 13:33:13', NULL);
INSERT INTO `base_config` VALUES ('1659753285869195265', '1659750156595478530', NULL, NULL, 1, 1, '环境搭建', NULL, NULL, NULL, NULL, 0, 'admin', '2023-05-20 00:00:00', 'admin', '2023-05-20 13:32:06', NULL);
INSERT INTO `base_config` VALUES ('1659753368236937218', '1659750156595478530', NULL, NULL, 1, 1, '基础教程', NULL, NULL, NULL, NULL, 0, 'admin', '2023-05-20 00:00:00', 'admin', '2023-05-20 13:32:15', NULL);
INSERT INTO `base_config` VALUES ('1659753730285064194', '1659750855425245186', NULL, NULL, 1, 1, '进阶教程', NULL, NULL, NULL, NULL, 0, 'admin', '2023-05-20 00:00:00', 'admin', '2023-05-20 13:32:38', NULL);
INSERT INTO `base_config` VALUES ('1659753919167156225', '1659750855425245186', NULL, NULL, 1, 1, '实战教程', NULL, NULL, NULL, NULL, 0, 'admin', '2023-05-20 00:00:00', 'admin', '2023-05-20 13:32:45', NULL);
INSERT INTO `base_config` VALUES ('1659755778376290305', '1659750156595478530', NULL, NULL, 1, 1, '基础视频', NULL, NULL, NULL, NULL, 0, 'admin', '2023-05-20 00:00:00', 'admin', '2023-05-20 13:32:22', NULL);
INSERT INTO `base_config` VALUES ('1659755928654008321', '1659750156595478530', NULL, NULL, 1, 1, '常见问题', NULL, NULL, NULL, NULL, 0, 'admin', '2023-05-20 00:00:00', 'admin', '2023-05-20 13:32:30', NULL);
INSERT INTO `base_config` VALUES ('1659756211941494786', '1659751478233251841', NULL, NULL, 1, 1, '源码', NULL, NULL, NULL, NULL, 0, 'admin', '2023-05-20 00:00:00', 'admin', '2023-05-20 13:32:56', NULL);
INSERT INTO `base_config` VALUES ('1659756292035923970', '1659751478233251841', NULL, NULL, 1, 1, '软件', NULL, NULL, NULL, NULL, 0, 'admin', '2023-05-20 00:00:00', 'admin', '2023-05-20 13:33:03', NULL);
INSERT INTO `base_config` VALUES ('1659756431177764865', '1659751716125786114', NULL, NULL, 1, 1, '面试', NULL, NULL, NULL, NULL, 0, 'admin', '2023-05-20 00:00:00', 'admin', '2023-05-20 13:33:25', NULL);
INSERT INTO `base_config` VALUES ('1659756523947380737', '1659751716125786114', NULL, NULL, 1, 1, '软考', NULL, NULL, NULL, NULL, 0, 'admin', '2023-05-20 00:00:00', 'admin', '2023-05-20 13:33:31', NULL);
INSERT INTO `base_config` VALUES ('1659756595963580417', '1659751716125786114', NULL, NULL, 1, 1, 'PPT模板', NULL, NULL, NULL, NULL, 0, 'admin', '2023-05-20 00:00:00', 'admin', '2023-05-20 13:33:38', NULL);
INSERT INTO `base_config` VALUES ('1659756675919597570', '1659751716125786114', NULL, NULL, 1, 1, '简历模板', NULL, NULL, NULL, NULL, 0, 'admin', '2023-05-20 00:00:00', 'admin', '2023-05-20 13:33:45', NULL);

-- ----------------------------
-- Table structure for base_custom
-- ----------------------------
DROP TABLE IF EXISTS `base_custom`;
CREATE TABLE `base_custom`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `user_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户ID',
  `source_type` tinyint(2) NULL DEFAULT NULL COMMENT '用户来源（1：记录；2：小程序）',
  `nickname` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '昵称',
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '真实姓名',
  `position` varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '职位',
  `work_info` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '工作',
  `character_info` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '性格',
  `hobby` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '爱好',
  `wx_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '微信昵称',
  `wx_number` varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '微信号码',
  `qq_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'QQ昵称',
  `qq_number` varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'QQ号码',
  `phone_number` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `scholl` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '学校',
  `related_contacts` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系人',
  `communicate_direction` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '沟通方向',
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '家庭住址',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `del_flag` int(1) NULL DEFAULT NULL COMMENT '删除状态',
  `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '基础信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of base_custom
-- ----------------------------
INSERT INTO `base_custom` VALUES ('1659792156765990913', NULL, 1, '1', NULL, NULL, '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', NULL, 1, 'admin', '2023-05-20 00:00:00', 'admin', '2023-05-20 13:24:33');
INSERT INTO `base_custom` VALUES ('1660117412768669697', NULL, 1, '1', '1', '1', '1', '1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'admin', '2023-05-21 10:56:49', NULL, NULL);

-- ----------------------------
-- Table structure for base_technician
-- ----------------------------
DROP TABLE IF EXISTS `base_technician`;
CREATE TABLE `base_technician`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `nickname` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '昵称',
  `wx_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '微信昵称',
  `wx_number` varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '微信号码',
  `qq_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'QQ昵称',
  `qq_number` varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'QQ号码',
  `phone_number` varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `technology_type` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '技术类型',
  `technology_level` tinyint(2) NULL DEFAULT NULL COMMENT '技术等级（1：优秀；2：高等；3：中等；4：低等）',
  `service_level` tinyint(2) NULL DEFAULT NULL COMMENT '服务等级（1：优秀；2：中等；3：差）',
  `recommend_flag` tinyint(2) NULL DEFAULT NULL COMMENT '推荐（1：推荐；2：考虑；3：不推荐）',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `del_flag` int(1) NULL DEFAULT NULL COMMENT '删除状态',
  `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '技术员信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of base_technician
-- ----------------------------
INSERT INTO `base_technician` VALUES ('1659791619479842817', NULL, '1', '1', '1', '1', '1', '10,9', 1, 1, 3, NULL, 0, 'admin', '2023-05-20 13:22:14', NULL, NULL);

-- ----------------------------
-- Table structure for biz_base_order
-- ----------------------------
DROP TABLE IF EXISTS `biz_base_order`;
CREATE TABLE `biz_base_order`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键id',
  `order_type` tinyint(2) NOT NULL COMMENT '订单类型（1：软件定制；2：环境搭建；3：远程调试；4：代码梳理；5：开发辅导）',
  `technician_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '技术员ID',
  `user_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户ID',
  `contact_group` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '沟通群组',
  `title` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标题',
  `technology_type` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '技术类型',
  `demand` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '需求',
  `start_time` datetime NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime NULL DEFAULT NULL COMMENT '结束时间',
  `duration` decimal(10, 2) NULL DEFAULT NULL COMMENT '工期',
  `price` decimal(10, 2) UNSIGNED ZEROFILL NULL DEFAULT NULL COMMENT '价格',
  `first_pay` decimal(10, 2) NULL DEFAULT NULL COMMENT '第一期支付金额',
  `second_pay` decimal(10, 2) NULL DEFAULT NULL COMMENT '第二期支付金额',
  `profit` decimal(10, 2) NULL DEFAULT NULL COMMENT '盈利',
  `order_status` tinyint(2) NULL DEFAULT NULL COMMENT '订单状态（1：未开始；2：进行中；3：已撤单；4：完成待付款；5：付款未评价；6：付款已评价）',
  `pay_status` tinyint(2) NULL DEFAULT NULL COMMENT '支付状态（1：未支付；2：已支付一期；3：全部已支付）',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '订单表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of biz_base_order
-- ----------------------------

-- ----------------------------
-- Table structure for biz_resource
-- ----------------------------
DROP TABLE IF EXISTS `biz_resource`;
CREATE TABLE `biz_resource`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键id',
  `link_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '链接名称',
  `link_level` tinyint(2) NULL DEFAULT NULL COMMENT '链接等级',
  `link_content` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '链接内容',
  `link_url` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '链接位置',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '资源表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of biz_resource
-- ----------------------------

-- ----------------------------
-- Table structure for biz_resource_order
-- ----------------------------
DROP TABLE IF EXISTS `biz_resource_order`;
CREATE TABLE `biz_resource_order`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键id',
  `order_type` tinyint(2) NOT NULL COMMENT '订单类型（1：源码；2：PPT模板；3：简历模板）',
  `user_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户ID',
  `resource_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '资源ID',
  `price` decimal(10, 2) NULL DEFAULT NULL COMMENT '价格',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '资源订单表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of biz_resource_order
-- ----------------------------
INSERT INTO `biz_resource_order` VALUES ('1660117478615048194', 1, '1660117412768669697', '1659828707168468994', 1.00, NULL, 'admin', '2023-05-21 10:57:05', NULL, NULL);

-- ----------------------------
-- Table structure for blog_article
-- ----------------------------
DROP TABLE IF EXISTS `blog_article`;
CREATE TABLE `blog_article`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键id',
  `title` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标题',
  `cover` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '封面',
  `abstract_info` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '摘要',
  `content` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '内容',
  `technology_type` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '技术类型',
  `income_type` tinyint(2) NULL DEFAULT NULL COMMENT '收益方式（0:无；1：打赏；2：星球币）',
  `template_flag` tinyint(2) NULL DEFAULT NULL COMMENT '是否模板（0：否；1：是）',
  `publish_status` tinyint(2) NULL DEFAULT NULL COMMENT '发布状态（0：否；1：是）',
  `price` decimal(10, 2) NULL DEFAULT NULL COMMENT '价格(星球币)',
  `resource_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '资源ID',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '文章表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of blog_article
-- ----------------------------
INSERT INTO `blog_article` VALUES ('1660080712214384641', '121', NULL, '12121', '<p>212121</p>\n<pre class=\"language-markup\"><code>212112121</code></pre>\n<p>&nbsp;</p>\n<p><em><strong>1212121</strong></em></p>', NULL, 0, 1, 0, 121.00, '1659828707168468994', NULL, 'admin', '2023-05-21 00:00:00', 'admin', '2023-05-21 08:52:24');
INSERT INTO `blog_article` VALUES ('1660084739320864770', '111', NULL, '11', '<p>11111</p>\n<p>&nbsp;</p>\n<p>&nbsp;</p>\n<p>&nbsp;</p>\n<p>&nbsp;</p>\n<p>&nbsp;</p>\n<p>&nbsp;</p>\n<p>&nbsp;</p>\n<p>&nbsp;</p>\n<p>&nbsp;</p>\n<p>&nbsp;</p>\n<p>&nbsp;</p>\n<p>&nbsp;</p>\n<p>&nbsp;</p>\n<p>1111</p>', NULL, 0, 1, 0, 1.00, '1659828707168468994', NULL, 'admin', '2023-05-21 00:00:00', 'admin', '2023-05-21 08:47:18');
INSERT INTO `blog_article` VALUES ('1660090986468237314', '111', NULL, '1', '<p>111</p>', NULL, 1, 1, 0, 1.00, '1659828707168468994', NULL, 'admin', '2023-05-21 00:00:00', 'articleAdmin', '2023-05-21 09:58:19');

-- ----------------------------
-- Table structure for blog_user_behavior
-- ----------------------------
DROP TABLE IF EXISTS `blog_user_behavior`;
CREATE TABLE `blog_user_behavior`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键id',
  `article_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文章ID',
  `user_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户ID',
  `behavior_type` tinyint(2) NOT NULL COMMENT '行为类型(1：点击；2：关注；3：点赞；4：收藏)',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `link_content` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '链接内容',
  `link_level` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '链接等级',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户行为表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of blog_user_behavior
-- ----------------------------

-- ----------------------------
-- Table structure for flyway_schema_history
-- ----------------------------
DROP TABLE IF EXISTS `flyway_schema_history`;
CREATE TABLE `flyway_schema_history`  (
  `installed_rank` int(11) NOT NULL,
  `version` varchar(50) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `description` varchar(200) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `type` varchar(20) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `script` varchar(1000) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `checksum` int(11) NULL DEFAULT NULL,
  `installed_by` varchar(100) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `installed_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `execution_time` int(11) NOT NULL,
  `success` tinyint(1) NOT NULL,
  PRIMARY KEY (`installed_rank`) USING BTREE,
  INDEX `flyway_schema_history_s_idx`(`success`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of flyway_schema_history
-- ----------------------------
INSERT INTO `flyway_schema_history` VALUES (1, '1', '<< Flyway Baseline >>', 'BASELINE', '<< Flyway Baseline >>', NULL, 'root', '2022-12-05 08:58:58', 0, 1);
INSERT INTO `flyway_schema_history` VALUES (2, '1.1', 'sys user', 'SQL', 'V1_1__sys_user.sql', 1434478002, 'root', '2022-12-05 08:58:58', 200, 1);

-- ----------------------------
-- Table structure for plan_mind
-- ----------------------------
DROP TABLE IF EXISTS `plan_mind`;
CREATE TABLE `plan_mind`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `task_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '任务ID',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `del_flag` int(1) NULL DEFAULT NULL COMMENT '删除状态',
  `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '基本思维导图表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of plan_mind
-- ----------------------------

-- ----------------------------
-- Table structure for plan_replay
-- ----------------------------
DROP TABLE IF EXISTS `plan_replay`;
CREATE TABLE `plan_replay`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `task_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '任务ID',
  `advantage` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '优势',
  `insufficient` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '不足',
  `improve` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '改进',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `del_flag` int(1) NULL DEFAULT NULL COMMENT '删除状态',
  `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '基础复盘表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of plan_replay
-- ----------------------------

-- ----------------------------
-- Table structure for plan_swot
-- ----------------------------
DROP TABLE IF EXISTS `plan_swot`;
CREATE TABLE `plan_swot`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `task_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '任务ID',
  `strengths` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '优势',
  `weaknesses` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '劣势',
  `opportunities` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '机会',
  `threats` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '威胁',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `del_flag` int(1) NULL DEFAULT NULL COMMENT '删除状态',
  `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '基础swot分析表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of plan_swot
-- ----------------------------

-- ----------------------------
-- Table structure for plan_task
-- ----------------------------
DROP TABLE IF EXISTS `plan_task`;
CREATE TABLE `plan_task`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `task_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '任务名称',
  `parent_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上级任务',
  `task_classification` int(1) NULL DEFAULT NULL COMMENT '任务分类',
  `task_type` int(1) NULL DEFAULT NULL COMMENT '任务类型（1：重要紧急；2：重要不紧急；3：紧急不重要；4：不紧急不重要）',
  `priority_level` int(1) NULL DEFAULT NULL COMMENT '优先级（1：低；2：中；3：高）',
  `start_time` datetime NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime NULL DEFAULT NULL COMMENT '结束时间',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `del_flag` int(1) NULL DEFAULT NULL COMMENT '删除状态',
  `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '基础任务表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of plan_task
-- ----------------------------

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键id',
  `username` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '登录账号',
  `realname` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '真实姓名',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密码',
  `salt` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'md5密码盐',
  `avatar` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '头像',
  `birthday` datetime NULL DEFAULT NULL COMMENT '生日',
  `sex` tinyint(1) NULL DEFAULT NULL COMMENT '性别(0-默认未知,1-男,2-女)',
  `email` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电子邮件',
  `phone` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电话',
  `org_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '登录会话的机构编码',
  `status` tinyint(1) NULL DEFAULT NULL COMMENT '性别(1-正常,2-冻结)',
  `del_flag` tinyint(1) NULL DEFAULT NULL COMMENT '删除状态(0-正常,1-已删除)',
  `third_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '第三方登录的唯一标识',
  `third_type` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '第三方类型',
  `activiti_sync` tinyint(1) NULL DEFAULT NULL COMMENT '同步工作流引擎(1-同步,0-不同步)',
  `work_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '工号，唯一键',
  `post` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '职务，关联职务表',
  `telephone` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '座机号',
  `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `user_identity` tinyint(1) NULL DEFAULT NULL COMMENT '身份（1普通成员 2上级）',
  `depart_ids` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '负责部门',
  `client_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备ID',
  `login_tenant_id` int(11) NULL DEFAULT NULL COMMENT '上次登录选择租户ID',
  `bpm_status` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '流程入职离职状态',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uniq_sys_user_work_no`(`work_no`) USING BTREE,
  UNIQUE INDEX `uniq_sys_user_username`(`username`) USING BTREE,
  UNIQUE INDEX `uniq_sys_user_phone`(`phone`) USING BTREE,
  UNIQUE INDEX `uniq_sys_user_email`(`email`) USING BTREE,
  INDEX `idx_su_username`(`username`) USING BTREE,
  INDEX `idx_su_status`(`status`) USING BTREE,
  INDEX `idx_su_del_flag`(`del_flag`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user
-- ----------------------------

-- ----------------------------
-- Table structure for sys_user_info
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_info`;
CREATE TABLE `sys_user_info`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键id',
  `username` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '登录账号',
  `realname` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '真实姓名',
  `birthday` datetime NULL DEFAULT NULL COMMENT '生日',
  `email` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电子邮件',
  `phone` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电话',
  `status` tinyint(1) NULL DEFAULT NULL COMMENT '性别(1-正常,2-冻结)',
  `del_flag` tinyint(1) NULL DEFAULT NULL COMMENT '删除状态(0-正常,1-已删除)',
  `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_info
-- ----------------------------

-- ----------------------------
-- Table structure for undo_log
-- ----------------------------
DROP TABLE IF EXISTS `undo_log`;
CREATE TABLE `undo_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `branch_id` bigint(20) NOT NULL,
  `xid` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `context` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `rollback_info` longblob NOT NULL,
  `log_status` int(11) NOT NULL,
  `log_created` datetime NOT NULL,
  `log_modified` datetime NOT NULL,
  `ext` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `ux_undo_log`(`xid`, `branch_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of undo_log
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
