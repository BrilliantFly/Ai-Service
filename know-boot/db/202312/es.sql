/*
 Navicat Premium Data Transfer

 Source Server         : 123.207.61.17
 Source Server Type    : MySQL
 Source Server Version : 50742
 Source Host           : 123.207.61.17:3306
 Source Schema         : es

 Target Server Type    : MySQL
 Target Server Version : 50742
 File Encoding         : 65001

 Date: 24/12/2023 13:13:54
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for es_extra_main
-- ----------------------------
DROP TABLE IF EXISTS `es_extra_main`;
CREATE TABLE `es_extra_main`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `word` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '词',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否已删除',
  `update_time` timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of es_extra_main
-- ----------------------------
INSERT INTO `es_extra_main` VALUES (1, '易烊千玺', 0, '2023-03-10 16:10:51.427410');

-- ----------------------------
-- Table structure for es_extra_stopword
-- ----------------------------
DROP TABLE IF EXISTS `es_extra_stopword`;
CREATE TABLE `es_extra_stopword`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `word` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '词',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否已删除',
  `update_time` timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of es_extra_stopword
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
