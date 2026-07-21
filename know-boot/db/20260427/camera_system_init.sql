-- ===============================
-- 摄像头管理系统数据库设计
-- ===============================

-- ----------------------------
-- 摄像头设备表
-- ----------------------------
DROP TABLE IF EXISTS `camera_device`;
CREATE TABLE `camera_device` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `device_name` varchar(100) NOT NULL COMMENT '设备名称',
  `device_code` varchar(100) NOT NULL COMMENT '设备编号',
  `device_model` varchar(50) DEFAULT NULL COMMENT '设备型号',
  `manufacturer` varchar(50) DEFAULT NULL COMMENT '厂商',
  `ip_address` varchar(50) DEFAULT NULL COMMENT '局域网IP',
  `mac_address` varchar(50) DEFAULT NULL COMMENT 'MAC地址',
  `port` int DEFAULT 8080 COMMENT '端口',
  `username` varchar(50) DEFAULT NULL COMMENT '设备用户名',
  `password` varchar(100) DEFAULT NULL COMMENT '设备密码(加密存储)',
  `stream_url` varchar(200) DEFAULT NULL COMMENT '视频流地址',
  `snapshot_url` varchar(200) DEFAULT NULL COMMENT '快照地址',
  `status` tinyint DEFAULT 0 COMMENT '状态: 0-离线, 1-在线',
  `position` varchar(100) DEFAULT NULL COMMENT '安装位置',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `user_id` bigint NOT NULL COMMENT '所属用户',
  `create_by` bigint DEFAULT NULL COMMENT '创建人',
  `create_time` bigint DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint DEFAULT NULL COMMENT '更新人',
  `update_time` bigint DEFAULT NULL COMMENT '更新时间',
  `delete_time` bigint DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_device_code` (`device_code`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='摄像头设备表';

-- ----------------------------
-- 录像记录表
-- ----------------------------
DROP TABLE IF EXISTS `camera_record`;
CREATE TABLE `camera_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `device_id` bigint NOT NULL COMMENT '设备ID',
  `record_type` tinyint DEFAULT 1 COMMENT '录像类型: 1-手动, 2-定时, 3-移动侦测',
  `start_time` bigint NOT NULL COMMENT '录像开始时间',
  `end_time` bigint DEFAULT NULL COMMENT '录像结束时间',
  `duration` int DEFAULT 0 COMMENT '时长(秒)',
  `file_path` varchar(500) DEFAULT NULL COMMENT '文件路径',
  `file_size` bigint DEFAULT NULL COMMENT '文件大小(字节)',
  `cloud_url` varchar(500) DEFAULT NULL COMMENT '云存储地址',
  `status` tinyint DEFAULT 0 COMMENT '状态: 0-录制中, 1-已完成, 2-已上传',
  `create_by` bigint DEFAULT NULL COMMENT '创建人',
  `create_time` bigint DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_device_id` (`device_id`),
  KEY `idx_start_time` (`start_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='录像记录表';

-- ----------------------------
-- 截图记录表
-- ----------------------------
DROP TABLE IF EXISTS `camera_snapshot`;
CREATE TABLE `camera_snapshot` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `device_id` bigint NOT NULL COMMENT '设备ID',
  `capture_time` bigint NOT NULL COMMENT '截图时间',
  `file_path` varchar(500) DEFAULT NULL COMMENT '本地路径',
  `cloud_url` varchar(500) DEFAULT NULL COMMENT '云存储地址',
  `thumbnail` varchar(500) DEFAULT NULL COMMENT '缩略图路径',
  `create_by` bigint DEFAULT NULL COMMENT '创建人',
  `create_time` bigint DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_device_id` (`device_id`),
  KEY `idx_capture_time` (`capture_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='截图记录表';

-- ----------------------------
-- 设备收藏/常用表
-- ----------------------------
DROP TABLE IF EXISTS `camera_favorite`;
CREATE TABLE `camera_favorite` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `device_id` bigint NOT NULL COMMENT '设备ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `sort` int DEFAULT 0 COMMENT '排序',
  `create_time` bigint DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_device_user` (`device_id`, `user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='设备收藏表';

-- ===============================
-- 初始化数据
-- ===============================
-- 暂无初始化数据