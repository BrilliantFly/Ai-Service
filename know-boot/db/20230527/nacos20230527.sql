/*
 Navicat Premium Data Transfer

 Source Server         : 39.98.108.121
 Source Server Type    : MySQL
 Source Server Version : 50740
 Source Host           : 39.98.108.121:3306
 Source Schema         : nacos

 Target Server Type    : MySQL
 Target Server Version : 50740
 File Encoding         : 65001

 Date: 27/05/2023 23:35:24
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for config_info
-- ----------------------------
DROP TABLE IF EXISTS `config_info`;
CREATE TABLE `config_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'content',
  `md5` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT 'source user',
  `src_ip` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'source ip',
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '租户字段',
  `c_desc` varchar(256) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `c_use` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `effect` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `type` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `c_schema` text CHARACTER SET utf8 COLLATE utf8_bin NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfo_datagrouptenant`(`data_id`, `group_id`, `tenant_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 356 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'config_info' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of config_info
-- ----------------------------
INSERT INTO `config_info` VALUES (43, 'system-dev.yaml', 'DEFAULT_GROUP', 'spring:\r\n  #easypoi启用覆盖,否则一下错误\r\n  # A bean with that name has already been defined in class path resource\r\n  main:\r\n    allow-bean-definition-overriding: true\r\n  mvc:\r\n    pathmatch:\r\n      matching-strategy: ant_path_matcher\r\n  # 配置ehcache缓存\r\n  cache:\r\n    type: ehcache\r\n    ehcache:\r\n      config: classpath:/ehcache.xml\r\n  autoconfigure:\r\n    # 排除 Druid 自动配置\r\n    exclude: com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure\r\n  datasource:\r\n    #配置Druid数据源\r\n    type: com.alibaba.druid.pool.DruidDataSource\r\n    dynamic:\r\n      #设置默认的数据源或者数据源组,默认值即为master\r\n      primary: master\r\n      #严格匹配数据源,默认false. true未匹配到指定数据源时抛异常,false使用默认数据源\r\n      strict: false\r\n      datasource:\r\n        master:\r\n          driver-class-name: com.mysql.cj.jdbc.Driver\r\n          url: jdbc:mysql://39.98.108.121:3306/know_boot?useSSL=false&useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&transformedBitIsBoolean=true&tinyInt1isBit=false&allowMultiQueries=true&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai\r\n          username: root\r\n          password: Mysql@135269\r\n\r\n        slave:\r\n          driver-class-name: com.mysql.cj.jdbc.Driver\r\n          url: jdbc:mysql://39.98.108.121:3306/jeecg_boot?useSSL=false&useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&transformedBitIsBoolean=true&tinyInt1isBit=false&allowMultiQueries=true&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai\r\n          username: root\r\n          password: Mysql@135269\r\n\r\n    #Druid数据源专有配置，必须导入druid-spring-boot-starter才能生效，也可以通过DruidConfig注入生效\r\n    druid:\r\n      initial-size: 5\r\n      min-idle: 5\r\n      max-active: 20\r\n      max-wait: 60000\r\n      time-between-eviction-runs-millis: 60000\r\n      min-evictable-idle-time-millis: 30000\r\n      validation-query: SELECT 1 FROM DUAL\r\n      test-while-idle: true\r\n      test-on-borrow: false\r\n      test-on-return: false\r\n      pool-prepared-statements: true\r\n\r\n      #配置监控统计拦截的filters,stat:监控统计，log4j,日志记录，wall,防御sql注入\r\n      #如果允许时报错 java.lang.ClassNotFoundException,则导入log4j依赖即可\r\n      filters: stat,wall,log4j\r\n      max-pool-prepared-statement-per-connection-size: 20\r\n      use-global-data-source-stat: true\r\n      connect-properties: druid.stat.mergesql=true;druid.stat.slowSqlMills=500\r\n      #配置监控页面servlet的用户名和密码\r\n      #访问地址：http://localhost:9001/system/druid/login.html\r\n      stat-view-servlet:\r\n        enabled: true\r\n        login-username: admin\r\n        login-password: 123456\r\n        reset-enable: false\r\n      #配置监控页面filter过滤器\r\n      web-stat-filter:\r\n        enabled: true\r\n        url-pattern: /*\r\n        exclusions: \'*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*\'\r\n  redis:\r\n    #redis数据库索引(默认为0)\r\n    database: 0\r\n    #redis服务器地址\r\n    host: 39.98.108.121\r\n    #redis服务器连接端口\r\n    port: 10001\r\n    #redis连接密码\r\n    password: 123456789\r\n    lettuce:\r\n      pool:\r\n        # 连接池最大连接数（使用负值表示没有限制） 默认 8\r\n        max-active: 16\r\n        # 连接池最大阻塞等待时间（使用负值表示没有限制） 默认 -1\r\n        max-wait: 60000\r\n        # 连接池中的最大空闲连接 默认 8\r\n        max-idle: 10\r\n        # 连接池中的最小空闲连接 默认 0\r\n        min-idle: 3\r\n  rabbitmq:\r\n    host: 39.98.108.121\r\n    port: 5672\r\n    virtualHost: /pd\r\n    username: admin\r\n    password: admin\r\n    #这个配置是保证提供者确保消息推送到交换机中，不管成不成功，都会回调\r\n    publisher-confirm-type: correlated\r\n    #保证交换机能把消息推送到队列中\r\n    publisher-returns: true\r\n    virtual-host: /\r\n    #这个配置是保证消费者会消费消息，手动确认\r\n    listener:\r\n      simple:\r\n        acknowledge-mode: manual\r\n    template:\r\n      mandatory: true\r\n\r\n\r\n  # 数据库自动同步\r\n  flyway:\r\n    # 是否启用flyway\r\n    enabled: true\r\n    # 编码格式，默认UTF-8\r\n    encoding: UTF-8\r\n    # 迁移sql脚本文件存放路径，默认db/migration\r\n    locations: classpath:db/migration\r\n    # 迁移sql脚本文件名称的前缀，默认V\r\n    sql-migration-prefix: V\r\n    # 迁移sql脚本文件名称的分隔符，默认2个下划线__\r\n    sql-migration-separator: __\r\n    # 迁移sql脚本文件名称的后缀\r\n    sql-migration-suffixes: .sql\r\n    # 迁移时是否进行校验，默认true\r\n    validate-on-migrate: true\r\n    # 当迁移发现数据库非空且存在没有元数据的表时，自动执行基准迁移，新建schema_version表\r\n    baseline-on-migrate: true\r\n\r\n# mybatis plus 设置\r\nmybatis-plus:\r\n  mapper-locations: classpath:/mapper/**.xml\r\n  #  global-config:\r\n  #    banner: false\r\n  #    db-config:\r\n  #      logic-delete-value: 1 #删除后的状态 默认值1\r\n  #      logic-not-delete-value: 0 #逻辑前的值 默认值0\r\n  configuration:\r\n    # 控制台打印sql\r\n#    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl\r\n    # 返回类型为Map,显示null对应的字段\r\n    call-setters-on-nulls: true\r\n\r\nelasticsearch:\r\n  # es的链接地址\r\n  host: 127.0.0.1\r\n  # 端口号\r\n  port: 9200\r\n  # 账号、密码\r\n#  username: elastic\r\n#  password: 123456\r\n\r\n# 自动以starter\r\nknow:\r\n  generator:\r\n    driver-class-name: com.mysql.cj.jdbc.Driver\r\n    jdbc-url: jdbc:mysql://192.168.1.28:3306/data_test?useSSL=false&useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&transformedBitIsBoolean=true&tinyInt1isBit=false&allowMultiQueries=true&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai\r\n    user-name: root\r\n    password: Flyedt@2021\r\n    ignore-table: test\r\n    ignore-table-prefix: test_\r\n    ignore-table-suffix: _test\r\n    save-path: D:/DatabaseDocument/\r\n    version: 1.0.0\r\n    description: 自动生成SQL库文档\r\n  jwt:\r\n    secret: qnAqsQa7600vrTBcr1WB8P8dg4cbgS5i8LZGjWnpREL # 密钥\r\n    expiration: 30 # token 有效期（S)\r\n  #限流器QPS设置\r\n  rateLimiter:\r\n    all:\r\n      #每秒生成令牌个数\r\n      permitsPerSecond: 1\r\n      #在warmupPeriod时间内RateLimiter会增加它的速率，在抵达它的稳定速率或者最大速率之前\r\n      warmupPeriod: 1\r\n    resource:\r\n      permitsPerSecond: 1\r\n      warmupPeriod: 1\r\n    interface:\r\n      permitsPerSecond: 1\r\n      warmupPeriod: 1\r\n  ignore:\r\n    urls:\r\n      - ${server.servlet.context-path}/sys/user/**\r\n  redisson:\r\n    address: redis://${spring.redis.host}:${spring.redis.port}', 'd9105136f1d1fd92968d6d75f05e2af9', '2022-12-28 17:10:46', '2023-01-12 17:43:39', NULL, '221.3.20.86', '', 'ab16776b-dae9-474e-8943-7bbbc627b637', 'null', 'null', 'null', 'yaml', 'null');
INSERT INTO `config_info` VALUES (44, 'transport.type', 'SEATA_GROUP', 'TCP', 'b136ef5f6a01d816991fe3cf7a6ac763', '2022-12-29 11:38:24', '2022-12-29 11:38:24', NULL, '127.0.0.1', '', 'd8e3fcfc-2eb4-4e8a-b1ee-f3557901d307', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (45, 'transport.server', 'SEATA_GROUP', 'NIO', 'b6d9dfc0fb54277321cebc0fff55df2f', '2022-12-29 11:38:26', '2022-12-29 11:38:26', NULL, '127.0.0.1', '', 'd8e3fcfc-2eb4-4e8a-b1ee-f3557901d307', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (46, 'transport.heartbeat', 'SEATA_GROUP', 'true', 'b326b5062b2f0e69046810717534cb09', '2022-12-29 11:38:29', '2022-12-29 11:38:29', NULL, '127.0.0.1', '', 'd8e3fcfc-2eb4-4e8a-b1ee-f3557901d307', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (47, 'transport.thread-factory.boss-thread-prefix', 'SEATA_GROUP', 'NettyBoss', '0f8db59a3b7f2823f38a70c308361836', '2022-12-29 11:38:32', '2022-12-29 11:38:32', NULL, '127.0.0.1', '', 'd8e3fcfc-2eb4-4e8a-b1ee-f3557901d307', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (48, 'transport.thread-factory.worker-thread-prefix', 'SEATA_GROUP', 'NettyServerNIOWorker', 'a78ec7ef5d1631754c4e72ae8a3e9205', '2022-12-29 11:38:37', '2022-12-29 11:38:37', NULL, '127.0.0.1', '', 'd8e3fcfc-2eb4-4e8a-b1ee-f3557901d307', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (49, 'transport.thread-factory.server-executor-thread-prefix', 'SEATA_GROUP', 'NettyServerBizHandler', '11a36309f3d9df84fa8b59cf071fa2da', '2022-12-29 11:38:43', '2022-12-29 11:38:43', NULL, '127.0.0.1', '', 'd8e3fcfc-2eb4-4e8a-b1ee-f3557901d307', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (50, 'transport.thread-factory.share-boss-worker', 'SEATA_GROUP', 'false', '68934a3e9455fa72420237eb05902327', '2022-12-29 11:38:46', '2022-12-29 11:38:46', NULL, '127.0.0.1', '', 'd8e3fcfc-2eb4-4e8a-b1ee-f3557901d307', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (51, 'transport.thread-factory.client-selector-thread-prefix', 'SEATA_GROUP', 'NettyClientSelector', 'cd7ec5a06541e75f5a7913752322c3af', '2022-12-29 11:38:52', '2022-12-29 11:38:52', NULL, '127.0.0.1', '', 'd8e3fcfc-2eb4-4e8a-b1ee-f3557901d307', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (52, 'transport.thread-factory.client-selector-thread-size', 'SEATA_GROUP', '1', 'c4ca4238a0b923820dcc509a6f75849b', '2022-12-29 11:38:56', '2022-12-29 11:38:56', NULL, '127.0.0.1', '', 'd8e3fcfc-2eb4-4e8a-b1ee-f3557901d307', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (53, 'transport.thread-factory.client-worker-thread-prefix', 'SEATA_GROUP', 'NettyClientWorkerThread', '61cf4e69a56354cf72f46dc86414a57e', '2022-12-29 11:39:02', '2022-12-29 11:39:02', NULL, '127.0.0.1', '', 'd8e3fcfc-2eb4-4e8a-b1ee-f3557901d307', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (54, 'transport.thread-factory.boss-thread-size', 'SEATA_GROUP', '1', 'c4ca4238a0b923820dcc509a6f75849b', '2022-12-29 11:39:05', '2022-12-29 11:39:05', NULL, '127.0.0.1', '', 'd8e3fcfc-2eb4-4e8a-b1ee-f3557901d307', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (55, 'transport.thread-factory.worker-thread-size', 'SEATA_GROUP', '8', 'c9f0f895fb98ab9159f51fd0297e236d', '2022-12-29 11:39:09', '2022-12-29 11:39:09', NULL, '127.0.0.1', '', 'd8e3fcfc-2eb4-4e8a-b1ee-f3557901d307', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (56, 'transport.shutdown.wait', 'SEATA_GROUP', '3', 'eccbc87e4b5ce2fe28308fd9f2a7baf3', '2022-12-29 11:39:11', '2022-12-29 11:39:11', NULL, '127.0.0.1', '', 'd8e3fcfc-2eb4-4e8a-b1ee-f3557901d307', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (57, 'service.vgroup_mapping.my_test_group', 'SEATA_GROUP', 'default', 'c21f969b5f03d33d43e04f8f136e7682', '2022-12-29 11:39:15', '2022-12-29 11:39:15', NULL, '127.0.0.1', '', 'd8e3fcfc-2eb4-4e8a-b1ee-f3557901d307', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (58, 'service.enableDegrade', 'SEATA_GROUP', 'false', '68934a3e9455fa72420237eb05902327', '2022-12-29 11:39:17', '2022-12-29 11:39:17', NULL, '127.0.0.1', '', 'd8e3fcfc-2eb4-4e8a-b1ee-f3557901d307', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (59, 'service.disable', 'SEATA_GROUP', 'false', '68934a3e9455fa72420237eb05902327', '2022-12-29 11:39:19', '2022-12-29 11:39:19', NULL, '127.0.0.1', '', 'd8e3fcfc-2eb4-4e8a-b1ee-f3557901d307', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (60, 'service.max.commit.retry.timeout', 'SEATA_GROUP', '-1', '6bb61e3b7bce0931da574d19d1d82c88', '2022-12-29 11:39:21', '2022-12-29 11:39:21', NULL, '127.0.0.1', '', 'd8e3fcfc-2eb4-4e8a-b1ee-f3557901d307', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (61, 'service.max.rollback.retry.timeout', 'SEATA_GROUP', '-1', '6bb61e3b7bce0931da574d19d1d82c88', '2022-12-29 11:39:24', '2022-12-29 11:39:24', NULL, '127.0.0.1', '', 'd8e3fcfc-2eb4-4e8a-b1ee-f3557901d307', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (62, 'client.async.commit.buffer.limit', 'SEATA_GROUP', '10000', 'b7a782741f667201b54880c925faec4b', '2022-12-29 11:39:27', '2022-12-29 11:39:27', NULL, '127.0.0.1', '', 'd8e3fcfc-2eb4-4e8a-b1ee-f3557901d307', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (63, 'client.lock.retry.internal', 'SEATA_GROUP', '10', 'd3d9446802a44259755d38e6d163e820', '2022-12-29 11:39:30', '2022-12-29 11:39:30', NULL, '127.0.0.1', '', 'd8e3fcfc-2eb4-4e8a-b1ee-f3557901d307', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (64, 'client.lock.retry.times', 'SEATA_GROUP', '30', '34173cb38f07f89ddbebc2ac9128303f', '2022-12-29 11:39:32', '2022-12-29 11:39:32', NULL, '127.0.0.1', '', 'd8e3fcfc-2eb4-4e8a-b1ee-f3557901d307', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (65, 'client.lock.retry.policy.branch-rollback-on-conflict', 'SEATA_GROUP', 'true', 'b326b5062b2f0e69046810717534cb09', '2022-12-29 11:39:36', '2022-12-29 11:39:36', NULL, '127.0.0.1', '', 'd8e3fcfc-2eb4-4e8a-b1ee-f3557901d307', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (66, 'client.table.meta.check.enable', 'SEATA_GROUP', 'true', 'b326b5062b2f0e69046810717534cb09', '2022-12-29 11:39:39', '2022-12-29 11:39:39', NULL, '127.0.0.1', '', 'd8e3fcfc-2eb4-4e8a-b1ee-f3557901d307', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (67, 'client.report.retry.count', 'SEATA_GROUP', '5', 'e4da3b7fbbce2345d7772b0674a318d5', '2022-12-29 11:39:42', '2022-12-29 11:39:42', NULL, '127.0.0.1', '', 'd8e3fcfc-2eb4-4e8a-b1ee-f3557901d307', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (68, 'client.tm.commit.retry.count', 'SEATA_GROUP', '1', 'c4ca4238a0b923820dcc509a6f75849b', '2022-12-29 11:39:44', '2022-12-29 11:39:44', NULL, '127.0.0.1', '', 'd8e3fcfc-2eb4-4e8a-b1ee-f3557901d307', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (69, 'client.tm.rollback.retry.count', 'SEATA_GROUP', '1', 'c4ca4238a0b923820dcc509a6f75849b', '2022-12-29 11:39:47', '2022-12-29 11:39:47', NULL, '127.0.0.1', '', 'd8e3fcfc-2eb4-4e8a-b1ee-f3557901d307', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (70, 'store.mode', 'SEATA_GROUP', 'file', '8c7dd922ad47494fc02c388e12c00eac', '2022-12-29 11:39:49', '2022-12-29 11:39:49', NULL, '127.0.0.1', '', 'd8e3fcfc-2eb4-4e8a-b1ee-f3557901d307', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (71, 'store.file.dir', 'SEATA_GROUP', 'file_store/data', '6a8dec07c44c33a8a9247cba5710bbb2', '2022-12-29 11:39:52', '2022-12-29 11:39:52', NULL, '127.0.0.1', '', 'd8e3fcfc-2eb4-4e8a-b1ee-f3557901d307', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (72, 'store.file.max-branch-session-size', 'SEATA_GROUP', '16384', 'c76fe1d8e08462434d800487585be217', '2022-12-29 11:39:55', '2022-12-29 11:39:55', NULL, '127.0.0.1', '', 'd8e3fcfc-2eb4-4e8a-b1ee-f3557901d307', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (73, 'store.file.max-global-session-size', 'SEATA_GROUP', '512', '10a7cdd970fe135cf4f7bb55c0e3b59f', '2022-12-29 11:39:58', '2022-12-29 11:39:58', NULL, '127.0.0.1', '', 'd8e3fcfc-2eb4-4e8a-b1ee-f3557901d307', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (74, 'store.file.file-write-buffer-cache-size', 'SEATA_GROUP', '16384', 'c76fe1d8e08462434d800487585be217', '2022-12-29 11:40:01', '2022-12-29 11:40:01', NULL, '127.0.0.1', '', 'd8e3fcfc-2eb4-4e8a-b1ee-f3557901d307', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (75, 'store.file.flush-disk-mode', 'SEATA_GROUP', 'async', '0df93e34273b367bb63bad28c94c78d5', '2022-12-29 11:40:04', '2022-12-29 11:40:04', NULL, '127.0.0.1', '', 'd8e3fcfc-2eb4-4e8a-b1ee-f3557901d307', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (76, 'store.file.session.reload.read_size', 'SEATA_GROUP', '100', 'f899139df5e1059396431415e770c6dd', '2022-12-29 11:40:07', '2022-12-29 11:40:07', NULL, '127.0.0.1', '', 'd8e3fcfc-2eb4-4e8a-b1ee-f3557901d307', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (77, 'store.db.datasource', 'SEATA_GROUP', 'dbcp', '3a9f384fb40c59fbdc67024ee97d94b1', '2022-12-29 11:40:09', '2022-12-29 11:40:09', NULL, '127.0.0.1', '', 'd8e3fcfc-2eb4-4e8a-b1ee-f3557901d307', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (78, 'store.db.db-type', 'SEATA_GROUP', 'mysql', '81c3b080dad537de7e10e0987a4bf52e', '2022-12-29 11:40:11', '2022-12-29 11:40:11', NULL, '127.0.0.1', '', 'd8e3fcfc-2eb4-4e8a-b1ee-f3557901d307', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (79, 'store.db.driver-class-name', 'SEATA_GROUP', 'com.mysql.jdbc.Driver', '683cf0c3a5a56cec94dfac94ca16d760', '2022-12-29 11:40:14', '2022-12-29 11:40:14', NULL, '127.0.0.1', '', 'd8e3fcfc-2eb4-4e8a-b1ee-f3557901d307', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (80, 'store.db.url', 'SEATA_GROUP', 'jdbc:mysql://192.168.1.28:3306/seata?useUnicode=true', '1aef691be1298905acd89291c204ef55', '2022-12-29 11:40:19', '2022-12-29 11:40:19', NULL, '127.0.0.1', '', 'd8e3fcfc-2eb4-4e8a-b1ee-f3557901d307', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (81, 'store.db.user', 'SEATA_GROUP', 'root', '63a9f0ea7bb98050796b649e85481845', '2022-12-29 11:40:20', '2022-12-29 11:40:20', NULL, '127.0.0.1', '', 'd8e3fcfc-2eb4-4e8a-b1ee-f3557901d307', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (82, 'store.db.password', 'SEATA_GROUP', 'Flyedt@2021', '23f925ea5542ce543d4336cf7ffe9a6f', '2022-12-29 11:40:23', '2022-12-29 11:40:23', NULL, '127.0.0.1', '', 'd8e3fcfc-2eb4-4e8a-b1ee-f3557901d307', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (83, 'store.db.min-conn', 'SEATA_GROUP', '1', 'c4ca4238a0b923820dcc509a6f75849b', '2022-12-29 11:40:24', '2022-12-29 11:40:24', NULL, '127.0.0.1', '', 'd8e3fcfc-2eb4-4e8a-b1ee-f3557901d307', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (84, 'store.db.max-conn', 'SEATA_GROUP', '3', 'eccbc87e4b5ce2fe28308fd9f2a7baf3', '2022-12-29 11:40:26', '2022-12-29 11:40:26', NULL, '127.0.0.1', '', 'd8e3fcfc-2eb4-4e8a-b1ee-f3557901d307', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (85, 'store.db.global.table', 'SEATA_GROUP', 'global_table', '8b28fb6bb4c4f984df2709381f8eba2b', '2022-12-29 11:40:28', '2022-12-29 11:40:28', NULL, '127.0.0.1', '', 'd8e3fcfc-2eb4-4e8a-b1ee-f3557901d307', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (86, 'store.db.branch.table', 'SEATA_GROUP', 'branch_table', '54bcdac38cf62e103fe115bcf46a660c', '2022-12-29 11:40:31', '2022-12-29 11:40:31', NULL, '127.0.0.1', '', 'd8e3fcfc-2eb4-4e8a-b1ee-f3557901d307', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (87, 'store.db.query-limit', 'SEATA_GROUP', '100', 'f899139df5e1059396431415e770c6dd', '2022-12-29 11:40:33', '2022-12-29 11:40:33', NULL, '127.0.0.1', '', 'd8e3fcfc-2eb4-4e8a-b1ee-f3557901d307', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (88, 'store.db.lock-table', 'SEATA_GROUP', 'lock_table', '55e0cae3b6dc6696b768db90098b8f2f', '2022-12-29 11:40:35', '2022-12-29 11:40:35', NULL, '127.0.0.1', '', 'd8e3fcfc-2eb4-4e8a-b1ee-f3557901d307', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (89, 'recovery.committing-retry-period', 'SEATA_GROUP', '1000', 'a9b7ba70783b617e9998dc4dd82eb3c5', '2022-12-29 11:40:38', '2022-12-29 11:40:38', NULL, '127.0.0.1', '', 'd8e3fcfc-2eb4-4e8a-b1ee-f3557901d307', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (90, 'recovery.asyn-committing-retry-period', 'SEATA_GROUP', '1000', 'a9b7ba70783b617e9998dc4dd82eb3c5', '2022-12-29 11:40:42', '2022-12-29 11:40:42', NULL, '127.0.0.1', '', 'd8e3fcfc-2eb4-4e8a-b1ee-f3557901d307', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (91, 'recovery.rollbacking-retry-period', 'SEATA_GROUP', '1000', 'a9b7ba70783b617e9998dc4dd82eb3c5', '2022-12-29 11:40:45', '2022-12-29 11:40:45', NULL, '127.0.0.1', '', 'd8e3fcfc-2eb4-4e8a-b1ee-f3557901d307', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (92, 'recovery.timeout-retry-period', 'SEATA_GROUP', '1000', 'a9b7ba70783b617e9998dc4dd82eb3c5', '2022-12-29 11:40:48', '2022-12-29 11:40:48', NULL, '127.0.0.1', '', 'd8e3fcfc-2eb4-4e8a-b1ee-f3557901d307', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (93, 'transaction.undo.data.validation', 'SEATA_GROUP', 'true', 'b326b5062b2f0e69046810717534cb09', '2022-12-29 11:40:51', '2022-12-29 11:40:51', NULL, '127.0.0.1', '', 'd8e3fcfc-2eb4-4e8a-b1ee-f3557901d307', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (94, 'transaction.undo.log.serialization', 'SEATA_GROUP', 'jackson', 'b41779690b83f182acc67d6388c7bac9', '2022-12-29 11:40:54', '2022-12-29 11:40:54', NULL, '127.0.0.1', '', 'd8e3fcfc-2eb4-4e8a-b1ee-f3557901d307', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (95, 'transaction.undo.log.save.days', 'SEATA_GROUP', '7', '8f14e45fceea167a5a36dedd4bea2543', '2022-12-29 11:40:56', '2022-12-29 11:40:56', NULL, '127.0.0.1', '', 'd8e3fcfc-2eb4-4e8a-b1ee-f3557901d307', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (96, 'transaction.undo.log.delete.period', 'SEATA_GROUP', '86400000', 'f4c122804fe9076cb2710f55c3c6e346', '2022-12-29 11:40:59', '2022-12-29 11:40:59', NULL, '127.0.0.1', '', 'd8e3fcfc-2eb4-4e8a-b1ee-f3557901d307', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (97, 'transaction.undo.log.table', 'SEATA_GROUP', 'undo_log', '2842d229c24afe9e61437135e8306614', '2022-12-29 11:41:02', '2022-12-29 11:41:02', NULL, '127.0.0.1', '', 'd8e3fcfc-2eb4-4e8a-b1ee-f3557901d307', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (98, 'transport.serialization', 'SEATA_GROUP', 'seata', 'b943081c423b9a5416a706524ee05d40', '2022-12-29 11:41:05', '2022-12-29 11:41:05', NULL, '127.0.0.1', '', 'd8e3fcfc-2eb4-4e8a-b1ee-f3557901d307', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (99, 'transport.compressor', 'SEATA_GROUP', 'none', '334c4a4c42fdb79d7ebc3e73b517e6f8', '2022-12-29 11:41:07', '2022-12-29 11:41:07', NULL, '127.0.0.1', '', 'd8e3fcfc-2eb4-4e8a-b1ee-f3557901d307', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (100, 'metrics.enabled', 'SEATA_GROUP', 'false', '68934a3e9455fa72420237eb05902327', '2022-12-29 11:41:09', '2022-12-29 11:41:09', NULL, '127.0.0.1', '', 'd8e3fcfc-2eb4-4e8a-b1ee-f3557901d307', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (101, 'metrics.registry-type', 'SEATA_GROUP', 'compact', '7cf74ca49c304df8150205fc915cd465', '2022-12-29 11:41:11', '2022-12-29 11:41:11', NULL, '127.0.0.1', '', 'd8e3fcfc-2eb4-4e8a-b1ee-f3557901d307', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (102, 'metrics.exporter-list', 'SEATA_GROUP', 'prometheus', 'e4f00638b8a10e6994e67af2f832d51c', '2022-12-29 11:41:14', '2022-12-29 11:41:14', NULL, '127.0.0.1', '', 'd8e3fcfc-2eb4-4e8a-b1ee-f3557901d307', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (103, 'metrics.exporter-prometheus-port', 'SEATA_GROUP', '9898', '7b9dc501afe4ee11c56a4831e20cee71', '2022-12-29 11:41:16', '2022-12-29 11:41:16', NULL, '127.0.0.1', '', 'd8e3fcfc-2eb4-4e8a-b1ee-f3557901d307', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (104, 'support.spring.datasource.autoproxy', 'SEATA_GROUP', 'false', '68934a3e9455fa72420237eb05902327', '2022-12-29 11:41:20', '2022-12-29 11:41:20', NULL, '127.0.0.1', '', 'd8e3fcfc-2eb4-4e8a-b1ee-f3557901d307', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (106, 'nacos.yaml', 'DEFAULT_GROUP', '1', 'c4ca4238a0b923820dcc509a6f75849b', '2022-12-29 15:57:34', '2022-12-29 15:57:34', NULL, '0:0:0:0:0:0:0:1', '', 'a1b8a687-12bd-4f36-8b2f-c93cc521c5c5', NULL, NULL, NULL, 'yaml', NULL);
INSERT INTO `config_info` VALUES (109, 'transport.type', 'SEATA_GROUP', 'TCP', 'b136ef5f6a01d816991fe3cf7a6ac763', '2022-12-30 10:00:59', '2022-12-30 10:00:59', NULL, '127.0.0.1', '', '9936aca9-bd06-4f2b-9b8d-ed67cc5b1e94', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (110, 'transport.server', 'SEATA_GROUP', 'NIO', 'b6d9dfc0fb54277321cebc0fff55df2f', '2022-12-30 10:00:59', '2022-12-30 10:00:59', NULL, '127.0.0.1', '', '9936aca9-bd06-4f2b-9b8d-ed67cc5b1e94', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (111, 'transport.heartbeat', 'SEATA_GROUP', 'true', 'b326b5062b2f0e69046810717534cb09', '2022-12-30 10:00:59', '2022-12-30 10:00:59', NULL, '127.0.0.1', '', '9936aca9-bd06-4f2b-9b8d-ed67cc5b1e94', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (112, 'transport.enableClientBatchSendRequest', 'SEATA_GROUP', 'false', '68934a3e9455fa72420237eb05902327', '2022-12-30 10:00:59', '2022-12-30 10:00:59', NULL, '127.0.0.1', '', '9936aca9-bd06-4f2b-9b8d-ed67cc5b1e94', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (113, 'transport.threadFactory.bossThreadPrefix', 'SEATA_GROUP', 'NettyBoss', '0f8db59a3b7f2823f38a70c308361836', '2022-12-30 10:00:59', '2022-12-30 10:00:59', NULL, '127.0.0.1', '', '9936aca9-bd06-4f2b-9b8d-ed67cc5b1e94', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (114, 'transport.threadFactory.workerThreadPrefix', 'SEATA_GROUP', 'NettyServerNIOWorker', 'a78ec7ef5d1631754c4e72ae8a3e9205', '2022-12-30 10:00:59', '2022-12-30 10:00:59', NULL, '127.0.0.1', '', '9936aca9-bd06-4f2b-9b8d-ed67cc5b1e94', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (115, 'transport.threadFactory.serverExecutorThreadPrefix', 'SEATA_GROUP', 'NettyServerBizHandler', '11a36309f3d9df84fa8b59cf071fa2da', '2022-12-30 10:00:59', '2022-12-30 10:00:59', NULL, '127.0.0.1', '', '9936aca9-bd06-4f2b-9b8d-ed67cc5b1e94', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (116, 'transport.threadFactory.shareBossWorker', 'SEATA_GROUP', 'false', '68934a3e9455fa72420237eb05902327', '2022-12-30 10:00:59', '2022-12-30 10:00:59', NULL, '127.0.0.1', '', '9936aca9-bd06-4f2b-9b8d-ed67cc5b1e94', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (117, 'transport.threadFactory.clientSelectorThreadPrefix', 'SEATA_GROUP', 'NettyClientSelector', 'cd7ec5a06541e75f5a7913752322c3af', '2022-12-30 10:00:59', '2022-12-30 10:00:59', NULL, '127.0.0.1', '', '9936aca9-bd06-4f2b-9b8d-ed67cc5b1e94', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (118, 'transport.threadFactory.clientSelectorThreadSize', 'SEATA_GROUP', '1', 'c4ca4238a0b923820dcc509a6f75849b', '2022-12-30 10:00:59', '2022-12-30 10:00:59', NULL, '127.0.0.1', '', '9936aca9-bd06-4f2b-9b8d-ed67cc5b1e94', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (119, 'transport.threadFactory.clientWorkerThreadPrefix', 'SEATA_GROUP', 'NettyClientWorkerThread', '61cf4e69a56354cf72f46dc86414a57e', '2022-12-30 10:00:59', '2022-12-30 10:00:59', NULL, '127.0.0.1', '', '9936aca9-bd06-4f2b-9b8d-ed67cc5b1e94', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (120, 'transport.threadFactory.bossThreadSize', 'SEATA_GROUP', '1', 'c4ca4238a0b923820dcc509a6f75849b', '2022-12-30 10:00:59', '2022-12-30 10:00:59', NULL, '127.0.0.1', '', '9936aca9-bd06-4f2b-9b8d-ed67cc5b1e94', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (121, 'transport.threadFactory.workerThreadSize', 'SEATA_GROUP', 'default', 'c21f969b5f03d33d43e04f8f136e7682', '2022-12-30 10:00:59', '2022-12-30 10:00:59', NULL, '127.0.0.1', '', '9936aca9-bd06-4f2b-9b8d-ed67cc5b1e94', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (122, 'transport.shutdown.wait', 'SEATA_GROUP', '3', 'eccbc87e4b5ce2fe28308fd9f2a7baf3', '2022-12-30 10:00:59', '2022-12-30 10:00:59', NULL, '127.0.0.1', '', '9936aca9-bd06-4f2b-9b8d-ed67cc5b1e94', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (123, 'service.vgroupMapping.springcloud_tx_group', 'SEATA_GROUP', 'default', 'c21f969b5f03d33d43e04f8f136e7682', '2022-12-30 10:00:59', '2022-12-30 10:00:59', NULL, '127.0.0.1', '', '9936aca9-bd06-4f2b-9b8d-ed67cc5b1e94', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (124, 'service.default.grouplist', 'SEATA_GROUP', '127.0.0.1:8091', 'c32ce0d3e264525dcdada751f98143a3', '2022-12-30 10:00:59', '2022-12-30 10:00:59', NULL, '127.0.0.1', '', '9936aca9-bd06-4f2b-9b8d-ed67cc5b1e94', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (125, 'service.enableDegrade', 'SEATA_GROUP', 'false', '68934a3e9455fa72420237eb05902327', '2022-12-30 10:00:59', '2022-12-30 10:00:59', NULL, '127.0.0.1', '', '9936aca9-bd06-4f2b-9b8d-ed67cc5b1e94', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (126, 'service.disableGlobalTransaction', 'SEATA_GROUP', 'false', '68934a3e9455fa72420237eb05902327', '2022-12-30 10:00:59', '2022-12-30 10:00:59', NULL, '127.0.0.1', '', '9936aca9-bd06-4f2b-9b8d-ed67cc5b1e94', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (127, 'client.rm.asyncCommitBufferLimit', 'SEATA_GROUP', '10000', 'b7a782741f667201b54880c925faec4b', '2022-12-30 10:00:59', '2022-12-30 10:00:59', NULL, '127.0.0.1', '', '9936aca9-bd06-4f2b-9b8d-ed67cc5b1e94', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (128, 'client.rm.lock.retryInterval', 'SEATA_GROUP', '10', 'd3d9446802a44259755d38e6d163e820', '2022-12-30 10:00:59', '2022-12-30 10:00:59', NULL, '127.0.0.1', '', '9936aca9-bd06-4f2b-9b8d-ed67cc5b1e94', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (129, 'client.rm.lock.retryTimes', 'SEATA_GROUP', '30', '34173cb38f07f89ddbebc2ac9128303f', '2022-12-30 10:00:59', '2022-12-30 10:00:59', NULL, '127.0.0.1', '', '9936aca9-bd06-4f2b-9b8d-ed67cc5b1e94', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (130, 'client.rm.lock.retryPolicyBranchRollbackOnConflict', 'SEATA_GROUP', 'true', 'b326b5062b2f0e69046810717534cb09', '2022-12-30 10:00:59', '2022-12-30 10:00:59', NULL, '127.0.0.1', '', '9936aca9-bd06-4f2b-9b8d-ed67cc5b1e94', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (131, 'client.rm.reportRetryCount', 'SEATA_GROUP', '5', 'e4da3b7fbbce2345d7772b0674a318d5', '2022-12-30 10:00:59', '2022-12-30 10:00:59', NULL, '127.0.0.1', '', '9936aca9-bd06-4f2b-9b8d-ed67cc5b1e94', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (132, 'client.rm.tableMetaCheckEnable', 'SEATA_GROUP', 'false', '68934a3e9455fa72420237eb05902327', '2022-12-30 10:00:59', '2022-12-30 10:00:59', NULL, '127.0.0.1', '', '9936aca9-bd06-4f2b-9b8d-ed67cc5b1e94', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (133, 'client.rm.sqlParserType', 'SEATA_GROUP', 'druid', '3d650fb8a5df01600281d48c47c9fa60', '2022-12-30 10:01:00', '2022-12-30 10:01:00', NULL, '127.0.0.1', '', '9936aca9-bd06-4f2b-9b8d-ed67cc5b1e94', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (134, 'client.rm.reportSuccessEnable', 'SEATA_GROUP', 'false', '68934a3e9455fa72420237eb05902327', '2022-12-30 10:01:00', '2022-12-30 10:01:00', NULL, '127.0.0.1', '', '9936aca9-bd06-4f2b-9b8d-ed67cc5b1e94', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (135, 'client.rm.sagaBranchRegisterEnable', 'SEATA_GROUP', 'false', '68934a3e9455fa72420237eb05902327', '2022-12-30 10:01:00', '2022-12-30 10:01:00', NULL, '127.0.0.1', '', '9936aca9-bd06-4f2b-9b8d-ed67cc5b1e94', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (136, 'client.tm.commitRetryCount', 'SEATA_GROUP', '5', 'e4da3b7fbbce2345d7772b0674a318d5', '2022-12-30 10:01:00', '2022-12-30 10:01:00', NULL, '127.0.0.1', '', '9936aca9-bd06-4f2b-9b8d-ed67cc5b1e94', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (137, 'client.tm.rollbackRetryCount', 'SEATA_GROUP', '5', 'e4da3b7fbbce2345d7772b0674a318d5', '2022-12-30 10:01:00', '2022-12-30 10:01:00', NULL, '127.0.0.1', '', '9936aca9-bd06-4f2b-9b8d-ed67cc5b1e94', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (138, 'store.mode', 'SEATA_GROUP', 'db', 'd77d5e503ad1439f585ac494268b351b', '2022-12-30 10:01:00', '2022-12-30 10:01:00', NULL, '127.0.0.1', '', '9936aca9-bd06-4f2b-9b8d-ed67cc5b1e94', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (139, 'store.file.dir', 'SEATA_GROUP', 'file_store/data', '6a8dec07c44c33a8a9247cba5710bbb2', '2022-12-30 10:01:00', '2022-12-30 10:01:00', NULL, '127.0.0.1', '', '9936aca9-bd06-4f2b-9b8d-ed67cc5b1e94', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (140, 'store.file.maxBranchSessionSize', 'SEATA_GROUP', '16384', 'c76fe1d8e08462434d800487585be217', '2022-12-30 10:01:00', '2022-12-30 10:01:00', NULL, '127.0.0.1', '', '9936aca9-bd06-4f2b-9b8d-ed67cc5b1e94', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (141, 'store.file.maxGlobalSessionSize', 'SEATA_GROUP', '512', '10a7cdd970fe135cf4f7bb55c0e3b59f', '2022-12-30 10:01:00', '2022-12-30 10:01:00', NULL, '127.0.0.1', '', '9936aca9-bd06-4f2b-9b8d-ed67cc5b1e94', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (142, 'store.file.fileWriteBufferCacheSize', 'SEATA_GROUP', '16384', 'c76fe1d8e08462434d800487585be217', '2022-12-30 10:01:00', '2022-12-30 10:01:00', NULL, '127.0.0.1', '', '9936aca9-bd06-4f2b-9b8d-ed67cc5b1e94', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (143, 'store.file.flushDiskMode', 'SEATA_GROUP', 'async', '0df93e34273b367bb63bad28c94c78d5', '2022-12-30 10:01:00', '2022-12-30 10:01:00', NULL, '127.0.0.1', '', '9936aca9-bd06-4f2b-9b8d-ed67cc5b1e94', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (144, 'store.file.sessionReloadReadSize', 'SEATA_GROUP', '100', 'f899139df5e1059396431415e770c6dd', '2022-12-30 10:01:00', '2022-12-30 10:01:00', NULL, '127.0.0.1', '', '9936aca9-bd06-4f2b-9b8d-ed67cc5b1e94', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (145, 'store.db.datasource', 'SEATA_GROUP', 'druid', '3d650fb8a5df01600281d48c47c9fa60', '2022-12-30 10:01:00', '2022-12-30 10:01:00', NULL, '127.0.0.1', '', '9936aca9-bd06-4f2b-9b8d-ed67cc5b1e94', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (146, 'store.db.dbType', 'SEATA_GROUP', 'mysql', '81c3b080dad537de7e10e0987a4bf52e', '2022-12-30 10:01:00', '2022-12-30 10:01:00', NULL, '127.0.0.1', '', '9936aca9-bd06-4f2b-9b8d-ed67cc5b1e94', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (147, 'store.db.driverClassName', 'SEATA_GROUP', 'com.mysql.jdbc.Driver', '683cf0c3a5a56cec94dfac94ca16d760', '2022-12-30 10:01:00', '2022-12-30 10:01:00', NULL, '127.0.0.1', '', '9936aca9-bd06-4f2b-9b8d-ed67cc5b1e94', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (148, 'store.db.url', 'SEATA_GROUP', 'jdbc:mysql://127.0.0.1:3306/seata?useUnicode=true', 'cbb3bd573704f125fb4f2489208abaec', '2022-12-30 10:01:00', '2022-12-30 10:01:00', NULL, '127.0.0.1', '', '9936aca9-bd06-4f2b-9b8d-ed67cc5b1e94', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (149, 'store.db.user', 'SEATA_GROUP', 'root', '63a9f0ea7bb98050796b649e85481845', '2022-12-30 10:01:00', '2022-12-30 10:01:00', NULL, '127.0.0.1', '', '9936aca9-bd06-4f2b-9b8d-ed67cc5b1e94', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (150, 'store.db.password', 'SEATA_GROUP', 'Mysql@135269', 'fdaa111ab0aacdbef88174980b6b0803', '2022-12-30 10:01:00', '2022-12-30 10:01:00', NULL, '127.0.0.1', '', '9936aca9-bd06-4f2b-9b8d-ed67cc5b1e94', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (151, 'store.db.minConn', 'SEATA_GROUP', '5', 'e4da3b7fbbce2345d7772b0674a318d5', '2022-12-30 10:01:00', '2022-12-30 10:01:00', NULL, '127.0.0.1', '', '9936aca9-bd06-4f2b-9b8d-ed67cc5b1e94', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (152, 'store.db.maxConn', 'SEATA_GROUP', '30', '34173cb38f07f89ddbebc2ac9128303f', '2022-12-30 10:01:00', '2022-12-30 10:01:00', NULL, '127.0.0.1', '', '9936aca9-bd06-4f2b-9b8d-ed67cc5b1e94', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (153, 'store.db.globalTable', 'SEATA_GROUP', 'global_table', '8b28fb6bb4c4f984df2709381f8eba2b', '2022-12-30 10:01:00', '2022-12-30 10:01:00', NULL, '127.0.0.1', '', '9936aca9-bd06-4f2b-9b8d-ed67cc5b1e94', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (154, 'store.db.branchTable', 'SEATA_GROUP', 'branch_table', '54bcdac38cf62e103fe115bcf46a660c', '2022-12-30 10:01:00', '2022-12-30 10:01:00', NULL, '127.0.0.1', '', '9936aca9-bd06-4f2b-9b8d-ed67cc5b1e94', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (155, 'store.db.queryLimit', 'SEATA_GROUP', '100', 'f899139df5e1059396431415e770c6dd', '2022-12-30 10:01:00', '2022-12-30 10:01:00', NULL, '127.0.0.1', '', '9936aca9-bd06-4f2b-9b8d-ed67cc5b1e94', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (156, 'store.db.lockTable', 'SEATA_GROUP', 'lock_table', '55e0cae3b6dc6696b768db90098b8f2f', '2022-12-30 10:01:00', '2022-12-30 10:01:00', NULL, '127.0.0.1', '', '9936aca9-bd06-4f2b-9b8d-ed67cc5b1e94', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (157, 'store.db.maxWait', 'SEATA_GROUP', '5000', 'a35fe7f7fe8217b4369a0af4244d1fca', '2022-12-30 10:01:00', '2022-12-30 10:01:00', NULL, '127.0.0.1', '', '9936aca9-bd06-4f2b-9b8d-ed67cc5b1e94', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (158, 'server.recovery.committingRetryPeriod', 'SEATA_GROUP', '1000', 'a9b7ba70783b617e9998dc4dd82eb3c5', '2022-12-30 10:01:00', '2022-12-30 10:01:00', NULL, '127.0.0.1', '', '9936aca9-bd06-4f2b-9b8d-ed67cc5b1e94', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (159, 'server.recovery.asynCommittingRetryPeriod', 'SEATA_GROUP', '1000', 'a9b7ba70783b617e9998dc4dd82eb3c5', '2022-12-30 10:01:00', '2022-12-30 10:01:00', NULL, '127.0.0.1', '', '9936aca9-bd06-4f2b-9b8d-ed67cc5b1e94', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (160, 'server.recovery.rollbackingRetryPeriod', 'SEATA_GROUP', '1000', 'a9b7ba70783b617e9998dc4dd82eb3c5', '2022-12-30 10:01:00', '2022-12-30 10:01:00', NULL, '127.0.0.1', '', '9936aca9-bd06-4f2b-9b8d-ed67cc5b1e94', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (161, 'server.recovery.timeoutRetryPeriod', 'SEATA_GROUP', '1000', 'a9b7ba70783b617e9998dc4dd82eb3c5', '2022-12-30 10:01:00', '2022-12-30 10:01:00', NULL, '127.0.0.1', '', '9936aca9-bd06-4f2b-9b8d-ed67cc5b1e94', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (162, 'server.maxCommitRetryTimeout', 'SEATA_GROUP', '-1', '6bb61e3b7bce0931da574d19d1d82c88', '2022-12-30 10:01:00', '2022-12-30 10:01:00', NULL, '127.0.0.1', '', '9936aca9-bd06-4f2b-9b8d-ed67cc5b1e94', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (163, 'server.maxRollbackRetryTimeout', 'SEATA_GROUP', '-1', '6bb61e3b7bce0931da574d19d1d82c88', '2022-12-30 10:01:00', '2022-12-30 10:01:00', NULL, '127.0.0.1', '', '9936aca9-bd06-4f2b-9b8d-ed67cc5b1e94', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (164, 'server.rollbackRetryTimeoutUnlockEnable', 'SEATA_GROUP', 'false', '68934a3e9455fa72420237eb05902327', '2022-12-30 10:01:00', '2022-12-30 10:01:00', NULL, '127.0.0.1', '', '9936aca9-bd06-4f2b-9b8d-ed67cc5b1e94', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (165, 'client.undo.dataValidation', 'SEATA_GROUP', 'true', 'b326b5062b2f0e69046810717534cb09', '2022-12-30 10:01:00', '2022-12-30 10:01:00', NULL, '127.0.0.1', '', '9936aca9-bd06-4f2b-9b8d-ed67cc5b1e94', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (166, 'client.undo.logSerialization', 'SEATA_GROUP', 'jackson', 'b41779690b83f182acc67d6388c7bac9', '2022-12-30 10:01:00', '2022-12-30 10:01:00', NULL, '127.0.0.1', '', '9936aca9-bd06-4f2b-9b8d-ed67cc5b1e94', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (167, 'server.undo.logSaveDays', 'SEATA_GROUP', '7', '8f14e45fceea167a5a36dedd4bea2543', '2022-12-30 10:01:00', '2022-12-30 10:01:00', NULL, '127.0.0.1', '', '9936aca9-bd06-4f2b-9b8d-ed67cc5b1e94', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (168, 'server.undo.logDeletePeriod', 'SEATA_GROUP', '86400000', 'f4c122804fe9076cb2710f55c3c6e346', '2022-12-30 10:01:00', '2022-12-30 10:01:00', NULL, '127.0.0.1', '', '9936aca9-bd06-4f2b-9b8d-ed67cc5b1e94', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (169, 'client.undo.logTable', 'SEATA_GROUP', 'undo_log', '2842d229c24afe9e61437135e8306614', '2022-12-30 10:01:00', '2022-12-30 10:01:00', NULL, '127.0.0.1', '', '9936aca9-bd06-4f2b-9b8d-ed67cc5b1e94', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (170, 'client.log.exceptionRate', 'SEATA_GROUP', '100', 'f899139df5e1059396431415e770c6dd', '2022-12-30 10:01:00', '2022-12-30 10:01:00', NULL, '127.0.0.1', '', '9936aca9-bd06-4f2b-9b8d-ed67cc5b1e94', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (171, 'transport.serialization', 'SEATA_GROUP', 'seata', 'b943081c423b9a5416a706524ee05d40', '2022-12-30 10:01:00', '2022-12-30 10:01:00', NULL, '127.0.0.1', '', '9936aca9-bd06-4f2b-9b8d-ed67cc5b1e94', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (172, 'transport.compressor', 'SEATA_GROUP', 'none', '334c4a4c42fdb79d7ebc3e73b517e6f8', '2022-12-30 10:01:00', '2022-12-30 10:01:00', NULL, '127.0.0.1', '', '9936aca9-bd06-4f2b-9b8d-ed67cc5b1e94', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (173, 'metrics.enabled', 'SEATA_GROUP', 'false', '68934a3e9455fa72420237eb05902327', '2022-12-30 10:01:00', '2022-12-30 10:01:00', NULL, '127.0.0.1', '', '9936aca9-bd06-4f2b-9b8d-ed67cc5b1e94', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (174, 'metrics.registryType', 'SEATA_GROUP', 'compact', '7cf74ca49c304df8150205fc915cd465', '2022-12-30 10:01:00', '2022-12-30 10:01:00', NULL, '127.0.0.1', '', '9936aca9-bd06-4f2b-9b8d-ed67cc5b1e94', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (175, 'metrics.exporterList', 'SEATA_GROUP', 'prometheus', 'e4f00638b8a10e6994e67af2f832d51c', '2022-12-30 10:01:00', '2022-12-30 10:01:00', NULL, '127.0.0.1', '', '9936aca9-bd06-4f2b-9b8d-ed67cc5b1e94', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (176, 'metrics.exporterPrometheusPort', 'SEATA_GROUP', '9898', '7b9dc501afe4ee11c56a4831e20cee71', '2022-12-30 10:01:00', '2022-12-30 10:01:00', NULL, '127.0.0.1', '', '9936aca9-bd06-4f2b-9b8d-ed67cc5b1e94', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (187, 'plan-dev.yaml', 'DEFAULT_GROUP', 'spring:\r\n  mvc:\r\n    pathmatch:\r\n      matching-strategy: ant_path_matcher\r\n  autoconfigure:\r\n    # 排除 Druid 自动配置\r\n    exclude: com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure\r\n  datasource:\r\n    #配置Druid数据源\r\n    type: com.alibaba.druid.pool.DruidDataSource\r\n    dynamic:\r\n      #设置默认的数据源或者数据源组,默认值即为master\r\n      primary: master\r\n      #严格匹配数据源,默认false. true未匹配到指定数据源时抛异常,false使用默认数据源\r\n      strict: false\r\n      datasource:\r\n        master:\r\n          driver-class-name: com.mysql.cj.jdbc.Driver\r\n          url: jdbc:mysql://39.98.108.121:3306/know_boot?useSSL=false&useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&transformedBitIsBoolean=true&tinyInt1isBit=false&allowMultiQueries=true&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai\r\n          username: root\r\n          password: Mysql@135269\r\n        slave:\r\n          driver-class-name: com.mysql.cj.jdbc.Driver\r\n          url: jdbc:mysql://39.98.108.121:3306/jeecg_boot?useSSL=false&useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&transformedBitIsBoolean=true&tinyInt1isBit=false&allowMultiQueries=true&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai\r\n          username: root\r\n          password: Mysql@135269\r\n    #Druid数据源专有配置，必须导入druid-spring-boot-starter才能生效，也可以通过DruidConfig注入生效\r\n    druid:\r\n      initial-size: 5\r\n      min-idle: 5\r\n      max-active: 20\r\n      max-wait: 60000\r\n      time-between-eviction-runs-millis: 60000\r\n      min-evictable-idle-time-millis: 30000\r\n      validation-query: SELECT 1 FROM DUAL\r\n      test-while-idle: true\r\n      test-on-borrow: false\r\n      test-on-return: false\r\n      pool-prepared-statements: true\r\n      #配置监控统计拦截的filters,stat:监控统计，log4j,日志记录，wall,防御sql注入\r\n      #如果允许时报错 java.lang.ClassNotFoundException,则导入log4j依赖即可\r\n      filters: stat,wall,log4j\r\n      max-pool-prepared-statement-per-connection-size: 20\r\n      use-global-data-source-stat: true\r\n      connect-properties: druid.stat.mergesql=true;druid.stat.slowSqlMills=500\r\n      #配置监控页面servlet的用户名和密码\r\n      #访问地址：http://localhost:9001/system/druid/login.html\r\n      stat-view-servlet:\r\n        enabled: true\r\n        login-username: admin\r\n        login-password: 123456\r\n        reset-enable: false\r\n      #配置监控页面filter过滤器\r\n      web-stat-filter:\r\n        enabled: true\r\n        url-pattern: /*\r\n        exclusions: \'*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*\'\r\n  redis:\r\n    #redis数据库索引(默认为0)\r\n    database: 0\r\n    #redis服务器地址\r\n    host: 39.98.108.121\r\n    #redis服务器连接端口\r\n    port: 10001\r\n    #redis连接密码\r\n    password: 123456789\r\n    lettuce:\r\n      pool:\r\n        # 连接池最大连接数（使用负值表示没有限制） 默认 8\r\n        max-active: 16\r\n        # 连接池最大阻塞等待时间（使用负值表示没有限制） 默认 -1\r\n        max-wait: 60000\r\n        # 连接池中的最大空闲连接 默认 8\r\n        max-idle: 10\r\n        # 连接池中的最小空闲连接 默认 0\r\n        min-idle: 3\r\n  rabbitmq:\r\n    host: 39.98.108.121\r\n    port: 5672\r\n    virtualHost: /pd\r\n    username: admin\r\n    password: admin\r\n    #这个配置是保证提供者确保消息推送到交换机中，不管成不成功，都会回调\r\n    publisher-confirm-type: correlated\r\n    #保证交换机能把消息推送到队列中\r\n    publisher-returns: true\r\n    virtual-host: /\r\n    #这个配置是保证消费者会消费消息，手动确认\r\n    listener:\r\n      simple:\r\n        acknowledge-mode: manual\r\n    template:\r\n      mandatory: true\r\nlogging:\r\n  # mybatis的sql语句是debug下打印\r\n  level:\r\n    com.know.knowboot.system.mapper: debug\r\n\r\n# mybatis plus 设置\r\nmybatis-plus:\r\n  mapper-locations: classpath:/mapper/**.xml\r\n  #  global-config:\r\n  #    banner: false\r\n  #    db-config:\r\n  #      logic-delete-value: 1 #删除后的状态 默认值1\r\n  #      logic-not-delete-value: 0 #逻辑前的值 默认值0\r\n  configuration:\r\n    # 控制台打印sql\r\n    #log-impl: org.apache.ibatis.logging.stdout.StdOutImpl\r\n    # 返回类型为Map,显示null对应的字段\r\n    call-setters-on-nulls: true\r\n\r\nelasticsearch:\r\n  # es的链接地址\r\n  host: 127.0.0.1\r\n  # 端口号\r\n  port: 9200\r\n  # 账号、密码\r\n#  username: elastic\r\n#  password: 123456\r\n\r\n# 自动以starter\r\nknow:\r\n  generator:\r\n    driver-class-name: com.mysql.cj.jdbc.Driver\r\n    jdbc-url: jdbc:mysql://192.168.1.28:3306/data_test?useSSL=false&useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&transformedBitIsBoolean=true&tinyInt1isBit=false&allowMultiQueries=true&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai\r\n    user-name: root\r\n    password: Flyedt@2021\r\n    ignore-table: test\r\n    ignore-table-prefix: test_\r\n    ignore-table-suffix: _test\r\n    save-path: D:/DatabaseDocument/\r\n    version: 1.0.0\r\n    description: 自动生成SQL库文档\r\n  jwt:\r\n    secret: qnAqsQa7600vrTBcr1WB8P8dg4cbgS5i8LZGjWnpREL # 密钥\r\n    expiration: 30 # token 有效期（S)\r\n  #限流器QPS设置\r\n  rateLimiter:\r\n    all:\r\n      #每秒生成令牌个数\r\n      permitsPerSecond: 1\r\n      #在warmupPeriod时间内RateLimiter会增加它的速率，在抵达它的稳定速率或者最大速率之前\r\n      warmupPeriod: 1\r\n    resource:\r\n      permitsPerSecond: 1\r\n      warmupPeriod: 1\r\n    interface:\r\n      permitsPerSecond: 1\r\n      warmupPeriod: 1\r\n  ignore:\r\n    urls:\r\n      - ${server.servlet.context-path}/sys/user/**\r\n  redisson:\r\n    address: redis://${spring.redis.host}:${spring.redis.port}', '65864e760acedeee8ecde64676413f57', '2023-01-05 08:59:55', '2023-01-13 11:36:39', NULL, '221.3.20.86', '', 'ab16776b-dae9-474e-8943-7bbbc627b637', 'null', 'null', 'null', 'yaml', 'null');
INSERT INTO `config_info` VALUES (188, 'plan-flow-rules', 'DEFAULT_GROUP', '[\r\n  {\r\n    \"resource\": \"/plan/base/customer/list\",\r\n    \"limitApp\": \"default\",\r\n    \"grade\": 1,\r\n    \"count\": 1,\r\n    \"clusterMode\": false,\r\n    \"controlBehavior\": 0,\r\n    \"strategy\": 0,\r\n    \"warmUpPeriodSec\": 10,\r\n    \"maxQueueingTimeMs\": 500,\r\n    \"refResource\": \"rrr\"\r\n  }\r\n]', 'c8632f6d9522beaf8e9a9177a7397fdb', '2023-01-10 08:31:10', '2023-01-10 08:54:15', NULL, '221.3.20.86', '', '3175f583-cd9b-4a0d-9991-f57ab23f5918', 'sentinel', 'null', 'null', 'json', 'null');
INSERT INTO `config_info` VALUES (194, 'plan-degrade-rules', 'DEFAULT_GROUP', '[\r\n  {\r\n    \"resource\": \"/plan/base/customer/list\",\r\n    \"limitApp\": \"default\",\r\n    \"grade\": 0,\r\n    \"count\": 200,\r\n    \"slowRatioThreshold\": 0.2,\r\n    \"minRequestAmount\": 5,\r\n    \"statIntervalMs\": 1000,\r\n    \"timeWindow\": 10\r\n  }\r\n]', '529f289cbfa5b0b349b8a28be71208ea', '2023-01-10 08:55:02', '2023-01-10 08:59:18', NULL, '221.3.20.86', '', '3175f583-cd9b-4a0d-9991-f57ab23f5918', 'sentinel', 'null', 'null', 'json', 'null');
INSERT INTO `config_info` VALUES (196, 'plan-system-rules', 'DEFAULT_GROUP', '  [\r\n    {\r\n      \"avgRt\": 1,\r\n      \"highestCpuUsage\": -1,\r\n      \"highestSystemLoad\": -1,\r\n      \"maxThread\": -1,\r\n      \"qps\": -1\r\n    }\r\n  ]', '025fee8a910b0e1faddbdd47096e51cd', '2023-01-10 08:56:22', '2023-01-10 08:59:26', NULL, '221.3.20.86', '', '3175f583-cd9b-4a0d-9991-f57ab23f5918', 'sentinel', 'null', 'null', 'json', 'null');
INSERT INTO `config_info` VALUES (197, 'plan-authority-rules', 'DEFAULT_GROUP', '[\r\n  {\r\n    \"resource\": \"sentinel_spring_web_context\",\r\n    \"limitApp\": \"/plan/base/customer/list\",\r\n    \"strategy\": 0\r\n  }\r\n]', 'b329783779d1850df85f14ddc093814a', '2023-01-10 08:57:02', '2023-01-10 08:59:35', NULL, '221.3.20.86', '', '3175f583-cd9b-4a0d-9991-f57ab23f5918', 'sentinel', 'null', 'null', 'json', 'null');
INSERT INTO `config_info` VALUES (198, 'plan-param-flow-rules', 'DEFAULT_GROUP', '[\r\n  {\r\n    \"resource\": \"/test1\",\r\n    \"grade\": 1,\r\n    \"paramIdx\": 0,\r\n    \"count\": 13,\r\n    \"durationInSec\": 6,\r\n    \"clusterMode\": false,\r\n    \"burstCount\": 0,\r\n    \"clusterConfig\": {\r\n      \"fallbackToLocalWhenFail\": true,\r\n      \"flowId\": 2,\r\n      \"sampleCount\": 10,\r\n      \"thresholdType\": 0,\r\n      \"windowIntervalMs\": 1000\r\n    },\r\n    \"controlBehavior\": 0,\r\n    \"limitApp\": \"default\",\r\n    \"maxQueueingTimeMs\": 0,\r\n    \"paramFlowItemList\": [\r\n      {\r\n        \"classType\": \"int\",\r\n        \"count\": 222,\r\n        \"object\": \"2\"\r\n      }\r\n    ]\r\n  }\r\n]', 'c8ca5bd3edbe1f5574928153d4e86749', '2023-01-10 08:59:00', '2023-01-10 08:59:42', NULL, '221.3.20.86', '', '3175f583-cd9b-4a0d-9991-f57ab23f5918', 'sentinel', 'null', 'null', 'json', 'null');
INSERT INTO `config_info` VALUES (203, 'transport.type', 'SEATA_GROUP', 'TCP', 'b136ef5f6a01d816991fe3cf7a6ac763', '2023-01-10 16:08:37', '2023-01-10 16:08:37', NULL, '39.98.108.121', '', '35f21db9-107d-4ecf-93f7-e5165847232e', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (204, 'transport.server', 'SEATA_GROUP', 'NIO', 'b6d9dfc0fb54277321cebc0fff55df2f', '2023-01-10 16:08:37', '2023-01-10 16:08:37', NULL, '39.98.108.121', '', '35f21db9-107d-4ecf-93f7-e5165847232e', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (205, 'transport.heartbeat', 'SEATA_GROUP', 'true', 'b326b5062b2f0e69046810717534cb09', '2023-01-10 16:08:37', '2023-01-10 16:08:37', NULL, '39.98.108.121', '', '35f21db9-107d-4ecf-93f7-e5165847232e', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (206, 'transport.enableClientBatchSendRequest', 'SEATA_GROUP', 'false', '68934a3e9455fa72420237eb05902327', '2023-01-10 16:08:38', '2023-01-10 16:08:38', NULL, '39.98.108.121', '', '35f21db9-107d-4ecf-93f7-e5165847232e', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (207, 'transport.threadFactory.bossThreadPrefix', 'SEATA_GROUP', 'NettyBoss', '0f8db59a3b7f2823f38a70c308361836', '2023-01-10 16:08:38', '2023-01-10 16:08:38', NULL, '39.98.108.121', '', '35f21db9-107d-4ecf-93f7-e5165847232e', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (208, 'transport.threadFactory.workerThreadPrefix', 'SEATA_GROUP', 'NettyServerNIOWorker', 'a78ec7ef5d1631754c4e72ae8a3e9205', '2023-01-10 16:08:38', '2023-01-10 16:08:38', NULL, '39.98.108.121', '', '35f21db9-107d-4ecf-93f7-e5165847232e', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (209, 'transport.threadFactory.serverExecutorThreadPrefix', 'SEATA_GROUP', 'NettyServerBizHandler', '11a36309f3d9df84fa8b59cf071fa2da', '2023-01-10 16:08:38', '2023-01-10 16:08:38', NULL, '39.98.108.121', '', '35f21db9-107d-4ecf-93f7-e5165847232e', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (210, 'transport.threadFactory.shareBossWorker', 'SEATA_GROUP', 'false', '68934a3e9455fa72420237eb05902327', '2023-01-10 16:08:38', '2023-01-10 16:08:38', NULL, '39.98.108.121', '', '35f21db9-107d-4ecf-93f7-e5165847232e', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (211, 'transport.threadFactory.clientSelectorThreadPrefix', 'SEATA_GROUP', 'NettyClientSelector', 'cd7ec5a06541e75f5a7913752322c3af', '2023-01-10 16:08:38', '2023-01-10 16:08:38', NULL, '39.98.108.121', '', '35f21db9-107d-4ecf-93f7-e5165847232e', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (212, 'transport.threadFactory.clientSelectorThreadSize', 'SEATA_GROUP', '1', 'c4ca4238a0b923820dcc509a6f75849b', '2023-01-10 16:08:38', '2023-01-10 16:08:38', NULL, '39.98.108.121', '', '35f21db9-107d-4ecf-93f7-e5165847232e', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (213, 'transport.threadFactory.clientWorkerThreadPrefix', 'SEATA_GROUP', 'NettyClientWorkerThread', '61cf4e69a56354cf72f46dc86414a57e', '2023-01-10 16:08:38', '2023-01-10 16:08:38', NULL, '39.98.108.121', '', '35f21db9-107d-4ecf-93f7-e5165847232e', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (214, 'transport.threadFactory.bossThreadSize', 'SEATA_GROUP', '1', 'c4ca4238a0b923820dcc509a6f75849b', '2023-01-10 16:08:38', '2023-01-10 16:08:38', NULL, '39.98.108.121', '', '35f21db9-107d-4ecf-93f7-e5165847232e', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (215, 'transport.threadFactory.workerThreadSize', 'SEATA_GROUP', 'default', 'c21f969b5f03d33d43e04f8f136e7682', '2023-01-10 16:08:38', '2023-01-10 16:08:38', NULL, '39.98.108.121', '', '35f21db9-107d-4ecf-93f7-e5165847232e', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (216, 'transport.shutdown.wait', 'SEATA_GROUP', '3', 'eccbc87e4b5ce2fe28308fd9f2a7baf3', '2023-01-10 16:08:38', '2023-01-10 16:08:38', NULL, '39.98.108.121', '', '35f21db9-107d-4ecf-93f7-e5165847232e', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (217, 'service.vgroupMapping.my_test_tx_group', 'SEATA_GROUP', 'default', 'c21f969b5f03d33d43e04f8f136e7682', '2023-01-10 16:08:38', '2023-01-10 16:08:38', NULL, '39.98.108.121', '', '35f21db9-107d-4ecf-93f7-e5165847232e', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (218, 'service.default.grouplist', 'SEATA_GROUP', '39.98.108.121:10007', 'b371498d5cc8771e7ade02dec4cf8de4', '2023-01-10 16:08:38', '2023-01-10 16:08:38', NULL, '39.98.108.121', '', '35f21db9-107d-4ecf-93f7-e5165847232e', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (219, 'service.enableDegrade', 'SEATA_GROUP', 'false', '68934a3e9455fa72420237eb05902327', '2023-01-10 16:08:38', '2023-01-10 16:08:38', NULL, '39.98.108.121', '', '35f21db9-107d-4ecf-93f7-e5165847232e', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (220, 'service.disableGlobalTransaction', 'SEATA_GROUP', 'false', '68934a3e9455fa72420237eb05902327', '2023-01-10 16:08:38', '2023-01-10 16:08:38', NULL, '39.98.108.121', '', '35f21db9-107d-4ecf-93f7-e5165847232e', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (221, 'client.rm.asyncCommitBufferLimit', 'SEATA_GROUP', '10000', 'b7a782741f667201b54880c925faec4b', '2023-01-10 16:08:38', '2023-01-10 16:08:38', NULL, '39.98.108.121', '', '35f21db9-107d-4ecf-93f7-e5165847232e', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (222, 'client.rm.lock.retryInterval', 'SEATA_GROUP', '10', 'd3d9446802a44259755d38e6d163e820', '2023-01-10 16:08:38', '2023-01-10 16:08:38', NULL, '39.98.108.121', '', '35f21db9-107d-4ecf-93f7-e5165847232e', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (223, 'client.rm.lock.retryTimes', 'SEATA_GROUP', '30', '34173cb38f07f89ddbebc2ac9128303f', '2023-01-10 16:08:38', '2023-01-10 16:08:38', NULL, '39.98.108.121', '', '35f21db9-107d-4ecf-93f7-e5165847232e', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (224, 'client.rm.lock.retryPolicyBranchRollbackOnConflict', 'SEATA_GROUP', 'true', 'b326b5062b2f0e69046810717534cb09', '2023-01-10 16:08:38', '2023-01-10 16:08:38', NULL, '39.98.108.121', '', '35f21db9-107d-4ecf-93f7-e5165847232e', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (225, 'client.rm.reportRetryCount', 'SEATA_GROUP', '5', 'e4da3b7fbbce2345d7772b0674a318d5', '2023-01-10 16:08:38', '2023-01-10 16:08:38', NULL, '39.98.108.121', '', '35f21db9-107d-4ecf-93f7-e5165847232e', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (226, 'client.rm.tableMetaCheckEnable', 'SEATA_GROUP', 'false', '68934a3e9455fa72420237eb05902327', '2023-01-10 16:08:38', '2023-01-10 16:08:38', NULL, '39.98.108.121', '', '35f21db9-107d-4ecf-93f7-e5165847232e', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (227, 'client.rm.sqlParserType', 'SEATA_GROUP', 'druid', '3d650fb8a5df01600281d48c47c9fa60', '2023-01-10 16:08:38', '2023-01-10 16:08:38', NULL, '39.98.108.121', '', '35f21db9-107d-4ecf-93f7-e5165847232e', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (228, 'client.rm.reportSuccessEnable', 'SEATA_GROUP', 'false', '68934a3e9455fa72420237eb05902327', '2023-01-10 16:08:38', '2023-01-10 16:08:38', NULL, '39.98.108.121', '', '35f21db9-107d-4ecf-93f7-e5165847232e', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (229, 'client.rm.sagaBranchRegisterEnable', 'SEATA_GROUP', 'false', '68934a3e9455fa72420237eb05902327', '2023-01-10 16:08:38', '2023-01-10 16:08:38', NULL, '39.98.108.121', '', '35f21db9-107d-4ecf-93f7-e5165847232e', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (230, 'client.tm.commitRetryCount', 'SEATA_GROUP', '5', 'e4da3b7fbbce2345d7772b0674a318d5', '2023-01-10 16:08:38', '2023-01-10 16:08:38', NULL, '39.98.108.121', '', '35f21db9-107d-4ecf-93f7-e5165847232e', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (231, 'client.tm.rollbackRetryCount', 'SEATA_GROUP', '5', 'e4da3b7fbbce2345d7772b0674a318d5', '2023-01-10 16:08:38', '2023-01-10 16:08:38', NULL, '39.98.108.121', '', '35f21db9-107d-4ecf-93f7-e5165847232e', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (232, 'client.tm.degradeCheck', 'SEATA_GROUP', 'false', '68934a3e9455fa72420237eb05902327', '2023-01-10 16:08:38', '2023-01-10 16:08:38', NULL, '39.98.108.121', '', '35f21db9-107d-4ecf-93f7-e5165847232e', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (233, 'client.tm.degradeCheckAllowTimes', 'SEATA_GROUP', '10', 'd3d9446802a44259755d38e6d163e820', '2023-01-10 16:08:38', '2023-01-10 16:08:38', NULL, '39.98.108.121', '', '35f21db9-107d-4ecf-93f7-e5165847232e', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (234, 'client.tm.degradeCheckPeriod', 'SEATA_GROUP', '2000', '08f90c1a417155361a5c4b8d297e0d78', '2023-01-10 16:08:38', '2023-01-10 16:08:38', NULL, '39.98.108.121', '', '35f21db9-107d-4ecf-93f7-e5165847232e', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (235, 'store.mode', 'SEATA_GROUP', 'db', 'd77d5e503ad1439f585ac494268b351b', '2023-01-10 16:08:38', '2023-01-10 16:08:38', NULL, '39.98.108.121', '', '35f21db9-107d-4ecf-93f7-e5165847232e', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (236, 'store.file.dir', 'SEATA_GROUP', 'file_store/data', '6a8dec07c44c33a8a9247cba5710bbb2', '2023-01-10 16:08:38', '2023-01-10 16:08:38', NULL, '39.98.108.121', '', '35f21db9-107d-4ecf-93f7-e5165847232e', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (237, 'store.file.maxBranchSessionSize', 'SEATA_GROUP', '16384', 'c76fe1d8e08462434d800487585be217', '2023-01-10 16:08:38', '2023-01-10 16:08:38', NULL, '39.98.108.121', '', '35f21db9-107d-4ecf-93f7-e5165847232e', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (238, 'store.file.maxGlobalSessionSize', 'SEATA_GROUP', '512', '10a7cdd970fe135cf4f7bb55c0e3b59f', '2023-01-10 16:08:38', '2023-01-10 16:08:38', NULL, '39.98.108.121', '', '35f21db9-107d-4ecf-93f7-e5165847232e', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (239, 'store.file.fileWriteBufferCacheSize', 'SEATA_GROUP', '16384', 'c76fe1d8e08462434d800487585be217', '2023-01-10 16:08:38', '2023-01-10 16:08:38', NULL, '39.98.108.121', '', '35f21db9-107d-4ecf-93f7-e5165847232e', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (240, 'store.file.flushDiskMode', 'SEATA_GROUP', 'async', '0df93e34273b367bb63bad28c94c78d5', '2023-01-10 16:08:38', '2023-01-10 16:08:38', NULL, '39.98.108.121', '', '35f21db9-107d-4ecf-93f7-e5165847232e', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (241, 'store.file.sessionReloadReadSize', 'SEATA_GROUP', '100', 'f899139df5e1059396431415e770c6dd', '2023-01-10 16:08:38', '2023-01-10 16:08:38', NULL, '39.98.108.121', '', '35f21db9-107d-4ecf-93f7-e5165847232e', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (242, 'store.db.datasource', 'SEATA_GROUP', 'druid', '3d650fb8a5df01600281d48c47c9fa60', '2023-01-10 16:08:38', '2023-01-10 16:08:38', NULL, '39.98.108.121', '', '35f21db9-107d-4ecf-93f7-e5165847232e', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (243, 'store.db.dbType', 'SEATA_GROUP', 'mysql', '81c3b080dad537de7e10e0987a4bf52e', '2023-01-10 16:08:38', '2023-01-10 16:08:38', NULL, '39.98.108.121', '', '35f21db9-107d-4ecf-93f7-e5165847232e', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (244, 'store.db.driverClassName', 'SEATA_GROUP', 'com.mysql.jdbc.Driver', '683cf0c3a5a56cec94dfac94ca16d760', '2023-01-10 16:08:38', '2023-01-10 16:08:38', NULL, '39.98.108.121', '', '35f21db9-107d-4ecf-93f7-e5165847232e', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (245, 'store.db.url', 'SEATA_GROUP', 'jdbc:mysql://39.98.108.121:3306/seata?useUnicode=true', '108d3f1418b0b962acba64031e00b06f', '2023-01-10 16:08:38', '2023-01-17 11:05:37', NULL, '221.3.20.86', '', '35f21db9-107d-4ecf-93f7-e5165847232e', 'null', 'null', 'null', 'null', 'null');
INSERT INTO `config_info` VALUES (246, 'store.db.user', 'SEATA_GROUP', 'root', '63a9f0ea7bb98050796b649e85481845', '2023-01-10 16:08:38', '2023-01-10 16:08:38', NULL, '39.98.108.121', '', '35f21db9-107d-4ecf-93f7-e5165847232e', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (247, 'store.db.password', 'SEATA_GROUP', 'Mysql@135269', 'fdaa111ab0aacdbef88174980b6b0803', '2023-01-10 16:08:38', '2023-01-10 16:08:38', NULL, '39.98.108.121', '', '35f21db9-107d-4ecf-93f7-e5165847232e', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (248, 'store.db.minConn', 'SEATA_GROUP', '5', 'e4da3b7fbbce2345d7772b0674a318d5', '2023-01-10 16:08:38', '2023-01-10 16:08:38', NULL, '39.98.108.121', '', '35f21db9-107d-4ecf-93f7-e5165847232e', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (249, 'store.db.maxConn', 'SEATA_GROUP', '30', '34173cb38f07f89ddbebc2ac9128303f', '2023-01-10 16:08:38', '2023-01-10 16:08:38', NULL, '39.98.108.121', '', '35f21db9-107d-4ecf-93f7-e5165847232e', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (250, 'store.db.globalTable', 'SEATA_GROUP', 'global_table', '8b28fb6bb4c4f984df2709381f8eba2b', '2023-01-10 16:08:38', '2023-01-10 16:08:38', NULL, '39.98.108.121', '', '35f21db9-107d-4ecf-93f7-e5165847232e', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (251, 'store.db.branchTable', 'SEATA_GROUP', 'branch_table', '54bcdac38cf62e103fe115bcf46a660c', '2023-01-10 16:08:38', '2023-01-10 16:08:38', NULL, '39.98.108.121', '', '35f21db9-107d-4ecf-93f7-e5165847232e', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (252, 'store.db.queryLimit', 'SEATA_GROUP', '100', 'f899139df5e1059396431415e770c6dd', '2023-01-10 16:08:38', '2023-01-10 16:08:38', NULL, '39.98.108.121', '', '35f21db9-107d-4ecf-93f7-e5165847232e', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (253, 'store.db.lockTable', 'SEATA_GROUP', 'lock_table', '55e0cae3b6dc6696b768db90098b8f2f', '2023-01-10 16:08:38', '2023-01-10 16:08:38', NULL, '39.98.108.121', '', '35f21db9-107d-4ecf-93f7-e5165847232e', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (254, 'store.db.maxWait', 'SEATA_GROUP', '5000', 'a35fe7f7fe8217b4369a0af4244d1fca', '2023-01-10 16:08:38', '2023-01-10 16:08:38', NULL, '39.98.108.121', '', '35f21db9-107d-4ecf-93f7-e5165847232e', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (255, 'store.redis.host', 'SEATA_GROUP', '127.0.0.1', 'f528764d624db129b32c21fbca0cb8d6', '2023-01-10 16:08:38', '2023-01-10 16:08:38', NULL, '39.98.108.121', '', '35f21db9-107d-4ecf-93f7-e5165847232e', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (256, 'store.redis.port', 'SEATA_GROUP', '6379', '92c3b916311a5517d9290576e3ea37ad', '2023-01-10 16:08:38', '2023-01-10 16:08:38', NULL, '39.98.108.121', '', '35f21db9-107d-4ecf-93f7-e5165847232e', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (257, 'store.redis.maxConn', 'SEATA_GROUP', '10', 'd3d9446802a44259755d38e6d163e820', '2023-01-10 16:08:38', '2023-01-10 16:08:38', NULL, '39.98.108.121', '', '35f21db9-107d-4ecf-93f7-e5165847232e', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (258, 'store.redis.minConn', 'SEATA_GROUP', '1', 'c4ca4238a0b923820dcc509a6f75849b', '2023-01-10 16:08:38', '2023-01-10 16:08:38', NULL, '39.98.108.121', '', '35f21db9-107d-4ecf-93f7-e5165847232e', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (259, 'store.redis.database', 'SEATA_GROUP', '0', 'cfcd208495d565ef66e7dff9f98764da', '2023-01-10 16:08:38', '2023-01-10 16:08:38', NULL, '39.98.108.121', '', '35f21db9-107d-4ecf-93f7-e5165847232e', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (260, 'store.redis.password', 'SEATA_GROUP', 'null', '37a6259cc0c1dae299a7866489dff0bd', '2023-01-10 16:08:38', '2023-01-10 16:08:38', NULL, '39.98.108.121', '', '35f21db9-107d-4ecf-93f7-e5165847232e', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (261, 'store.redis.queryLimit', 'SEATA_GROUP', '100', 'f899139df5e1059396431415e770c6dd', '2023-01-10 16:08:38', '2023-01-10 16:08:38', NULL, '39.98.108.121', '', '35f21db9-107d-4ecf-93f7-e5165847232e', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (262, 'server.recovery.committingRetryPeriod', 'SEATA_GROUP', '1000', 'a9b7ba70783b617e9998dc4dd82eb3c5', '2023-01-10 16:08:38', '2023-01-10 16:08:38', NULL, '39.98.108.121', '', '35f21db9-107d-4ecf-93f7-e5165847232e', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (263, 'server.recovery.asynCommittingRetryPeriod', 'SEATA_GROUP', '1000', 'a9b7ba70783b617e9998dc4dd82eb3c5', '2023-01-10 16:08:38', '2023-01-10 16:08:38', NULL, '39.98.108.121', '', '35f21db9-107d-4ecf-93f7-e5165847232e', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (264, 'server.recovery.rollbackingRetryPeriod', 'SEATA_GROUP', '1000', 'a9b7ba70783b617e9998dc4dd82eb3c5', '2023-01-10 16:08:38', '2023-01-10 16:08:38', NULL, '39.98.108.121', '', '35f21db9-107d-4ecf-93f7-e5165847232e', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (265, 'server.recovery.timeoutRetryPeriod', 'SEATA_GROUP', '1000', 'a9b7ba70783b617e9998dc4dd82eb3c5', '2023-01-10 16:08:38', '2023-01-10 16:08:38', NULL, '39.98.108.121', '', '35f21db9-107d-4ecf-93f7-e5165847232e', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (266, 'server.maxCommitRetryTimeout', 'SEATA_GROUP', '-1', '6bb61e3b7bce0931da574d19d1d82c88', '2023-01-10 16:08:38', '2023-01-10 16:08:38', NULL, '39.98.108.121', '', '35f21db9-107d-4ecf-93f7-e5165847232e', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (267, 'server.maxRollbackRetryTimeout', 'SEATA_GROUP', '-1', '6bb61e3b7bce0931da574d19d1d82c88', '2023-01-10 16:08:39', '2023-01-10 16:08:39', NULL, '39.98.108.121', '', '35f21db9-107d-4ecf-93f7-e5165847232e', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (268, 'server.rollbackRetryTimeoutUnlockEnable', 'SEATA_GROUP', 'false', '68934a3e9455fa72420237eb05902327', '2023-01-10 16:08:39', '2023-01-10 16:08:39', NULL, '39.98.108.121', '', '35f21db9-107d-4ecf-93f7-e5165847232e', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (269, 'client.undo.dataValidation', 'SEATA_GROUP', 'true', 'b326b5062b2f0e69046810717534cb09', '2023-01-10 16:08:39', '2023-01-10 16:08:39', NULL, '39.98.108.121', '', '35f21db9-107d-4ecf-93f7-e5165847232e', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (270, 'client.undo.logSerialization', 'SEATA_GROUP', 'jackson', 'b41779690b83f182acc67d6388c7bac9', '2023-01-10 16:08:39', '2023-01-12 10:10:43', NULL, '221.3.20.86', '', '35f21db9-107d-4ecf-93f7-e5165847232e', 'null', 'null', 'null', 'null', 'null');
INSERT INTO `config_info` VALUES (271, 'client.undo.onlyCareUpdateColumns', 'SEATA_GROUP', 'true', 'b326b5062b2f0e69046810717534cb09', '2023-01-10 16:08:39', '2023-01-10 16:08:39', NULL, '39.98.108.121', '', '35f21db9-107d-4ecf-93f7-e5165847232e', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (272, 'server.undo.logSaveDays', 'SEATA_GROUP', '7', '8f14e45fceea167a5a36dedd4bea2543', '2023-01-10 16:08:39', '2023-01-10 16:08:39', NULL, '39.98.108.121', '', '35f21db9-107d-4ecf-93f7-e5165847232e', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (273, 'server.undo.logDeletePeriod', 'SEATA_GROUP', '86400000', 'f4c122804fe9076cb2710f55c3c6e346', '2023-01-10 16:08:39', '2023-01-10 16:08:39', NULL, '39.98.108.121', '', '35f21db9-107d-4ecf-93f7-e5165847232e', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (274, 'client.undo.logTable', 'SEATA_GROUP', 'undo_log', '2842d229c24afe9e61437135e8306614', '2023-01-10 16:08:39', '2023-01-10 16:08:39', NULL, '39.98.108.121', '', '35f21db9-107d-4ecf-93f7-e5165847232e', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (275, 'client.log.exceptionRate', 'SEATA_GROUP', '100', 'f899139df5e1059396431415e770c6dd', '2023-01-10 16:08:39', '2023-01-10 16:08:39', NULL, '39.98.108.121', '', '35f21db9-107d-4ecf-93f7-e5165847232e', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (276, 'transport.serialization', 'SEATA_GROUP', 'seata', 'b943081c423b9a5416a706524ee05d40', '2023-01-10 16:08:39', '2023-01-10 16:08:39', NULL, '39.98.108.121', '', '35f21db9-107d-4ecf-93f7-e5165847232e', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (277, 'transport.compressor', 'SEATA_GROUP', 'none', '334c4a4c42fdb79d7ebc3e73b517e6f8', '2023-01-10 16:08:39', '2023-01-10 16:08:39', NULL, '39.98.108.121', '', '35f21db9-107d-4ecf-93f7-e5165847232e', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (278, 'metrics.enabled', 'SEATA_GROUP', 'false', '68934a3e9455fa72420237eb05902327', '2023-01-10 16:08:39', '2023-01-10 16:08:39', NULL, '39.98.108.121', '', '35f21db9-107d-4ecf-93f7-e5165847232e', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (279, 'metrics.registryType', 'SEATA_GROUP', 'compact', '7cf74ca49c304df8150205fc915cd465', '2023-01-10 16:08:39', '2023-01-10 16:08:39', NULL, '39.98.108.121', '', '35f21db9-107d-4ecf-93f7-e5165847232e', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (280, 'metrics.exporterList', 'SEATA_GROUP', 'prometheus', 'e4f00638b8a10e6994e67af2f832d51c', '2023-01-10 16:08:39', '2023-01-10 16:08:39', NULL, '39.98.108.121', '', '35f21db9-107d-4ecf-93f7-e5165847232e', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (281, 'metrics.exporterPrometheusPort', 'SEATA_GROUP', '9898', '7b9dc501afe4ee11c56a4831e20cee71', '2023-01-10 16:08:39', '2023-01-10 16:08:39', NULL, '39.98.108.121', '', '35f21db9-107d-4ecf-93f7-e5165847232e', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (283, 'transport.type', 'SEATA_GROUP', 'TCP', 'b136ef5f6a01d816991fe3cf7a6ac763', '2023-01-10 18:13:35', '2023-01-10 18:13:35', NULL, '127.0.0.1', '', '0a4a6631-6406-4da5-b6b1-cfed7f1849d6', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (284, 'transport.server', 'SEATA_GROUP', 'NIO', 'b6d9dfc0fb54277321cebc0fff55df2f', '2023-01-10 18:13:36', '2023-01-10 18:13:36', NULL, '127.0.0.1', '', '0a4a6631-6406-4da5-b6b1-cfed7f1849d6', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (285, 'transport.heartbeat', 'SEATA_GROUP', 'true', 'b326b5062b2f0e69046810717534cb09', '2023-01-10 18:13:36', '2023-01-10 18:13:36', NULL, '127.0.0.1', '', '0a4a6631-6406-4da5-b6b1-cfed7f1849d6', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (286, 'transport.enableClientBatchSendRequest', 'SEATA_GROUP', 'false', '68934a3e9455fa72420237eb05902327', '2023-01-10 18:13:37', '2023-01-10 18:13:37', NULL, '127.0.0.1', '', '0a4a6631-6406-4da5-b6b1-cfed7f1849d6', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (287, 'transport.threadFactory.bossThreadPrefix', 'SEATA_GROUP', 'NettyBoss', '0f8db59a3b7f2823f38a70c308361836', '2023-01-10 18:13:37', '2023-01-10 18:13:37', NULL, '127.0.0.1', '', '0a4a6631-6406-4da5-b6b1-cfed7f1849d6', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (288, 'transport.threadFactory.workerThreadPrefix', 'SEATA_GROUP', 'NettyServerNIOWorker', 'a78ec7ef5d1631754c4e72ae8a3e9205', '2023-01-10 18:13:37', '2023-01-10 18:13:37', NULL, '127.0.0.1', '', '0a4a6631-6406-4da5-b6b1-cfed7f1849d6', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (289, 'transport.threadFactory.serverExecutorThreadPrefix', 'SEATA_GROUP', 'NettyServerBizHandler', '11a36309f3d9df84fa8b59cf071fa2da', '2023-01-10 18:13:38', '2023-01-10 18:13:38', NULL, '127.0.0.1', '', '0a4a6631-6406-4da5-b6b1-cfed7f1849d6', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (290, 'transport.threadFactory.shareBossWorker', 'SEATA_GROUP', 'false', '68934a3e9455fa72420237eb05902327', '2023-01-10 18:13:38', '2023-01-10 18:13:38', NULL, '127.0.0.1', '', '0a4a6631-6406-4da5-b6b1-cfed7f1849d6', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (291, 'transport.threadFactory.clientSelectorThreadPrefix', 'SEATA_GROUP', 'NettyClientSelector', 'cd7ec5a06541e75f5a7913752322c3af', '2023-01-10 18:13:38', '2023-01-10 18:13:38', NULL, '127.0.0.1', '', '0a4a6631-6406-4da5-b6b1-cfed7f1849d6', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (292, 'transport.threadFactory.clientSelectorThreadSize', 'SEATA_GROUP', '1', 'c4ca4238a0b923820dcc509a6f75849b', '2023-01-10 18:13:39', '2023-01-10 18:13:39', NULL, '127.0.0.1', '', '0a4a6631-6406-4da5-b6b1-cfed7f1849d6', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (293, 'transport.threadFactory.clientWorkerThreadPrefix', 'SEATA_GROUP', 'NettyClientWorkerThread', '61cf4e69a56354cf72f46dc86414a57e', '2023-01-10 18:13:39', '2023-01-10 18:13:39', NULL, '127.0.0.1', '', '0a4a6631-6406-4da5-b6b1-cfed7f1849d6', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (294, 'transport.threadFactory.bossThreadSize', 'SEATA_GROUP', '1', 'c4ca4238a0b923820dcc509a6f75849b', '2023-01-10 18:13:40', '2023-01-10 18:13:40', NULL, '127.0.0.1', '', '0a4a6631-6406-4da5-b6b1-cfed7f1849d6', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (295, 'transport.threadFactory.workerThreadSize', 'SEATA_GROUP', 'default', 'c21f969b5f03d33d43e04f8f136e7682', '2023-01-10 18:13:40', '2023-01-10 18:13:40', NULL, '127.0.0.1', '', '0a4a6631-6406-4da5-b6b1-cfed7f1849d6', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (296, 'transport.shutdown.wait', 'SEATA_GROUP', '3', 'eccbc87e4b5ce2fe28308fd9f2a7baf3', '2023-01-10 18:13:40', '2023-01-10 18:13:40', NULL, '127.0.0.1', '', '0a4a6631-6406-4da5-b6b1-cfed7f1849d6', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (297, 'service.vgroupMapping.my_test_tx_group', 'SEATA_GROUP', 'default', 'c21f969b5f03d33d43e04f8f136e7682', '2023-01-10 18:13:41', '2023-01-10 18:13:41', NULL, '127.0.0.1', '', '0a4a6631-6406-4da5-b6b1-cfed7f1849d6', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (298, 'service.default.grouplist', 'SEATA_GROUP', '127.0.0.1:8091', 'c32ce0d3e264525dcdada751f98143a3', '2023-01-10 18:13:41', '2023-01-10 18:13:41', NULL, '127.0.0.1', '', '0a4a6631-6406-4da5-b6b1-cfed7f1849d6', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (299, 'service.enableDegrade', 'SEATA_GROUP', 'false', '68934a3e9455fa72420237eb05902327', '2023-01-10 18:13:41', '2023-01-10 18:13:41', NULL, '127.0.0.1', '', '0a4a6631-6406-4da5-b6b1-cfed7f1849d6', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (300, 'service.disableGlobalTransaction', 'SEATA_GROUP', 'false', '68934a3e9455fa72420237eb05902327', '2023-01-10 18:13:42', '2023-01-10 18:13:42', NULL, '127.0.0.1', '', '0a4a6631-6406-4da5-b6b1-cfed7f1849d6', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (301, 'client.rm.asyncCommitBufferLimit', 'SEATA_GROUP', '10000', 'b7a782741f667201b54880c925faec4b', '2023-01-10 18:13:42', '2023-01-10 18:13:42', NULL, '127.0.0.1', '', '0a4a6631-6406-4da5-b6b1-cfed7f1849d6', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (302, 'client.rm.lock.retryInterval', 'SEATA_GROUP', '10', 'd3d9446802a44259755d38e6d163e820', '2023-01-10 18:13:42', '2023-01-10 18:13:42', NULL, '127.0.0.1', '', '0a4a6631-6406-4da5-b6b1-cfed7f1849d6', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (303, 'client.rm.lock.retryTimes', 'SEATA_GROUP', '30', '34173cb38f07f89ddbebc2ac9128303f', '2023-01-10 18:13:43', '2023-01-10 18:13:43', NULL, '127.0.0.1', '', '0a4a6631-6406-4da5-b6b1-cfed7f1849d6', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (304, 'client.rm.lock.retryPolicyBranchRollbackOnConflict', 'SEATA_GROUP', 'true', 'b326b5062b2f0e69046810717534cb09', '2023-01-10 18:13:43', '2023-01-10 18:13:43', NULL, '127.0.0.1', '', '0a4a6631-6406-4da5-b6b1-cfed7f1849d6', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (305, 'client.rm.reportRetryCount', 'SEATA_GROUP', '5', 'e4da3b7fbbce2345d7772b0674a318d5', '2023-01-10 18:13:44', '2023-01-10 18:13:44', NULL, '127.0.0.1', '', '0a4a6631-6406-4da5-b6b1-cfed7f1849d6', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (306, 'client.rm.tableMetaCheckEnable', 'SEATA_GROUP', 'false', '68934a3e9455fa72420237eb05902327', '2023-01-10 18:13:44', '2023-01-10 18:13:44', NULL, '127.0.0.1', '', '0a4a6631-6406-4da5-b6b1-cfed7f1849d6', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (307, 'client.rm.sqlParserType', 'SEATA_GROUP', 'druid', '3d650fb8a5df01600281d48c47c9fa60', '2023-01-10 18:13:44', '2023-01-10 18:13:44', NULL, '127.0.0.1', '', '0a4a6631-6406-4da5-b6b1-cfed7f1849d6', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (308, 'client.rm.reportSuccessEnable', 'SEATA_GROUP', 'false', '68934a3e9455fa72420237eb05902327', '2023-01-10 18:13:45', '2023-01-10 18:13:45', NULL, '127.0.0.1', '', '0a4a6631-6406-4da5-b6b1-cfed7f1849d6', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (309, 'client.rm.sagaBranchRegisterEnable', 'SEATA_GROUP', 'false', '68934a3e9455fa72420237eb05902327', '2023-01-10 18:13:45', '2023-01-10 18:13:45', NULL, '127.0.0.1', '', '0a4a6631-6406-4da5-b6b1-cfed7f1849d6', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (310, 'client.tm.commitRetryCount', 'SEATA_GROUP', '5', 'e4da3b7fbbce2345d7772b0674a318d5', '2023-01-10 18:13:46', '2023-01-10 18:13:46', NULL, '127.0.0.1', '', '0a4a6631-6406-4da5-b6b1-cfed7f1849d6', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (311, 'client.tm.rollbackRetryCount', 'SEATA_GROUP', '5', 'e4da3b7fbbce2345d7772b0674a318d5', '2023-01-10 18:13:46', '2023-01-10 18:13:46', NULL, '127.0.0.1', '', '0a4a6631-6406-4da5-b6b1-cfed7f1849d6', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (312, 'store.mode', 'SEATA_GROUP', 'db', 'd77d5e503ad1439f585ac494268b351b', '2023-01-10 18:13:46', '2023-01-10 18:13:46', NULL, '127.0.0.1', '', '0a4a6631-6406-4da5-b6b1-cfed7f1849d6', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (313, 'store.file.dir', 'SEATA_GROUP', 'file_store/data', '6a8dec07c44c33a8a9247cba5710bbb2', '2023-01-10 18:13:47', '2023-01-10 18:13:47', NULL, '127.0.0.1', '', '0a4a6631-6406-4da5-b6b1-cfed7f1849d6', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (314, 'store.file.maxBranchSessionSize', 'SEATA_GROUP', '16384', 'c76fe1d8e08462434d800487585be217', '2023-01-10 18:13:47', '2023-01-10 18:13:47', NULL, '127.0.0.1', '', '0a4a6631-6406-4da5-b6b1-cfed7f1849d6', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (315, 'store.file.maxGlobalSessionSize', 'SEATA_GROUP', '512', '10a7cdd970fe135cf4f7bb55c0e3b59f', '2023-01-10 18:13:48', '2023-01-10 18:13:48', NULL, '127.0.0.1', '', '0a4a6631-6406-4da5-b6b1-cfed7f1849d6', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (316, 'store.file.fileWriteBufferCacheSize', 'SEATA_GROUP', '16384', 'c76fe1d8e08462434d800487585be217', '2023-01-10 18:13:48', '2023-01-10 18:13:48', NULL, '127.0.0.1', '', '0a4a6631-6406-4da5-b6b1-cfed7f1849d6', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (317, 'store.file.flushDiskMode', 'SEATA_GROUP', 'async', '0df93e34273b367bb63bad28c94c78d5', '2023-01-10 18:13:48', '2023-01-10 18:13:48', NULL, '127.0.0.1', '', '0a4a6631-6406-4da5-b6b1-cfed7f1849d6', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (318, 'store.file.sessionReloadReadSize', 'SEATA_GROUP', '100', 'f899139df5e1059396431415e770c6dd', '2023-01-10 18:13:49', '2023-01-10 18:13:49', NULL, '127.0.0.1', '', '0a4a6631-6406-4da5-b6b1-cfed7f1849d6', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (319, 'store.db.datasource', 'SEATA_GROUP', 'druid', '3d650fb8a5df01600281d48c47c9fa60', '2023-01-10 18:13:49', '2023-01-10 18:13:49', NULL, '127.0.0.1', '', '0a4a6631-6406-4da5-b6b1-cfed7f1849d6', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (320, 'store.db.dbType', 'SEATA_GROUP', 'mysql', '81c3b080dad537de7e10e0987a4bf52e', '2023-01-10 18:13:50', '2023-01-10 18:13:50', NULL, '127.0.0.1', '', '0a4a6631-6406-4da5-b6b1-cfed7f1849d6', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (321, 'store.db.driverClassName', 'SEATA_GROUP', 'com.mysql.jdbc.Driver', '683cf0c3a5a56cec94dfac94ca16d760', '2023-01-10 18:13:50', '2023-01-10 18:13:50', NULL, '127.0.0.1', '', '0a4a6631-6406-4da5-b6b1-cfed7f1849d6', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (322, 'store.db.url', 'SEATA_GROUP', 'jdbc:mysql://192.168.1.28:3306/seata?useUnicode=true', '1aef691be1298905acd89291c204ef55', '2023-01-10 18:13:50', '2023-01-10 18:13:50', NULL, '127.0.0.1', '', '0a4a6631-6406-4da5-b6b1-cfed7f1849d6', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (323, 'store.db.user', 'SEATA_GROUP', 'root', '63a9f0ea7bb98050796b649e85481845', '2023-01-10 18:13:51', '2023-01-10 18:13:51', NULL, '127.0.0.1', '', '0a4a6631-6406-4da5-b6b1-cfed7f1849d6', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (324, 'store.db.password', 'SEATA_GROUP', 'Flyedt@2021', '23f925ea5542ce543d4336cf7ffe9a6f', '2023-01-10 18:13:51', '2023-01-10 18:13:51', NULL, '127.0.0.1', '', '0a4a6631-6406-4da5-b6b1-cfed7f1849d6', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (325, 'store.db.minConn', 'SEATA_GROUP', '5', 'e4da3b7fbbce2345d7772b0674a318d5', '2023-01-10 18:13:51', '2023-01-10 18:13:51', NULL, '127.0.0.1', '', '0a4a6631-6406-4da5-b6b1-cfed7f1849d6', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (326, 'store.db.maxConn', 'SEATA_GROUP', '30', '34173cb38f07f89ddbebc2ac9128303f', '2023-01-10 18:13:52', '2023-01-10 18:13:52', NULL, '127.0.0.1', '', '0a4a6631-6406-4da5-b6b1-cfed7f1849d6', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (327, 'store.db.globalTable', 'SEATA_GROUP', 'global_table', '8b28fb6bb4c4f984df2709381f8eba2b', '2023-01-10 18:13:52', '2023-01-10 18:13:52', NULL, '127.0.0.1', '', '0a4a6631-6406-4da5-b6b1-cfed7f1849d6', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (328, 'store.db.branchTable', 'SEATA_GROUP', 'branch_table', '54bcdac38cf62e103fe115bcf46a660c', '2023-01-10 18:13:53', '2023-01-10 18:13:53', NULL, '127.0.0.1', '', '0a4a6631-6406-4da5-b6b1-cfed7f1849d6', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (329, 'store.db.queryLimit', 'SEATA_GROUP', '100', 'f899139df5e1059396431415e770c6dd', '2023-01-10 18:13:53', '2023-01-10 18:13:53', NULL, '127.0.0.1', '', '0a4a6631-6406-4da5-b6b1-cfed7f1849d6', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (330, 'store.db.lockTable', 'SEATA_GROUP', 'lock_table', '55e0cae3b6dc6696b768db90098b8f2f', '2023-01-10 18:13:53', '2023-01-10 18:13:53', NULL, '127.0.0.1', '', '0a4a6631-6406-4da5-b6b1-cfed7f1849d6', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (331, 'store.db.maxWait', 'SEATA_GROUP', '5000', 'a35fe7f7fe8217b4369a0af4244d1fca', '2023-01-10 18:13:54', '2023-01-10 18:13:54', NULL, '127.0.0.1', '', '0a4a6631-6406-4da5-b6b1-cfed7f1849d6', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (332, 'server.recovery.committingRetryPeriod', 'SEATA_GROUP', '1000', 'a9b7ba70783b617e9998dc4dd82eb3c5', '2023-01-10 18:13:54', '2023-01-10 18:13:54', NULL, '127.0.0.1', '', '0a4a6631-6406-4da5-b6b1-cfed7f1849d6', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (333, 'server.recovery.asynCommittingRetryPeriod', 'SEATA_GROUP', '1000', 'a9b7ba70783b617e9998dc4dd82eb3c5', '2023-01-10 18:13:54', '2023-01-10 18:13:54', NULL, '127.0.0.1', '', '0a4a6631-6406-4da5-b6b1-cfed7f1849d6', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (334, 'server.recovery.rollbackingRetryPeriod', 'SEATA_GROUP', '1000', 'a9b7ba70783b617e9998dc4dd82eb3c5', '2023-01-10 18:13:55', '2023-01-10 18:13:55', NULL, '127.0.0.1', '', '0a4a6631-6406-4da5-b6b1-cfed7f1849d6', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (335, 'server.recovery.timeoutRetryPeriod', 'SEATA_GROUP', '1000', 'a9b7ba70783b617e9998dc4dd82eb3c5', '2023-01-10 18:13:55', '2023-01-10 18:13:55', NULL, '127.0.0.1', '', '0a4a6631-6406-4da5-b6b1-cfed7f1849d6', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (336, 'server.maxCommitRetryTimeout', 'SEATA_GROUP', '-1', '6bb61e3b7bce0931da574d19d1d82c88', '2023-01-10 18:13:56', '2023-01-10 18:13:56', NULL, '127.0.0.1', '', '0a4a6631-6406-4da5-b6b1-cfed7f1849d6', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (337, 'server.maxRollbackRetryTimeout', 'SEATA_GROUP', '-1', '6bb61e3b7bce0931da574d19d1d82c88', '2023-01-10 18:13:56', '2023-01-10 18:13:56', NULL, '127.0.0.1', '', '0a4a6631-6406-4da5-b6b1-cfed7f1849d6', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (338, 'server.rollbackRetryTimeoutUnlockEnable', 'SEATA_GROUP', 'false', '68934a3e9455fa72420237eb05902327', '2023-01-10 18:13:56', '2023-01-10 18:13:56', NULL, '127.0.0.1', '', '0a4a6631-6406-4da5-b6b1-cfed7f1849d6', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (339, 'client.undo.dataValidation', 'SEATA_GROUP', 'true', 'b326b5062b2f0e69046810717534cb09', '2023-01-10 18:13:57', '2023-01-10 18:13:57', NULL, '127.0.0.1', '', '0a4a6631-6406-4da5-b6b1-cfed7f1849d6', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (340, 'client.undo.logSerialization', 'SEATA_GROUP', 'jackson', 'b41779690b83f182acc67d6388c7bac9', '2023-01-10 18:13:57', '2023-01-10 18:13:57', NULL, '127.0.0.1', '', '0a4a6631-6406-4da5-b6b1-cfed7f1849d6', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (341, 'server.undo.logSaveDays', 'SEATA_GROUP', '7', '8f14e45fceea167a5a36dedd4bea2543', '2023-01-10 18:13:58', '2023-01-10 18:13:58', NULL, '127.0.0.1', '', '0a4a6631-6406-4da5-b6b1-cfed7f1849d6', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (342, 'server.undo.logDeletePeriod', 'SEATA_GROUP', '86400000', 'f4c122804fe9076cb2710f55c3c6e346', '2023-01-10 18:13:58', '2023-01-10 18:13:58', NULL, '127.0.0.1', '', '0a4a6631-6406-4da5-b6b1-cfed7f1849d6', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (343, 'client.undo.logTable', 'SEATA_GROUP', 'undo_log', '2842d229c24afe9e61437135e8306614', '2023-01-10 18:13:58', '2023-01-10 18:13:58', NULL, '127.0.0.1', '', '0a4a6631-6406-4da5-b6b1-cfed7f1849d6', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (344, 'client.log.exceptionRate', 'SEATA_GROUP', '100', 'f899139df5e1059396431415e770c6dd', '2023-01-10 18:13:59', '2023-01-10 18:13:59', NULL, '127.0.0.1', '', '0a4a6631-6406-4da5-b6b1-cfed7f1849d6', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (345, 'transport.serialization', 'SEATA_GROUP', 'seata', 'b943081c423b9a5416a706524ee05d40', '2023-01-10 18:13:59', '2023-01-10 18:13:59', NULL, '127.0.0.1', '', '0a4a6631-6406-4da5-b6b1-cfed7f1849d6', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (346, 'transport.compressor', 'SEATA_GROUP', 'none', '334c4a4c42fdb79d7ebc3e73b517e6f8', '2023-01-10 18:13:59', '2023-01-10 18:13:59', NULL, '127.0.0.1', '', '0a4a6631-6406-4da5-b6b1-cfed7f1849d6', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (347, 'metrics.enabled', 'SEATA_GROUP', 'false', '68934a3e9455fa72420237eb05902327', '2023-01-10 18:14:00', '2023-01-10 18:14:00', NULL, '127.0.0.1', '', '0a4a6631-6406-4da5-b6b1-cfed7f1849d6', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (348, 'metrics.registryType', 'SEATA_GROUP', 'compact', '7cf74ca49c304df8150205fc915cd465', '2023-01-10 18:14:00', '2023-01-10 18:14:00', NULL, '127.0.0.1', '', '0a4a6631-6406-4da5-b6b1-cfed7f1849d6', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (349, 'metrics.exporterList', 'SEATA_GROUP', 'prometheus', 'e4f00638b8a10e6994e67af2f832d51c', '2023-01-10 18:14:01', '2023-01-10 18:14:01', NULL, '127.0.0.1', '', '0a4a6631-6406-4da5-b6b1-cfed7f1849d6', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (350, 'metrics.exporterPrometheusPort', 'SEATA_GROUP', '9898', '7b9dc501afe4ee11c56a4831e20cee71', '2023-01-10 18:14:01', '2023-01-10 18:14:01', NULL, '127.0.0.1', '', '0a4a6631-6406-4da5-b6b1-cfed7f1849d6', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `config_info` VALUES (351, 'plan-flow-rules', 'DEFAULT_GROUP', '[\r\n  {\r\n    \"resource\": \"/base/customer/list\",\r\n    \"limitApp\": \"default\",\r\n    \"grade\": 1,\r\n    \"count\": 1,\r\n    \"clusterMode\": false,\r\n    \"controlBehavior\": 0,\r\n    \"strategy\": 0,\r\n    \"warmUpPeriodSec\": 10,\r\n    \"maxQueueingTimeMs\": 500,\r\n    \"refResource\": \"rrr\"\r\n  }\r\n]', 'd7b6af2599a4c2c6d44329dc3434424b', '2023-01-11 10:31:43', '2023-01-11 15:27:07', NULL, '221.3.20.86', '', '', 'plan-flow-rules', 'null', 'null', 'json', 'null');
INSERT INTO `config_info` VALUES (352, 'plan-degrade-rules', 'DEFAULT_GROUP', '[\r\n  {\r\n    \"resource\": \"/base/customer/list\",\r\n    \"limitApp\": \"default\",\r\n    \"grade\": 0,\r\n    \"count\": 200,\r\n    \"slowRatioThreshold\": 0.2,\r\n    \"minRequestAmount\": 5,\r\n    \"statIntervalMs\": 1000,\r\n    \"timeWindow\": 10\r\n  }\r\n]', '8486142354bc67c6d1132df2d9292b86', '2023-01-11 10:31:43', '2023-01-11 14:14:31', NULL, '221.3.20.86', '', '', 'null', 'null', 'null', 'json', 'null');
INSERT INTO `config_info` VALUES (353, 'plan-system-rules', 'DEFAULT_GROUP', '  [\r\n    {\r\n      \"avgRt\": 1,\r\n      \"highestCpuUsage\": -1,\r\n      \"highestSystemLoad\": -1,\r\n      \"maxThread\": -1,\r\n      \"qps\": -1\r\n    }\r\n  ]', '025fee8a910b0e1faddbdd47096e51cd', '2023-01-11 10:31:43', '2023-01-11 10:31:43', NULL, '221.3.20.86', '', '', NULL, NULL, NULL, 'json', NULL);
INSERT INTO `config_info` VALUES (354, 'plan-authority-rules', 'DEFAULT_GROUP', '[\r\n  {\r\n    \"resource\": \"sentinel_spring_web_context\",\r\n    \"limitApp\": \"/base/customer/list\",\r\n    \"strategy\": 0\r\n  }\r\n]', '61654d98379ecca655ad901c0737f684', '2023-01-11 10:31:43', '2023-01-11 14:14:48', NULL, '221.3.20.86', '', '', 'null', 'null', 'null', 'json', 'null');
INSERT INTO `config_info` VALUES (355, 'plan-param-flow-rules', 'DEFAULT_GROUP', '[\r\n  {\r\n    \"resource\": \"/base/customer/list\",\r\n    \"grade\": 1,\r\n    \"paramIdx\": 0,\r\n    \"count\": 13,\r\n    \"durationInSec\": 6,\r\n    \"clusterMode\": false,\r\n    \"burstCount\": 0,\r\n    \"clusterConfig\": {\r\n      \"fallbackToLocalWhenFail\": true,\r\n      \"flowId\": 2,\r\n      \"sampleCount\": 10,\r\n      \"thresholdType\": 0,\r\n      \"windowIntervalMs\": 1000\r\n    },\r\n    \"controlBehavior\": 0,\r\n    \"limitApp\": \"default\",\r\n    \"maxQueueingTimeMs\": 0,\r\n    \"paramFlowItemList\": [\r\n      {\r\n        \"classType\": \"int\",\r\n        \"count\": 222,\r\n        \"object\": \"2\"\r\n      }\r\n    ]\r\n  }\r\n]', '8c2ff21e28337baff762dbb2ece8b51d', '2023-01-11 10:31:43', '2023-01-11 14:15:09', NULL, '221.3.20.86', '', '', 'null', 'null', 'null', 'json', 'null');

-- ----------------------------
-- Table structure for config_info_aggr
-- ----------------------------
DROP TABLE IF EXISTS `config_info_aggr`;
CREATE TABLE `config_info_aggr`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `datum_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'datum_id',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '内容',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '租户字段',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfoaggr_datagrouptenantdatum`(`data_id`, `group_id`, `tenant_id`, `datum_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '增加租户字段' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of config_info_aggr
-- ----------------------------

-- ----------------------------
-- Table structure for config_info_beta
-- ----------------------------
DROP TABLE IF EXISTS `config_info_beta`;
CREATE TABLE `config_info_beta`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'app_name',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'content',
  `beta_ips` varchar(1024) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'betaIps',
  `md5` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT 'source user',
  `src_ip` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'source ip',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '租户字段',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfobeta_datagrouptenant`(`data_id`, `group_id`, `tenant_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'config_info_beta' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of config_info_beta
-- ----------------------------

-- ----------------------------
-- Table structure for config_info_route
-- ----------------------------
DROP TABLE IF EXISTS `config_info_route`;
CREATE TABLE `config_info_route`  (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `route_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '路由id',
  `uri` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'uri路径',
  `predicates` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '判定器',
  `filters` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '过滤器',
  `orders` int(11) NULL DEFAULT NULL COMMENT '排序',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `intercept` int(1) NOT NULL DEFAULT 1 COMMENT '是否拦截 1 是 0 否',
  `status` int(1) NOT NULL DEFAULT 1 COMMENT '状态：Y-有效，N-无效',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `ux_gateway_routes_uri`(`uri`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 125 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '网关路由表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of config_info_route
-- ----------------------------
INSERT INTO `config_info_route` VALUES (101, 'authorization', 'lb://authorization', '[{\"name\":\"Path\",\"args\":{\"pattern\":\"/authorization*/**\"}}]', '[]', 100, '用户认证相关接口', 1, 1, '2020-04-21 17:00:32', '2020-04-21 17:00:32');
INSERT INTO `config_info_route` VALUES (102, 'admin', 'lb://admin', '[{\"name\":\"Path\",\"args\":{\"pattern\":\"/admin*/**\"}}]', '[]', 100, '系统管理相关接口', 1, 1, '2020-04-21 17:00:32', '2020-04-21 17:00:32');
INSERT INTO `config_info_route` VALUES (103, 'login', 'lb://authorization/login', '[{\"name\":\"Path\",\"args\":{\"pattern\":\"/login\"}}]', '[]', 100, '用户登录相关接口', 0, 1, '2020-04-21 17:00:32', '2020-04-21 17:00:32');
INSERT INTO `config_info_route` VALUES (104, 'logout', 'lb://authorization/logout', '[{\"name\":\"Path\",\"args\":{\"pattern\":\"/logout\"}}]', '[]', 100, '用户退出相关接口', 0, 1, '2020-04-21 17:00:32', '2020-04-21 17:00:32');
INSERT INTO `config_info_route` VALUES (105, 'reLogin', 'lb://authorization/reLogin', '[{\"name\":\"Path\",\"args\":{\"pattern\":\"/reLogin\"}}]', '[]', 100, '用户重新登录相关接口', 0, 1, '2020-04-21 17:00:32', '2020-04-21 17:00:32');
INSERT INTO `config_info_route` VALUES (106, 'crm', 'lb://crm', '[{\"name\":\"Path\",\"args\":{\"pattern\":\"/crm*/**\"}}]', '[]', 100, '客户管理相关接口', 1, 1, '2020-04-21 17:00:32', '2020-04-21 17:00:32');
INSERT INTO `config_info_route` VALUES (107, 'hrm', 'lb://hrm', '[{\"name\":\"Path\",\"args\":{\"pattern\":\"/hrm*/**\"}}]', '[]', 100, '人力资源相关接口', 1, 1, '2020-04-21 17:00:32', '2020-04-21 17:00:32');
INSERT INTO `config_info_route` VALUES (108, 'jxc', 'lb://jxc', '[{\"name\":\"Path\",\"args\":{\"pattern\":\"/jxc*/**\"}}]', '[]', 100, '进销存相关接口', 1, 1, '2020-04-21 17:00:32', '2020-04-21 17:00:32');
INSERT INTO `config_info_route` VALUES (109, 'work', 'lb://work', '[{\"name\":\"Path\",\"args\":{\"pattern\":\"/work*/**\"}}]', '[]', 100, '项目管理相关接口', 1, 1, '2020-04-21 17:00:32', '2020-04-21 17:00:32');
INSERT INTO `config_info_route` VALUES (113, 'oa', 'lb://oa', '[{\"name\":\"Path\",\"args\":{\"pattern\":\"/oa*/**\"}}]', '[]', 100, 'OA相关接口', 1, 1, '2020-04-21 17:00:32', '2020-04-21 17:00:32');
INSERT INTO `config_info_route` VALUES (114, 'email', 'lb://email', '[{\"name\":\"Path\",\"args\":{\"pattern\":\"/email*/**\"}}]', '[]', 100, '邮箱相关接口', 1, 1, '2020-04-21 17:00:32', '2020-04-21 17:00:32');
INSERT INTO `config_info_route` VALUES (115, 'km', 'lb://km', '[{\"name\":\"Path\",\"args\":{\"pattern\":\"/km*/**\"}}]', '[]', 100, '知识库相关接口', 1, 1, '2020-04-21 17:00:32', '2020-04-21 17:00:32');
INSERT INTO `config_info_route` VALUES (116, 'bi', 'lb://bi', '[{\"name\":\"Path\",\"args\":{\"pattern\":\"/bi*/**\"}}]', '[]', 100, '商业智能相关接口', 1, 1, '2020-04-21 17:00:32', '2020-04-21 17:00:32');
INSERT INTO `config_info_route` VALUES (117, 'file', 'http://127.0.0.1:8012/onlinePreview', '[{\"name\":\"Path\",\"args\":{\"pattern\":\"/onlinePreview\"}}]', '[]', 100, '文件预览相关接口', 0, 1, '2020-04-21 17:00:32', '2020-04-21 17:00:32');
INSERT INTO `config_info_route` VALUES (118, 'queryShareUrl', 'lb://km', '[{\"name\":\"Path\",\"args\":{\"pattern\":\"/documentShare/queryShareUrl/*\"}}]', '[]', 100, '知识库分享接口', 0, 1, '2020-04-21 17:00:32', '2020-04-21 17:00:32');
INSERT INTO `config_info_route` VALUES (119, 'crmCallUpload', 'lb://crmCall/upload', '[{\"name\":\"Path\",\"args\":{\"pattern\":\"crmCall/upload/*\"}}]', '[]', 100, '呼叫中心上传接口', 0, 1, '2020-04-21 17:00:32', '2020-04-21 17:00:32');
INSERT INTO `config_info_route` VALUES (123, 'permission', 'lb://authorization/permission', '[{\"name\":\"Path\",\"args\":{\"pattern\":\"/permission\"}}]', '[]', 100, '用户权限验证接口', 0, 1, '2020-04-21 17:00:32', '2020-04-21 17:00:32');
INSERT INTO `config_info_route` VALUES (124, 'examine', 'lb://examine', '[{\"name\":\"Path\",\"args\":{\"pattern\":\"/examine*/**\"}}]', '[]', 100, '审批相关接口', 1, 1, '2020-04-21 17:00:32', '2020-04-21 17:00:32');

