-- ============================================================
-- 知识库标签表（kb_tag）
-- 执行方式: mysql -h 101.37.83.88 -u root -p know_boot_v1 < kb_tag_create.sql
-- ============================================================

CREATE TABLE IF NOT EXISTS `kb_tag` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` VARCHAR(100) NOT NULL COMMENT '标签名称',
  `color` VARCHAR(20) DEFAULT NULL COMMENT '颜色',
  `sort` INT DEFAULT 0 COMMENT '排序',
  `create_time` BIGINT DEFAULT NULL COMMENT '创建时间',
  `update_time` BIGINT DEFAULT NULL COMMENT '更新时间',
  `delete_time` BIGINT DEFAULT 0 COMMENT '删除时间',
  PRIMARY KEY (`id`),
  KEY `idx_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='知识库标签';
