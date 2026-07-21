-- ----------------------------
-- 菜单配置表
-- ----------------------------
DROP TABLE IF EXISTS sys_menu_config;
CREATE TABLE sys_menu_config (
  id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  menu_name VARCHAR(100) NOT NULL COMMENT '菜单名称',
  menu_code VARCHAR(50) NOT NULL COMMENT '菜单编码',
  menu_type TINYINT DEFAULT 1 COMMENT '菜单类型 [1:tabBar, 2:首页, 3:侧边栏]',
  parent_id BIGINT DEFAULT 0 COMMENT '父级ID',
  icon VARCHAR(255) DEFAULT NULL COMMENT '图标',
  selected_icon VARCHAR(255) DEFAULT NULL COMMENT '选中图标',
  path VARCHAR(255) DEFAULT NULL COMMENT '页面路径',
  url VARCHAR(255) DEFAULT NULL COMMENT '外部URL',
  sort INT DEFAULT 0 COMMENT '排序',
  is_show TINYINT DEFAULT 1 COMMENT '是否显示 [0:否, 1:是]',
  is_big TINYINT DEFAULT 0 COMMENT '是否凸起按钮 [0:否, 1:是]',
  big_icon VARCHAR(255) DEFAULT NULL COMMENT '凸起图标',
  big_type VARCHAR(50) DEFAULT NULL COMMENT '凸起类型',
  big_list VARCHAR(500) DEFAULT NULL COMMENT '凸起菜单列表(JSON)',
  render_type TINYINT DEFAULT 1 COMMENT '渲染类型 [1:动态列表, 2:固定表单, 3:指定界面]',
  render_config VARCHAR(500) DEFAULT NULL COMMENT '渲染配置(JSON)',
  permission_id BIGINT DEFAULT NULL COMMENT '权限ID',
  permission_code VARCHAR(100) DEFAULT NULL COMMENT '权限编码',
  create_by VARCHAR(32) DEFAULT NULL COMMENT '创建人',
  create_time DATETIME DEFAULT NULL COMMENT '创建时间',
  update_by VARCHAR(32) DEFAULT NULL COMMENT '更新人',
  update_time DATETIME DEFAULT NULL COMMENT '更新时间',
  del_flag TINYINT DEFAULT 0 COMMENT '删除标志 [0:否, 1:是]',
  PRIMARY KEY (id),
  UNIQUE KEY uk_menu_code (menu_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜单配置表';

-- ----------------------------
-- 初始化默认TabBar菜单
-- ----------------------------
INSERT INTO sys_menu_config (menu_name, menu_code, menu_type, icon, selected_icon, path, is_show, is_big, sort, render_type) VALUES
('首页', 'home', 1, 'static/images/tabbar/home.png', 'static/images/tabbar/home_s.png', '/pages/index/index', 1, 0, 1, 1),
('文章', 'article', 1, 'static/images/tabbar/news.png', 'static/images/tabbar/news_s.png', '/pages/news/news', 1, 0, 2, 1),
('我的', 'profile', 1, 'static/images/tabbar/user.png', 'static/images/tabbar/user_s.png', '/pages/user/user', 1, 0, 3, 1);

-- ----------------------------
-- 初始化带凸起按钮的TabBar菜单（中间凸起）
-- ----------------------------
-- INSERT INTO sys_menu_config (menu_name, menu_code, menu_type, icon, selected_icon, path, is_show, is_big, big_icon, big_type, sort, render_type) VALUES
-- ('首页', 'home', 1, 'static/images/tabbar/home.png', 'static/images/tabbar/home_s.png', '/pages/index/index', 1, 0, NULL, NULL, 1, 1),
-- ('发布', 'publish', 1, 'static/images/tabbar/add.png', 'static/images/tabbar/add.png', NULL, 1, 1, 'static/images/tabbar/add.png', 'popup', 2, 1),
-- ('文章', 'article', 1, 'static/images/tabbar/news.png', 'static/images/tabbar/news_s.png', '/pages/news/news', 1, 0, NULL, NULL, 3, 1),
-- ('我的', 'profile', 1, 'static/images/tabbar/user.png', 'static/images/tabbar/user_s.png', '/pages/user/user', 1, 0, NULL, NULL, 4, 1);

-- ----------------------------
-- 初始化首页菜单
-- ----------------------------
INSERT INTO sys_menu_config (menu_name, menu_code, menu_type, path, is_show, sort, render_type, render_config) VALUES
('客户管理', 'customer', 2, '/pages/customer/info', 1, 1, 1, NULL),
('我的收藏', 'collection', 2, '/pages/collection/collection', 1, 2, 1, NULL),
('联系客服', 'service', 2, '/pages/customer_service/customer_service', 1, 3, 1, NULL);

-- ----------------------------
-- 初始化带指定界面渲染的菜单（示例）
-- ----------------------------
-- INSERT INTO sys_menu_config (menu_name, menu_code, menu_type, path, is_show, sort, render_type, render_config) VALUES
-- ('订单列表', 'order', 2, '/pages/order/list', 1, 1, 3, '{"template":"list","fields":["orderNo","amount","status","createTime"]}');