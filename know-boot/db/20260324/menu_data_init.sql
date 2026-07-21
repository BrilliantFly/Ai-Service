-- ============================================
-- 菜单数据初始化 - 基于Vue前端路由
-- 直接执行INSERT即可
-- ============================================

-- ----------------------------
-- 插入菜单数据
-- ----------------------------
-- 一级菜单
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `path`, `component`, `perms`, `icon`, `menu_type`, `sort`, `status`, `create_by`, `create_time`) VALUES
(1, 0, '首页', '/home', 'LAYOUT', NULL, 'home', 1, 1, 1, 'system', NOW()),
(2, 0, '表单', '/form', 'LAYOUT', NULL, 'form', 1, 2, 1, 'system', NOW()),
(3, 0, '表格', '/table', 'LAYOUT', NULL, 'table', 1, 3, 1, 'system', NOW()),
(4, 0, '图片', '/image', 'LAYOUT', NULL, 'image', 1, 4, 1, 'system', NOW()),
(5, 0, '视频', '/video', 'LAYOUT', NULL, 'video', 1, 5, 1, 'system', NOW()),
(6, 0, '组件', '/compo', 'LAYOUT', NULL, 'compo', 1, 6, 1, 'system', NOW()),
(7, 0, '编辑器', '/editor', 'LAYOUT', NULL, 'editor', 1, 7, 1, 'system', NOW()),
(8, 0, '流程', '/flow-editor', 'LAYOUT', NULL, 'flow', 1, 8, 1, 'system', NOW()),
(9, 0, '树形', '/tree', 'LAYOUT', NULL, 'tree', 1, 9, 1, 'system', NOW()),
(10, 0, '客户', '/customer', 'LAYOUT', NULL, 'user', 1, 10, 1, 'system', NOW()),
(11, 0, 'Excel', '/excel', 'LAYOUT', NULL, 'excel', 1, 10, 1, 'system', NOW()),
(12, 0, '异常', '/exception', 'LAYOUT', NULL, 'bug', 1, 11, 1, 'system', NOW()),
(13, 0, '外链', '/iframe', 'LAYOUT', NULL, 'computer', 1, 12, 1, 'system', NOW()),
(14, 0, '系统设置', '/system', 'LAYOUT', NULL, 'system', 1, 14, 1, 'system', NOW());

-- ----------------------------
-- 二级菜单 - 首页
-- ----------------------------
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `path`, `component`, `perms`, `icon`, `menu_type`, `sort`, `status`, `create_by`, `create_time`) VALUES
(101, 1, '首页', '/home', 'LAYOUT', NULL, 'home', 2, 1, 1, 'system', NOW());

-- ----------------------------
-- 二级菜单 - 表单
-- ----------------------------
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `path`, `component`, `perms`, `icon`, `menu_type`, `sort`, `status`, `create_by`, `create_time`) VALUES
(201, 2, '基础表单', '/form/basic-form', 'BasicForm', NULL, NULL, 2, 1, 1, 'system', NOW()),
(202, 2, '表单设计器', '/form/form-designer', 'FormDesigner', NULL, NULL, 2, 2, 1, 'system', NOW());

-- ----------------------------
-- 二级菜单 - 表格
-- ----------------------------
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `path`, `component`, `perms`, `icon`, `menu_type`, `sort`, `status`, `create_by`, `create_time`) VALUES
(301, 3, '基础表格', '/table/table-basic', 'TableBasic', NULL, NULL, 2, 1, 1, 'system', NOW()),
(302, 3, '可编辑表格', '/table/table-edit-row', 'TableEditRow', NULL, NULL, 2, 2, 1, 'system', NOW());

