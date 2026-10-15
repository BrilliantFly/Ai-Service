-- ============================================================
-- V1_12 知识库上传文件表 kb_uploaded_file
-- 模块: know-boot-knowledge
-- 说明: 幂等脚本（CREATE TABLE IF NOT EXISTS），可重复执行
-- 关联设计: Ai-Design-V2/AI知识库菜单设计/kb_file_upload.sql
-- ============================================================
SET NAMES utf8mb4;

CREATE TABLE IF NOT EXISTS `kb_uploaded_file` (
  `id`                 BIGINT        NOT NULL AUTO_INCREMENT  COMMENT '主键',
  `original_name`      VARCHAR(255)  NOT NULL                 COMMENT '原始文件名(用户上传时的名字)',
  `file_name`          VARCHAR(255)  NOT NULL                 COMMENT '存储文件名(UUID+ext,防重名)',
  `file_path`          VARCHAR(500)  NOT NULL                 COMMENT '文件存储相对路径(./upload/knowledge/...)',
  `file_ext`           VARCHAR(20)   NOT NULL                 COMMENT '扩展名: html / htm / md / markdown',
  `file_content`       LONGTEXT                               COMMENT '文件原始文本内容(UTF-8,上传时直接读取存库)',
  `file_size`          BIGINT        DEFAULT 0                COMMENT '文件大小(字节)',
  `mime_type`          VARCHAR(100)  DEFAULT NULL             COMMENT 'MIME 类型',
  `knowledge_base_id`  BIGINT        DEFAULT NULL             COMMENT '归属知识库ID(可空,表示未分类)',
  `document_id`        BIGINT        DEFAULT NULL             COMMENT '解析成功后对应的 kb_document.id',
  `parse_status`       TINYINT       DEFAULT 0                COMMENT '解析状态: 0待解析 1成功 2失败',
  `parse_error`        VARCHAR(1000) DEFAULT NULL             COMMENT '解析失败原因',
  `source_type`        VARCHAR(20)   DEFAULT 'upload'         COMMENT '来源类型: upload / import / system',
  `upload_platform`    VARCHAR(20)   DEFAULT NULL             COMMENT '上传端: vue / uniapp-h5 / uniapp-app / uniapp-mp',
  `sort`               INT           DEFAULT 0                COMMENT '排序',
  `status`             TINYINT       DEFAULT 1                COMMENT '状态: 0禁用 1正常',
  `create_by`          BIGINT        DEFAULT NULL             COMMENT '上传人ID',
  `create_time`        BIGINT        DEFAULT NULL             COMMENT '上传时间(毫秒时间戳)',
  `update_time`        BIGINT        DEFAULT NULL             COMMENT '更新时间(毫秒时间戳)',
  `delete_time`        BIGINT        DEFAULT 0                COMMENT '删除时间(毫秒,0=未删除)',
  PRIMARY KEY (`id`),
  KEY `idx_kb_id`       (`knowledge_base_id`),
  KEY `idx_doc_id`      (`document_id`),
  KEY `idx_create_by`   (`create_by`),
  KEY `idx_parse`       (`parse_status`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_source_type` (`source_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='知识库上传文件表(HTML/MD)';