-- ----------------------------
-- Table structure for config_info_tag
-- ----------------------------
DROP TABLE IF EXISTS `config_info_tag`;
CREATE TABLE `config_info_tag`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT 'tenant_id',
  `tag_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'tag_id',
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'app_name',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'content',
  `md5` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT 'source user',
  `src_ip` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'source ip',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfotag_datagrouptenanttag`(`data_id`, `group_id`, `tenant_id`, `tag_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'config_info_tag' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of config_info_tag
-- ----------------------------

-- ----------------------------
-- Table structure for config_tags_relation
-- ----------------------------
DROP TABLE IF EXISTS `config_tags_relation`;
CREATE TABLE `config_tags_relation`  (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `tag_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'tag_name',
  `tag_type` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'tag_type',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT 'tenant_id',
  `nid` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`nid`) USING BTREE,
  UNIQUE INDEX `uk_configtagrelation_configidtag`(`id`, `tag_name`, `tag_type`) USING BTREE,
  INDEX `idx_tenant_id`(`tenant_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'config_tag_relation' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of config_tags_relation
-- ----------------------------

-- ----------------------------
-- Table structure for group_capacity
-- ----------------------------
DROP TABLE IF EXISTS `group_capacity`;
CREATE TABLE `group_capacity`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT 'Group ID，空字符表示整个集群',
  `quota` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '配额，0表示使用默认值',
  `usage` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '使用量',
  `max_size` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
  `max_aggr_count` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '聚合子配置最大个数，，0表示使用默认值',
  `max_aggr_size` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
  `max_history_count` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '最大变更历史数量',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_group_id`(`group_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '集群、各Group容量信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of group_capacity
-- ----------------------------

-- ----------------------------
-- Table structure for his_config_info
-- ----------------------------
DROP TABLE IF EXISTS `his_config_info`;
CREATE TABLE `his_config_info`  (
  `id` bigint(64) UNSIGNED NOT NULL,
  `nid` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'app_name',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `md5` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `src_user` text CHARACTER SET utf8 COLLATE utf8_bin NULL,
  `src_ip` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `op_type` char(10) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '租户字段',
  PRIMARY KEY (`nid`) USING BTREE,
  INDEX `idx_gmt_create`(`gmt_create`) USING BTREE,
  INDEX `idx_gmt_modified`(`gmt_modified`) USING BTREE,
  INDEX `idx_did`(`data_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '多租户改造' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of his_config_info
-- ----------------------------

-- ----------------------------
-- Table structure for permissions
-- ----------------------------
DROP TABLE IF EXISTS `permissions`;
CREATE TABLE `permissions`  (
  `role` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `resource` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `action` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  UNIQUE INDEX `uk_role_permission`(`role`, `resource`, `action`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of permissions
-- ----------------------------

-- ----------------------------
-- Table structure for roles
-- ----------------------------
DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles`  (
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `role` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  UNIQUE INDEX `idx_user_role`(`username`, `role`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of roles
-- ----------------------------
INSERT INTO `roles` VALUES ('nacos', 'ROLE_ADMIN');

-- ----------------------------
-- Table structure for tenant_capacity
-- ----------------------------
DROP TABLE IF EXISTS `tenant_capacity`;
CREATE TABLE `tenant_capacity`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT 'Tenant ID',
  `quota` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '配额，0表示使用默认值',
  `usage` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '使用量',
  `max_size` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
  `max_aggr_count` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '聚合子配置最大个数',
  `max_aggr_size` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
  `max_history_count` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '最大变更历史数量',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_tenant_id`(`tenant_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '租户容量信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of tenant_capacity
-- ----------------------------

-- ----------------------------
-- Table structure for tenant_info
-- ----------------------------
DROP TABLE IF EXISTS `tenant_info`;
CREATE TABLE `tenant_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `kp` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'kp',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT 'tenant_id',
  `tenant_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT 'tenant_name',
  `tenant_desc` varchar(256) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'tenant_desc',
  `create_source` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'create_source',
  `gmt_create` bigint(20) NOT NULL COMMENT '创建时间',
  `gmt_modified` bigint(20) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_tenant_info_kptenantid`(`kp`, `tenant_id`) USING BTREE,
  INDEX `idx_tenant_id`(`tenant_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'tenant_info' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of tenant_info
-- ----------------------------
INSERT INTO `tenant_info` VALUES (1, '1', 'ab16776b-dae9-474e-8943-7bbbc627b637', 'service-dev', '开发环境', 'nacos', 1672218090893, 1673344780150);
INSERT INTO `tenant_info` VALUES (6, '1', '3175f583-cd9b-4a0d-9991-f57ab23f5918', 'sentinel-dev', 'sentinel', 'nacos', 1673310608747, 1673344706738);
INSERT INTO `tenant_info` VALUES (8, '1', '35f21db9-107d-4ecf-93f7-e5165847232e', 'seata-dev', 'seata-dev', 'nacos', 1673335905926, 1673344718975);
INSERT INTO `tenant_info` VALUES (9, '1', '0a4a6631-6406-4da5-b6b1-cfed7f1849d6', 'seata-crm', 'seata-crm', 'nacos', 1673345298016, 1673345298016);

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `password` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `enabled` tinyint(1) NOT NULL,
  PRIMARY KEY (`username`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES ('nacos', '$2a$10$EuWPZHzz32dJN7jexM34MOeYirDdFAZm2kuWj7VEOJhhZkDrxfvUu', 1);

SET FOREIGN_KEY_CHECKS = 1;