-- ----------------------------
-- 二级菜单 - 图片
-- ----------------------------
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `path`, `component`, `perms`, `icon`, `menu_type`, `sort`, `status`, `create_by`, `create_time`) VALUES
(401, 4, '图片裁剪', '/image/image-cropper', 'ImageCropper', NULL, NULL, 2, 1, 1, 'system', NOW()),
(402, 4, '图片压缩', '/image/image-compress', 'ImageCompress', NULL, NULL, 2, 2, 1, 'system', NOW()),
(403, 4, '图片合成', '/image/image-composition', 'ImageComposition', NULL, NULL, 2, 3, 1, 'system', NOW());

-- ----------------------------
-- 二级菜单 - 视频
-- ----------------------------
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `path`, `component`, `perms`, `icon`, `menu_type`, `sort`, `status`, `create_by`, `create_time`) VALUES
(501, 5, '视频播放器', '/video/video-player', 'VideoPlayer', NULL, NULL, 2, 1, 1, 'system', NOW()),
(502, 5, '视频水印', '/video/video-watermark', 'VideoWatermark', NULL, NULL, 2, 2, 1, 'system', NOW());

-- ----------------------------
-- 二级菜单 - 组件
-- ----------------------------
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `path`, `component`, `perms`, `icon`, `menu_type`, `sort`, `status`, `create_by`, `create_time`) VALUES
(601, 6, '图片上传', '/compo/image-upload', 'ImageUpload', NULL, NULL, 2, 1, 1, 'system', NOW()),
(602, 6, '拖拽', '/compo/drag', NULL, NULL, NULL, 1, 2, 1, 'system', NOW()),
(603, 6, '穿梭框', '/compo/transfer', 'TransferPage', NULL, NULL, 2, 3, 1, 'system', NOW()),
(604, 6, '数字动画', '/compo/count-to', 'CountToPage', NULL, NULL, 2, 4, 1, 'system', NOW());

-- ----------------------------
-- 三级菜单 - 拖拽子菜单
-- ----------------------------
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `path`, `component`, `perms`, `icon`, `menu_type`, `sort`, `status`, `create_by`, `create_time`) VALUES
(6021, 602, '拖拽列表', '/compo/drag/drag-list', 'DragList', NULL, NULL, 2, 1, 1, 'system', NOW()),
(6022, 602, '拖拽调整', '/compo/drag/drag-resize', 'VueDragResize', NULL, NULL, 2, 2, 1, 'system', NOW());

-- ----------------------------
-- 二级菜单 - 编辑器
-- ----------------------------
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `path`, `component`, `perms`, `icon`, `menu_type`, `sort`, `status`, `create_by`, `create_time`) VALUES
(701, 7, 'Markdown', '/editor/markdown', 'Markdown', NULL, NULL, 2, 1, 1, 'system', NOW()),
(702, 7, '富文本', '/editor/rich-text', 'RichText', NULL, NULL, 2, 2, 1, 'system', NOW()),
(703, 7, '代码编辑器', '/editor/code-editor', 'CodeEditor', NULL, NULL, 2, 3, 1, 'system', NOW());

-- ----------------------------
-- 二级菜单 - 流程
-- ----------------------------
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `path`, `component`, `perms`, `icon`, `menu_type`, `sort`, `status`, `create_by`, `create_time`) VALUES
(801, 8, '审批', '/flow-editor/flow-approve', 'FlowApprove', NULL, NULL, 2, 1, 1, 'system', NOW()),
(802, 8, 'BPMN', '/flow-editor/flow-bpmn', 'FlowBpmn', NULL, NULL, 2, 2, 1, 'system', NOW());

-- ----------------------------
-- 二级菜单 - 树形
-- ----------------------------
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `path`, `component`, `perms`, `icon`, `menu_type`, `sort`, `status`, `create_by`, `create_time`) VALUES
(901, 9, '组织架构树', '/tree/org-tree', 'OrgTree', NULL, NULL, 2, 1, 1, 'system', NOW()),
(902, 9, 'AntV树', '/tree/antd-tree', 'AntdTree', NULL, NULL, 2, 2, 1, 'system', NOW());

