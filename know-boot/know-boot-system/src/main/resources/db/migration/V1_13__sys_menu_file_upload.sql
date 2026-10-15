-- ============================================================
-- V1_13 知识库文件管理 —— 菜单与数据集
-- 模块: know-boot-knowledge
-- 说明: 幂等脚本，可重复执行。
--   * 父级菜单按 path='/knowledge' 动态定位（实测生产为 id=6011，
--     设计稿中的 id=20 与实际不符，故不硬编码 ID）
--   * 新增「文件管理」菜单（menu_type=2）+ 3 个按钮权限（menu_type=3）
--   * 授权给超级管理员角色 role_id=1
--   * 新增 UniApp 首页快捷入口 sys_menu_config(file-upload)
-- 关联设计: Ai-Design-V2/AI知识库菜单设计/kb_file_upload.sql
-- ============================================================
SET NAMES utf8mb4;

-- ------------------------------------------------------------
-- ① Vue 管理端：文件管理菜单（挂到「知识管理」下）
-- ------------------------------------------------------------
INSERT INTO `sys_menu`
  (`parent_id`, `menu_name`, `path`, `component`, `perms`, `icon`, `menu_type`, `sort`, `status`,
   `tenant_id`, `create_by`, `create_time`, `update_time`, `del_flag`)
SELECT p.id, '文件管理', '/knowledge/file', 'knowledge/file/index', 'knowledge:file:view', 'upload', 2,
       COALESCE((SELECT MAX(s.sort) FROM (SELECT sort FROM `sys_menu` WHERE parent_id = p.id) s), 0) + 1,
       1, NULL, 'system', NOW(), NOW(), 0
FROM (SELECT id FROM `sys_menu` WHERE parent_id = 0 AND path = '/knowledge' AND del_flag = 0 ORDER BY id LIMIT 1) p
WHERE NOT EXISTS (SELECT 1 FROM (SELECT id FROM `sys_menu` WHERE path = '/knowledge/file' AND del_flag = 0) x);

SET @file_menu_id = (SELECT id FROM (SELECT id FROM `sys_menu` WHERE path = '/knowledge/file' AND del_flag = 0 ORDER BY id DESC LIMIT 1) t);

-- ------------------------------------------------------------
-- ② 按钮权限（menu_type = 3）
-- ------------------------------------------------------------
INSERT INTO `sys_menu`
  (`parent_id`, `menu_name`, `path`, `component`, `perms`, `icon`, `menu_type`, `sort`, `status`,
   `tenant_id`, `create_by`, `create_time`, `update_time`, `del_flag`)
SELECT @file_menu_id, '文件上传', NULL, NULL, 'knowledge:file:upload', NULL, 3, 1, 1, NULL, 'system', NOW(), NOW(), 0
FROM DUAL
WHERE @file_menu_id IS NOT NULL
  AND NOT EXISTS (SELECT 1 FROM (SELECT id FROM `sys_menu` WHERE perms = 'knowledge:file:upload' AND del_flag = 0) t);

INSERT INTO `sys_menu`
  (`parent_id`, `menu_name`, `path`, `component`, `perms`, `icon`, `menu_type`, `sort`, `status`,
   `tenant_id`, `create_by`, `create_time`, `update_time`, `del_flag`)
SELECT @file_menu_id, '文件删除', NULL, NULL, 'knowledge:file:delete', NULL, 3, 2, 1, NULL, 'system', NOW(), NOW(), 0
FROM DUAL
WHERE @file_menu_id IS NOT NULL
  AND NOT EXISTS (SELECT 1 FROM (SELECT id FROM `sys_menu` WHERE perms = 'knowledge:file:delete' AND del_flag = 0) t);

INSERT INTO `sys_menu`
  (`parent_id`, `menu_name`, `path`, `component`, `perms`, `icon`, `menu_type`, `sort`, `status`,
   `tenant_id`, `create_by`, `create_time`, `update_time`, `del_flag`)
SELECT @file_menu_id, '文件解析', NULL, NULL, 'knowledge:file:parse', NULL, 3, 3, 1, NULL, 'system', NOW(), NOW(), 0
FROM DUAL
WHERE @file_menu_id IS NOT NULL
  AND NOT EXISTS (SELECT 1 FROM (SELECT id FROM `sys_menu` WHERE perms = 'knowledge:file:parse' AND del_flag = 0) t);

-- ------------------------------------------------------------
-- ③ 角色授权：超级管理员 role_id = 1
--    （sys_role_menu 唯一键 uk_role_menu(role_id, menu_id)）
-- ------------------------------------------------------------
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`, `create_by`, `create_time`, `del_flag`)
SELECT 1, m.id, 'system', NOW(), 0
FROM `sys_menu` m
WHERE (m.path = '/knowledge/file' OR m.perms LIKE 'knowledge:file:%')
  AND m.del_flag = 0
  AND NOT EXISTS (SELECT 1 FROM (SELECT role_id, menu_id FROM `sys_role_menu`) rm
                  WHERE rm.role_id = 1 AND rm.menu_id = m.id);

-- ------------------------------------------------------------
-- ④ UniApp 首页快捷入口（sys_menu_config, menu_code 唯一）
-- ------------------------------------------------------------
INSERT INTO `sys_menu_config`
  (`menu_name`, `menu_code`, `menu_type`, `icon`, `path`, `is_show`, `sort`, `render_type`, `render_config`, `create_time`)
SELECT '文件导入', 'file-upload', 2, '📤', '/pages/knowledge/file-upload/index', 1, 13, 1,
       '{"iconClass":"premium-icon-g9","title":"文件导入","desc":"上传 HTML/MD 快速入库并预览","tag":"导入","thumbBg":"linear-gradient(135deg,#ecfdf5,#d1fae5)","sections":["quick"]}',
       NOW()
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM (SELECT id FROM `sys_menu_config` WHERE menu_code = 'file-upload') t);
