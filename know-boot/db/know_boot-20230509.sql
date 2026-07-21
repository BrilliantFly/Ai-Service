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

 Date: 09/05/2023 22:42:42
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
  `config_type` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '配置类型（1：菜单；2：导航）',
  `page_type` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '界面类型（1：首页；2：文章；3：资源；4：留言板；5：我的）',
  `title` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标题',
  `url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '路径',
  `content` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '内容',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `del_flag` int(1) NULL DEFAULT NULL COMMENT '删除状态',
  `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '基础配置表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of base_config
-- ----------------------------

-- ----------------------------
-- Table structure for base_information
-- ----------------------------
DROP TABLE IF EXISTS `base_information`;
CREATE TABLE `base_information`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `nickname` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '昵称',
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '真实姓名',
  `position` varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '职位',
  `work` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '工作',
  `character_info` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '性格',
  `hobby` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '爱好',
  `phone_number` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号',
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
-- Records of base_information
-- ----------------------------
INSERT INTO `base_information` VALUES ('1612658416534036482', 'zhangsan11', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, '2023-01-10 11:51:43', NULL, NULL);
INSERT INTO `base_information` VALUES ('1612658811549392898', 'zhangsan11', NULL, '111', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, '2023-01-10 11:53:17', NULL, NULL);
INSERT INTO `base_information` VALUES ('1612660250103480321', 'zhangsan11', NULL, '111', NULL, '11111', '11111', NULL, NULL, '11111', NULL, '111111', 0, NULL, '2023-01-10 11:59:00', NULL, NULL);
INSERT INTO `base_information` VALUES ('1612660301253017601', 'zhangsan11', NULL, '111', NULL, '11111', '11111', NULL, NULL, '11111', NULL, '111111', 0, NULL, '2023-01-10 11:59:12', NULL, NULL);
INSERT INTO `base_information` VALUES ('1612735436748193793', 'zhangsan11', NULL, '111', NULL, '11111', '11111', NULL, NULL, '11111', NULL, '111111', 0, NULL, '2023-01-10 16:57:46', NULL, NULL);
INSERT INTO `base_information` VALUES ('1612736370362515458', 'zhangsan11', NULL, '111', NULL, '11111', '11111', NULL, NULL, '11111', NULL, '111111', 0, NULL, '2023-01-10 17:01:29', NULL, NULL);
INSERT INTO `base_information` VALUES ('1612736599602200578', 'zhangsan11', NULL, '111', NULL, '11111', '11111', NULL, NULL, '11111', NULL, '111111', 0, NULL, '2023-01-10 17:02:23', NULL, NULL);
INSERT INTO `base_information` VALUES ('1612736761636552705', 'zhangsan11', NULL, '111', NULL, '11111', '11111', NULL, NULL, '11111', NULL, '111111', 0, NULL, '2023-01-10 17:03:02', NULL, NULL);
INSERT INTO `base_information` VALUES ('1612737508931510273', 'zhangsan11', NULL, '111', NULL, '11111', '11111', NULL, NULL, '11111', NULL, '111111', 0, NULL, '2023-01-10 17:06:00', NULL, NULL);
INSERT INTO `base_information` VALUES ('1612738487752028162', 'zhangsan11', NULL, '111', NULL, '11111', '11111', NULL, NULL, '11111', NULL, '111111', 0, NULL, '2023-01-10 17:09:53', NULL, NULL);
INSERT INTO `base_information` VALUES ('1612739023721091074', 'zhangsan11', NULL, '111', NULL, '11111', '11111', NULL, NULL, '11111', NULL, '111111', 0, NULL, '2023-01-10 17:12:01', NULL, NULL);
INSERT INTO `base_information` VALUES ('1612739451263275010', 'zhangsan11', NULL, '111', NULL, '11111', '11111', NULL, NULL, '11111', NULL, '111111', 0, NULL, '2023-01-10 17:13:43', NULL, NULL);
INSERT INTO `base_information` VALUES ('1612739622449598466', 'zhangsan11', NULL, '111', NULL, '11111', '11111', NULL, NULL, '11111', NULL, '111111', 0, NULL, '2023-01-10 17:14:24', NULL, NULL);
INSERT INTO `base_information` VALUES ('1612740061018607618', 'zhangsan11', NULL, '111', NULL, '11111', '11111', NULL, NULL, '11111', NULL, '111111', 0, NULL, '2023-01-10 17:16:08', NULL, NULL);
INSERT INTO `base_information` VALUES ('1612740161593823233', 'zhangsan11', NULL, '111', NULL, '11111', '11111', NULL, NULL, '11111', NULL, '111111', 0, NULL, '2023-01-10 17:16:32', NULL, NULL);
INSERT INTO `base_information` VALUES ('1612743814941761537', 'zhangsan11', NULL, '111', NULL, '11111', '11111', NULL, NULL, '11111', NULL, '111111', 0, NULL, '2023-01-10 17:31:03', NULL, NULL);
INSERT INTO `base_information` VALUES ('1612745233845420034', 'zhangsan11', NULL, '111', NULL, '11111', '11111', NULL, NULL, '11111', NULL, '111111', 0, NULL, '2023-01-10 17:36:42', NULL, NULL);
INSERT INTO `base_information` VALUES ('1612745304473305089', 'zhangsan11', NULL, '111', NULL, '11111', '11111', NULL, NULL, '11111', NULL, '111111', 0, NULL, '2023-01-10 17:36:59', NULL, NULL);
INSERT INTO `base_information` VALUES ('1612745333414002690', 'zhangsan11', NULL, '111', NULL, '11111', '11111', NULL, NULL, '11111', NULL, '111111', 0, NULL, '2023-01-10 17:37:06', NULL, NULL);
INSERT INTO `base_information` VALUES ('1612745640453832706', 'zhangsan11', NULL, '111', NULL, '11111', '11111', NULL, NULL, '11111', NULL, '111111', 0, NULL, '2023-01-10 17:38:19', NULL, NULL);
INSERT INTO `base_information` VALUES ('1612745927629438977', 'zhangsan11', NULL, '111', NULL, '11111', '11111', NULL, NULL, '11111', NULL, '111111', 0, NULL, '2023-01-10 17:39:27', NULL, NULL);
INSERT INTO `base_information` VALUES ('1613336719360184321', 'zhangsan11', NULL, '111', NULL, '11111', '11111', NULL, NULL, '11111', NULL, '111111', 0, NULL, '2023-01-12 08:47:03', NULL, NULL);
INSERT INTO `base_information` VALUES ('1613353735768903682', 'zhangsan11', NULL, '111', NULL, '11111', '11111', NULL, NULL, '11111', NULL, '111111', 0, NULL, '2023-01-12 09:54:40', NULL, NULL);
INSERT INTO `base_information` VALUES ('1613353868040474625', 'zhangsan11', NULL, '111', NULL, '11111', '11111', NULL, NULL, '11111', NULL, '111111', 0, NULL, '2023-01-12 09:55:11', NULL, NULL);
INSERT INTO `base_information` VALUES ('1613353896821788674', 'zhangsan11', NULL, '111', NULL, '11111', '11111', NULL, NULL, '11111', NULL, '111111', 0, NULL, '2023-01-12 09:55:18', NULL, NULL);
INSERT INTO `base_information` VALUES ('1613354171632705538', 'zhangsan11', NULL, '111', NULL, '11111', '11111', NULL, NULL, '11111', NULL, '111111', 0, NULL, '2023-01-12 09:56:24', NULL, NULL);
INSERT INTO `base_information` VALUES ('1613355122259124226', 'zhangsan11', NULL, '111', NULL, '11111', '11111', NULL, NULL, '11111', NULL, '111111', 0, NULL, '2023-01-12 10:00:10', NULL, NULL);
INSERT INTO `base_information` VALUES ('1613355396100984834', 'zhangsan11', NULL, '111', NULL, '11111', '11111', NULL, NULL, '11111', NULL, '111111', 0, NULL, '2023-01-12 10:01:16', NULL, NULL);
INSERT INTO `base_information` VALUES ('1613355491110359042', 'zhangsan11', NULL, '111', NULL, '11111', '11111', NULL, NULL, '11111', NULL, '111111', 0, NULL, '2023-01-12 10:01:38', NULL, NULL);
INSERT INTO `base_information` VALUES ('1613355686686560258', 'zhangsan11', NULL, '111', NULL, '11111', '11111', NULL, NULL, '11111', NULL, '111111', 0, NULL, '2023-01-12 10:02:25', NULL, NULL);
INSERT INTO `base_information` VALUES ('1613355930535043074', 'zhangsan11', NULL, '111', NULL, '11111', '11111', NULL, NULL, '11111', NULL, '111111', 0, NULL, '2023-01-12 10:03:23', NULL, NULL);
INSERT INTO `base_information` VALUES ('1613356011027931138', 'zhangsan11', NULL, '111', NULL, '11111', '11111', NULL, NULL, '11111', NULL, '111111', 0, NULL, '2023-01-12 10:03:42', NULL, NULL);
INSERT INTO `base_information` VALUES ('1613356028555927554', 'zhangsan11', NULL, '111', NULL, '11111', '11111', NULL, NULL, '11111', NULL, '111111', 0, NULL, '2023-01-12 10:03:47', NULL, NULL);
INSERT INTO `base_information` VALUES ('1613356036126646274', 'zhangsan11', NULL, '111', NULL, '11111', '11111', NULL, NULL, '11111', NULL, '111111', 0, NULL, '2023-01-12 10:03:48', NULL, NULL);
INSERT INTO `base_information` VALUES ('1613356042111918082', 'zhangsan11', NULL, '111', NULL, '11111', '11111', NULL, NULL, '11111', NULL, '111111', 0, NULL, '2023-01-12 10:03:50', NULL, NULL);
INSERT INTO `base_information` VALUES ('1613356141550477313', 'zhangsan11', NULL, '111', NULL, '11111', '11111', NULL, NULL, '11111', NULL, '111111', 0, NULL, '2023-01-12 10:04:14', NULL, NULL);
INSERT INTO `base_information` VALUES ('1613356149544820738', 'zhangsan11', NULL, '111', NULL, '11111', '11111', NULL, NULL, '11111', NULL, '111111', 0, NULL, '2023-01-12 10:04:15', NULL, NULL);
INSERT INTO `base_information` VALUES ('1613356154754146306', 'zhangsan11', NULL, '111', NULL, '11111', '11111', NULL, NULL, '11111', NULL, '111111', 0, NULL, '2023-01-12 10:04:17', NULL, NULL);
INSERT INTO `base_information` VALUES ('1613356158684209153', 'zhangsan11', NULL, '111', NULL, '11111', '11111', NULL, NULL, '11111', NULL, '111111', 0, NULL, '2023-01-12 10:04:18', NULL, NULL);
INSERT INTO `base_information` VALUES ('1613356162656215042', 'zhangsan11', NULL, '111', NULL, '11111', '11111', NULL, NULL, '11111', NULL, '111111', 0, NULL, '2023-01-12 10:04:19', NULL, NULL);
INSERT INTO `base_information` VALUES ('1613356171917238273', 'zhangsan11', NULL, '111', NULL, '11111', '11111', NULL, NULL, '11111', NULL, '111111', 0, NULL, '2023-01-12 10:04:21', NULL, NULL);
INSERT INTO `base_information` VALUES ('1613356290829950978', 'zhangsan11', NULL, '111', NULL, '11111', '11111', NULL, NULL, '11111', NULL, '111111', 0, NULL, '2023-01-12 10:04:49', NULL, NULL);
INSERT INTO `base_information` VALUES ('1613356345909551105', 'zhangsan11', NULL, '111', NULL, '11111', '11111', NULL, NULL, '11111', NULL, '111111', 0, NULL, '2023-01-12 10:05:02', NULL, NULL);
INSERT INTO `base_information` VALUES ('1613357951539441666', 'zhangsan11', NULL, '111', NULL, '11111', '11111', NULL, NULL, '11111', NULL, '111111', 0, NULL, '2023-01-12 10:11:25', NULL, NULL);
INSERT INTO `base_information` VALUES ('1613358243253284865', 'zhangsan11', NULL, '111', NULL, '11111', '11111', NULL, NULL, '11111', NULL, '111111', 0, NULL, '2023-01-12 10:12:35', NULL, NULL);
INSERT INTO `base_information` VALUES ('1613359120890343426', 'zhangsan11', NULL, '111', NULL, '11111', '11111', NULL, NULL, '11111', NULL, '111111', 0, NULL, '2023-01-12 10:16:04', NULL, NULL);
INSERT INTO `base_information` VALUES ('1613361338402168834', 'zhangsan11', NULL, '111', NULL, '11111', '11111', NULL, NULL, '11111', NULL, '111111', 0, NULL, '2023-01-12 10:24:53', NULL, NULL);
INSERT INTO `base_information` VALUES ('1613361924795252737', 'zhangsan11', NULL, '111', NULL, '11111', '11111', NULL, NULL, '11111', NULL, '111111', 0, NULL, '2023-01-12 10:27:12', NULL, NULL);
INSERT INTO `base_information` VALUES ('1613361948111388673', 'zhangsan11', NULL, '111', NULL, '11111', '11111', NULL, NULL, '11111', NULL, '111111', 0, NULL, '2023-01-12 10:27:18', NULL, NULL);
INSERT INTO `base_information` VALUES ('1613361953924694018', 'zhangsan11', NULL, '111', NULL, '11111', '11111', NULL, NULL, '11111', NULL, '111111', 0, NULL, '2023-01-12 10:27:19', NULL, NULL);
INSERT INTO `base_information` VALUES ('1613361963751948289', 'zhangsan11', NULL, '111', NULL, '11111', '11111', NULL, NULL, '11111', NULL, '111111', 0, NULL, '2023-01-12 10:27:22', NULL, NULL);
INSERT INTO `base_information` VALUES ('1613361969066131458', 'zhangsan11', NULL, '111', NULL, '11111', '11111', NULL, NULL, '11111', NULL, '111111', 0, NULL, '2023-01-12 10:27:23', NULL, NULL);
INSERT INTO `base_information` VALUES ('1613362408973123585', 'zhangsan11', NULL, '111', NULL, '11111', '11111', NULL, NULL, '11111', NULL, '111111', 0, NULL, '2023-01-12 10:29:08', NULL, NULL);
INSERT INTO `base_information` VALUES ('1613365295010783234', 'zhangsan11', NULL, '111', NULL, '11111', '11111', NULL, NULL, '11111', NULL, '111111', 0, NULL, '2023-01-12 10:40:36', NULL, NULL);
INSERT INTO `base_information` VALUES ('1613365460643848194', 'zhangsan11', NULL, '111', NULL, '11111', '11111', NULL, NULL, '11111', NULL, '111111', 0, NULL, '2023-01-12 10:41:15', NULL, NULL);
INSERT INTO `base_information` VALUES ('1613365524229496833', 'zhangsan11', NULL, '111', NULL, '11111', '11111', NULL, NULL, '11111', NULL, '111111', 0, NULL, '2023-01-12 10:41:31', NULL, NULL);
INSERT INTO `base_information` VALUES ('1613365531041046530', 'zhangsan11', NULL, '111', NULL, '11111', '11111', NULL, NULL, '11111', NULL, '111111', 0, NULL, '2023-01-12 10:41:32', NULL, NULL);
INSERT INTO `base_information` VALUES ('1613365537483497474', 'zhangsan11', NULL, '111', NULL, '11111', '11111', NULL, NULL, '11111', NULL, '111111', 0, NULL, '2023-01-12 10:41:34', NULL, NULL);
INSERT INTO `base_information` VALUES ('1613365546669023234', 'zhangsan11', NULL, '111', NULL, '11111', '11111', NULL, NULL, '11111', NULL, '111111', 0, NULL, '2023-01-12 10:41:36', NULL, NULL);
INSERT INTO `base_information` VALUES ('1613365554659172354', 'zhangsan11', NULL, '111', NULL, '11111', '11111', NULL, NULL, '11111', NULL, '111111', 0, NULL, '2023-01-12 10:41:38', NULL, NULL);
INSERT INTO `base_information` VALUES ('1613365562582212609', 'zhangsan11', NULL, '111', NULL, '11111', '11111', NULL, NULL, '11111', NULL, '111111', 0, NULL, '2023-01-12 10:41:40', NULL, NULL);
INSERT INTO `base_information` VALUES ('1613365570928877569', 'zhangsan11', NULL, '111', NULL, '11111', '11111', NULL, NULL, '11111', NULL, '111111', 0, NULL, '2023-01-12 10:41:42', NULL, NULL);
INSERT INTO `base_information` VALUES ('1613365579527200770', 'zhangsan11', NULL, '111', NULL, '11111', '11111', NULL, NULL, '11111', NULL, '111111', 0, NULL, '2023-01-12 10:41:44', NULL, NULL);
INSERT INTO `base_information` VALUES ('1613365585365671937', 'zhangsan11', NULL, '111', NULL, '11111', '11111', NULL, NULL, '11111', NULL, '111111', 0, NULL, '2023-01-12 10:41:45', NULL, NULL);
INSERT INTO `base_information` VALUES ('1613365590285590529', 'zhangsan11', NULL, '111', NULL, '11111', '11111', NULL, NULL, '11111', NULL, '111111', 0, NULL, '2023-01-12 10:41:46', NULL, NULL);
INSERT INTO `base_information` VALUES ('1613365599529836545', 'zhangsan11', NULL, '111', NULL, '11111', '11111', NULL, NULL, '11111', NULL, '111111', 0, NULL, '2023-01-12 10:41:48', NULL, NULL);

-- ----------------------------
-- Table structure for base_technician
-- ----------------------------
DROP TABLE IF EXISTS `base_technician`;
CREATE TABLE `base_technician`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
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

-- ----------------------------
-- Table structure for blog_article
-- ----------------------------
DROP TABLE IF EXISTS `blog_article`;
CREATE TABLE `blog_article`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键id',
  `title` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标题',
  `cover` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '封面',
  `abstract` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '摘要',
  `content` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '内容',
  `income_type` tinyint(2) NULL DEFAULT NULL COMMENT '收益方式（打赏、星球币、无）',
  `price` decimal(10, 2) NULL DEFAULT NULL COMMENT '价格(星球币)',
  `link_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '链接名称',
  `link_content` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '链接内容',
  `link_level` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '链接等级',
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
