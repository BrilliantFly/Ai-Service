-- ----------------------------
-- 底部导航表
-- ----------------------------
DROP TABLE IF EXISTS `sys_tabbar`;
CREATE TABLE `sys_tabbar` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(50) NOT NULL COMMENT '导航名称',
  `selected` varchar(255) DEFAULT NULL COMMENT '选中图标',
  `unselected` varchar(255) DEFAULT NULL COMMENT '未选中图标',
  `link` text COMMENT '跳转配置JSON',
  `is_show` tinyint(1) DEFAULT 1 COMMENT '是否显示(0隐藏1显示)',
  `is_big` tinyint(1) DEFAULT 0 COMMENT '是否大按钮(0否1是)',
  `big_icon` varchar(255) DEFAULT NULL COMMENT '大按钮图标',
  `big_type` varchar(20) DEFAULT 'jump' COMMENT '大按钮类型(jump跳转/popup弹出)',
  `big_position` int DEFAULT 2 COMMENT '大按钮位置(默认居中)',
  `big_list` text COMMENT '弹出菜单JSON(大按钮为popup时使用)',
  `sort` int DEFAULT 0 COMMENT '排序',
  `create_time` int DEFAULT NULL COMMENT '创建时间',
  `update_time` int DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='底部导航表';

-- ----------------------------
-- 初始化默认数据
-- ----------------------------
INSERT INTO `sys_tabbar` (`id`, `name`, `selected`, `unselected`, `link`, `is_show`, `is_big`, `big_icon`, `big_type`, `big_position`, `big_list`, `sort`, `create_time`, `update_time`) VALUES
(1, '首页', '/static/images/tabbar/home_s.png', '/static/images/tabbar/home.png', '{"type":"shop","path":"/pages/index/index","canTab":true}', 1, 0, NULL, 'jump', 2, NULL, 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP()),
(2, '文章', '/static/images/tabbar/news_s.png', '/static/images/tabbar/news.png', '{"type":"custom","path":"/pages/news/news","canTab":true}', 1, 0, NULL, 'jump', 2, NULL, 2, UNIX_TIMESTAMP(), UNIX_TIMESTAMP()),
(3, '我的', '/static/images/tabbar/user_s.png', '/static/images/tabbar/user.png', '{"type":"shop","path":"/pages/user/user","canTab":true}', 1, 0, NULL, 'jump', 2, NULL, 3, UNIX_TIMESTAMP(), UNIX_TIMESTAMP());

-- ----------------------------
-- 样式配置记录(存储在base_config表)
-- ----------------------------
INSERT INTO `base_config` (`id`, `config_type`, `name`, `value`, `create_time`, `update_time`) VALUES
('tabbar_style', 'tabbar', 'style', '{"defaultColor":"#999999","selectedColor":"#1890ff"}', UNIX_TIMESTAMP(), UNIX_TIMESTAMP())
ON DUPLICATE KEY UPDATE `value` = '{"defaultColor":"#999999","selectedColor":"#1890ff"}';
