-- ===============================================
-- 首页配置管理数据库表结构
-- 创建时间: 2026-03-17
-- 模块: know-boot-plan
-- ===============================================

-- ----------------------------
-- 首页配置表
-- ----------------------------
DROP TABLE IF EXISTS `plan_home_config`;
CREATE TABLE `plan_home_config` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `config_type` varchar(50) NOT NULL COMMENT '配置类型(banner:轮播图 notice:通知 menu:滚动菜单 grid:九宫格)',
  `title` varchar(100) DEFAULT NULL COMMENT '标题',
  `content` text COMMENT '配置内容JSON',
  `icon` varchar(100) DEFAULT NULL COMMENT '图标',
  `link` varchar(200) DEFAULT NULL COMMENT '链接地址',
  `sort` int DEFAULT 0 COMMENT '排序',
  `status` tinyint DEFAULT 1 COMMENT '状态(0:禁用 1:启用)',
  `role_id` varchar(32) DEFAULT NULL COMMENT '角色ID(null表示全局)',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  INDEX `idx_config_type` (`config_type`),
  INDEX `idx_role_id` (`role_id`),
  INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='首页配置表';

-- ----------------------------
-- 预置默认配置数据
-- ----------------------------

-- 轮播图配置
INSERT INTO `plan_home_config` (`config_type`, `title`, `content`, `sort`, `status`) VALUES
('banner', '首页轮播图', '{"banners":[{"image":"","link":""}]}', 1, 1);

-- 通知配置
INSERT INTO `plan_home_config` (`config_type`, `title`, `content`, `sort`, `status`) VALUES
('notice', '通知公告', '{"notices":[{"id":1,"title":"欢迎使用日程计划管理系统"}]}', 2, 1);

-- 滚动菜单配置
INSERT INTO `plan_home_config` (`config_type`, `title`, `content`, `sort`, `status`) VALUES
('menu', '快捷菜单', '{"menus":[{"id":1,"name":"日程管理","icon":"calendar","path":"/pages/plan/schedule/index"},{"id":2,"name":"习惯打卡","icon":"flag","path":"/pages/plan/habit/index"}]}', 3, 1);

-- 九宫格菜单配置
INSERT INTO `plan_home_config` (`config_type`, `title`, `content`, `sort`, `status`) VALUES
('grid', '功能菜单', '{"grids":[{"id":1,"name":"日程管理","icon":"calendar","path":"/pages/plan/schedule/index"},{"id":2,"name":"习惯打卡","icon":"flag","path":"/pages/plan/habit/index"},{"id":3,"name":"四象限","icon":"pie-chart","path":"/pages/plan/schedule/index"},{"id":4,"name":"数据统计","icon":"bar-chart","path":"/pages/plan/habit/index"}]}', 4, 1);

-- ----------------------------
-- config_type 说明
-- ----------------------------
-- banner: 轮播图
-- notice: 通知公告
-- menu: 左右滚动菜单
-- grid: 九宫格菜单
-- ----------------------------

-- ----------------------------
-- content JSON结构示例
-- ----------------------------
-- 轮播图:
-- {"banners":[{"image":"图片URL","link":"跳转链接"}]}

-- 通知:
-- {"notices":[{"id":1,"title":"公告标题","content":"内容","createTime":"2026-03-17"}]}

-- 滚动菜单:
-- {"menus":[{"id":1,"name":"名称","icon":"图标","path":"路径"}]}

-- 九宫格:
-- {"grids":[{"id":1,"name":"名称","icon":"图标","path":"路径"}]}
