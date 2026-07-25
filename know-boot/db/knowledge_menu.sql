-- 知识库管理菜单初始化SQL
-- 用于在sys_menu表中添加知识库相关菜单

-- 1. 添加知识管理根菜单
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `path`, `component`, `perms`, `icon`, `menu_type`, `sort`, `status`, `create_by`, `create_time`)
VALUES (20, 0, '知识管理', '/knowledge', NULL, 'knowledge:view', 'book', 1, 15, 1, 'admin', UNIX_TIMESTAMP() * 1000)
ON DUPLICATE KEY UPDATE `menu_name` = '知识管理';

-- 2. 添加子菜单
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `path`, `component`, `perms`, `icon`, `menu_type`, `sort`, `status`, `create_by`, `create_time`)
VALUES
(2001, 20, '知识库管理', '/knowledge/base', 'knowledge/base', 'knowledge:base:view', 'database', 2, 1, 1, 'admin', UNIX_TIMESTAMP() * 1000),
(2002, 20, '文档管理', '/knowledge/document', 'knowledge/document', 'knowledge:document:view', 'file-text', 2, 2, 1, 'admin', UNIX_TIMESTAMP() * 1000),
(2003, 20, '标签管理', '/knowledge/tag', 'knowledge/tag', 'knowledge:tag:view', 'tags', 2, 3, 1, 'admin', UNIX_TIMESTAMP() * 1000),
(2004, 20, '小记管理', '/knowledge/quick-note', 'knowledge/quick-note', 'knowledge:quick-note:view', 'edit', 2, 4, 1, 'admin', UNIX_TIMESTAMP() * 1000)
ON DUPLICATE KEY UPDATE `menu_name` = VALUES(`menu_name`);

-- 3. 授权给超级管理员角色 (role_id=1)
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`, `create_by`, `create_time`)
SELECT 1, id, 'admin', UNIX_TIMESTAMP() * 1000 FROM `sys_menu` WHERE `id` >= 20 AND `id` < 21 AND `del_flag` = 0
ON DUPLICATE KEY UPDATE `create_by` = 'admin';

INSERT INTO `sys_role_menu` (`role_id`, `menu_id`, `create_by`, `create_time`)
SELECT 1, id, 'admin', UNIX_TIMESTAMP() * 1000 FROM `sys_menu` WHERE `id` >= 2001 AND `id` <= 2004 AND `del_flag` = 0
ON DUPLICATE KEY UPDATE `create_by` = 'admin';
