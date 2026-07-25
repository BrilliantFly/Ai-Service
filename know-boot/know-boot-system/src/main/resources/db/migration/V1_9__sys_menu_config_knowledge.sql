-- ----------------------------
-- 知识库模块首页菜单配置
-- 菜单类型 2 = 首页/功能菜单（快捷功能网格）
-- ----------------------------

INSERT IGNORE INTO `sys_menu_config`
(`menu_name`, `menu_code`, `menu_type`, `icon`, `path`, `is_show`, `sort`, `render_type`, `render_config`, `create_time`)
VALUES
('知识库', 'knowledge', 2, '📚', '/pages/knowledge/list/index', 1, 10, 1,
 '{"iconClass":"premium-icon-g9","title":"知识库","desc":"分类整理你的笔记、文档与灵感","tag":"知识","thumbBg":"linear-gradient(135deg,#e0f2fe,#bae6fd)","sections":["quick"]}',
 NOW()),
('小记', 'memo', 2, '✏️', '/pages/knowledge/memo/index', 1, 11, 1,
 '{"iconClass":"premium-icon-g3","title":"快速小记","desc":"随时记录灵感与待办事项","tag":"笔记","thumbBg":"linear-gradient(135deg,#fce7f3,#fbcfe8)","sections":["quick"]}',
 NOW()),
('文档编辑', 'doc-edit', 2, '📝', '/pages/knowledge/document-edit/index', 1, 12, 1,
 '{"iconClass":"premium-icon-g2","title":"文档编辑","desc":"富文本编辑器，轻松创建与排版","tag":"编辑","thumbBg":"linear-gradient(135deg,#d1fae5,#a7f3d0)","sections":["tool"]}',
 NOW());
