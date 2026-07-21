-- ============================================
-- 摄像头设备模块 - 数据库脚本
-- 创建时间: 2026-03-20
-- ============================================

-- ----------------------------
-- 1. 摄像头设备表
-- ----------------------------
DROP TABLE IF EXISTS `camera_device`;
CREATE TABLE `camera_device` (
  `id` varchar(32) NOT NULL COMMENT '主键ID',
  `name` varchar(100) NOT NULL COMMENT '设备名称',
  `ip` varchar(50) NOT NULL COMMENT 'IP地址',
  `port` int DEFAULT 554 COMMENT 'RTSP端口',
  `http_port` int DEFAULT 80 COMMENT 'HTTP端口',
  `rtsp_url` varchar(255) DEFAULT NULL COMMENT 'RTSP完整地址',
  `username` varchar(100) DEFAULT NULL COMMENT '认证用户名',
  `password` varchar(100) DEFAULT NULL COMMENT '认证密码',
  `channel` int DEFAULT 1 COMMENT '通道号',
  `stream_type` varchar(20) DEFAULT 'main' COMMENT '码流类型(main主码流/sub子码流)',
  `manufacturer` varchar(50) DEFAULT NULL COMMENT '设备厂商',
  `model` varchar(50) DEFAULT NULL COMMENT '设备型号',
  `status` tinyint(1) DEFAULT 0 COMMENT '状态: 0-离线 1-在线',
  `wifi_ssid` varchar(100) DEFAULT NULL COMMENT '所属WiFi名称',
  `wifi_bssid` varchar(50) DEFAULT NULL COMMENT 'WiFi MAC地址',
  `thumbnail` varchar(255) DEFAULT NULL COMMENT '缩略图URL',
  `stream_url` varchar(255) DEFAULT NULL COMMENT '转流后的HLS地址',
  `last_online_time` datetime DEFAULT NULL COMMENT '最后在线时间',
  `last_stream_time` datetime DEFAULT NULL COMMENT '最后播放时间',
  `play_count` int DEFAULT 0 COMMENT '播放次数',
  `sort` int DEFAULT 0 COMMENT '排序',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_user` int DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_user` int DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  `deleted` tinyint(1) DEFAULT 0 COMMENT '是否删除: 0-否 1-是',
  PRIMARY KEY (`id`),
  KEY `idx_ip` (`ip`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='摄像头设备表';

-- ----------------------------
-- 2. 摄像头录像表
-- ----------------------------
DROP TABLE IF EXISTS `camera_record`;
CREATE TABLE `camera_record` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `device_id` varchar(32) NOT NULL COMMENT '设备ID',
  `file_name` varchar(255) NOT NULL COMMENT '文件名',
  `file_path` varchar(255) NOT NULL COMMENT '文件路径',
  `file_size` bigint DEFAULT 0 COMMENT '文件大小(字节)',
  `duration` int DEFAULT 0 COMMENT '时长(秒)',
  `start_time` int DEFAULT NULL COMMENT '开始时间戳',
  `end_time` int DEFAULT NULL COMMENT '结束时间戳',
  `record_type` varchar(20) DEFAULT 'manual' COMMENT '录像类型(manual手动/timing定时/motion移动侦测)',
  `status` tinyint(1) DEFAULT 1 COMMENT '状态(0生成中1已完成)',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_device_id` (`device_id`),
  KEY `idx_start_time` (`start_time`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='摄像头录像表';

-- ----------------------------
-- 3. 摄像头截图表
-- ----------------------------
DROP TABLE IF EXISTS `camera_screenshot`;
CREATE TABLE `camera_screenshot` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `device_id` varchar(32) NOT NULL COMMENT '设备ID',
  `file_path` varchar(255) NOT NULL COMMENT '文件路径',
  `file_size` bigint DEFAULT 0 COMMENT '文件大小',
  `snapshot_time` int DEFAULT NULL COMMENT '截图时间戳',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_device_id` (`device_id`),
  KEY `idx_snapshot_time` (`snapshot_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='摄像头截图表';

-- ----------------------------
-- 4. 摄像头配置表
-- ----------------------------
DROP TABLE IF EXISTS `camera_config`;
CREATE TABLE `camera_config` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `config_key` varchar(50) NOT NULL COMMENT '配置键',
  `config_value` varchar(500) DEFAULT NULL COMMENT '配置值',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_config_key` (`config_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='摄像头配置表';

-- ----------------------------
-- 5. 摄像头播放记录表
-- ----------------------------
DROP TABLE IF EXISTS `camera_play_log`;
CREATE TABLE `camera_play_log` (
    `id` varchar(32) NOT NULL COMMENT '主键ID',
    `camera_id` varchar(32) NOT NULL COMMENT '摄像头ID',
    `user_id` int DEFAULT NULL COMMENT '播放用户ID',
    `user_ip` varchar(50) DEFAULT NULL COMMENT '播放用户IP',
    `start_time` datetime NOT NULL COMMENT '开始播放时间',
    `end_time` datetime DEFAULT NULL COMMENT '结束播放时间',
    `duration` int DEFAULT 0 COMMENT '播放时长(秒)',
    `platform` varchar(20) DEFAULT NULL COMMENT '播放平台: web/app/uniapp',
    `client_info` varchar(255) DEFAULT NULL COMMENT '客户端信息',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_camera_id` (`camera_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_start_time` (`start_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='摄像头播放记录表';

-- ----------------------------
-- 6. 局域网设备发现记录表
-- ----------------------------
DROP TABLE IF EXISTS `camera_lan_device`;
CREATE TABLE `camera_lan_device` (
    `id` varchar(32) NOT NULL COMMENT '主键ID',
    `ip` varchar(50) NOT NULL COMMENT 'IP地址',
    `mac` varchar(50) DEFAULT NULL COMMENT 'MAC地址',
    `port` int DEFAULT NULL COMMENT '开放端口',
    `manufacturer` varchar(100) DEFAULT NULL COMMENT '设备厂商',
    `device_type` varchar(50) DEFAULT NULL COMMENT '设备类型: camera/router/other',
    `ssid` varchar(100) DEFAULT NULL COMMENT '发现的WiFi名称',
    `signal_strength` int DEFAULT NULL COMMENT '信号强度',
    `last_seen` datetime DEFAULT NULL COMMENT '最后发现时间',
    `scan_count` int DEFAULT 1 COMMENT '被扫描次数',
    `is_added` tinyint(1) DEFAULT 0 COMMENT '是否已添加为摄像头: 0-否 1-是',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_ip` (`ip`),
    KEY `idx_device_type` (`device_type`),
    KEY `idx_last_seen` (`last_seen`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='局域网设备发现记录表';

-- ----------------------------
-- 7. 初始化配置数据
-- ----------------------------
INSERT INTO `camera_config` (`config_key`, `config_value`, `remark`) VALUES
('storage_path', '/data/camera/record', '录像存储路径'),
('storage_max_size', '10737418240', '最大存储(10G)'),
('screenshot_path', '/data/camera/screenshot', '截图存储路径'),
('scan_timeout', '3000', '扫描超时(ms)'),
('stream_proxy_port', '8554', '流代理端口');

-- ----------------------------
-- 8. 初始化演示数据
-- ----------------------------
INSERT INTO `camera_device` (`id`, `name`, `ip`, `port`, `http_port`, `channel`, `stream_type`, `status`, `wifi_ssid`, `manufacturer`, `sort`, `create_time`) VALUES
('1', '客厅摄像头', '192.168.1.100', 554, 80, 1, 'main', 1, 'Home_WiFi', '海康威视', 1, NOW()),
('2', '门口摄像头', '192.168.1.101', 554, 80, 1, 'main', 1, 'Home_WiFi', '大华', 2, NOW()),
('3', '车库摄像头', '192.168.1.102', 554, 80, 1, 'main', 0, 'Home_WiFi', '萤石', 3, NOW());