-- ----------------------------
-- 二级菜单 - 客户
-- ----------------------------
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `path`, `component`, `perms`, `icon`, `menu_type`, `sort`, `status`, `create_by`, `create_time`) VALUES
(1001, 10, '客户列表', '/customer/list', 'CustomerList', NULL, NULL, 2, 1, 1, 'system', NOW());

-- ----------------------------
-- 二级菜单 - Excel
-- ----------------------------
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `path`, `component`, `perms`, `icon`, `menu_type`, `sort`, `status`, `create_by`, `create_time`) VALUES
(1101, 11, '导出Excel', '/excel/export-excel', 'ExportExcel', NULL, NULL, 2, 1, 1, 'system', NOW()),
(1102, 11, '导入Excel', '/excel/import-excel', 'ImportExcel', NULL, NULL, 2, 2, 1, 'system', NOW());

-- ----------------------------
-- 二级菜单 - 异常
-- ----------------------------
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `path`, `component`, `perms`, `icon`, `menu_type`, `sort`, `status`, `create_by`, `create_time`) VALUES
(1201, 12, '403', '/exception/page-403', 'Page403', NULL, NULL, 2, 1, 1, 'system', NOW()),
(1202, 12, '404', '/exception/page-404', 'Page404', NULL, NULL, 2, 2, 1, 'system', NOW()),
(1203, 12, '500', '/exception/page-500', 'Page500', NULL, NULL, 2, 3, 1, 'system', NOW());

-- ----------------------------
-- 二级菜单 - 外链
-- ----------------------------
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `path`, `component`, `perms`, `icon`, `menu_type`, `sort`, `status`, `create_by`, `create_time`) VALUES
(1301, 13, 'Vue文档', '/iframe/vue-doc', NULL, NULL, NULL, 2, 1, 1, 'system', NOW()),
(1302, 13, 'Pinia文档', '/iframe/pinia-doc', NULL, NULL, NULL, 2, 2, 1, 'system', NOW()),
(1303, 13, 'AntD文档', '/iframe/antd-doc', NULL, NULL, NULL, 2, 3, 1, 'system', NOW());

-- ----------------------------
-- 二级菜单 - 系统设置
-- ----------------------------
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `path`, `component`, `perms`, `icon`, `menu_type`, `sort`, `status`, `create_by`, `create_time`) VALUES
(1401, 14, '用户管理', '/system/user', 'User', NULL, NULL, 2, 1, 1, 'system', NOW()),
(1402, 14, '角色管理', '/system/role', 'Role', NULL, NULL, 2, 2, 1, 'system', NOW()),
(1403, 14, '菜单管理', '/system/menu', 'Menu', NULL, NULL, 2, 3, 1, 'system', NOW()),
(1404, 14, '部门管理', '/system/dept', 'Dept', NULL, NULL, 2, 4, 1, 'system', NOW()),
(1405, 14, '岗位管理', '/system/job', 'Job', NULL, NULL, 2, 5, 1, 'system', NOW()),
(1406, 14, '字典管理', '/system/dict', 'Dict', NULL, NULL, 2, 6, 1, 'system', NOW()),
(1407, 14, '底部导航', '/system/tabbar', 'Tabbar', NULL, NULL, 2, 7, 1, 'system', NOW()),
(1408, 14, '摄像头管理', '/system/camera', 'Camera', NULL, NULL, 2, 8, 1, 'system', NOW()),
(1409, 14, 'WiFi管理', '/system/wifi', 'Wifi', NULL, NULL, 2, 9, 1, 'system', NOW()),
(1410, 14, '租户管理', '/system/tenant', 'Tenant', NULL, NULL, 2, 10, 1, 'system', NOW());
-- ----------------------------
SELECT '>>> 菜单数据 <<<' AS info;
SELECT id, parent_id, menu_name, path, menu_type, sort, status FROM sys_menu WHERE del_flag = 0 ORDER BY sort;