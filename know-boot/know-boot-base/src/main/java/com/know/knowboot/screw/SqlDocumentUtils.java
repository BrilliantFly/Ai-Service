package com.know.knowboot.screw;

import cn.smallbun.screw.core.Configuration;
import cn.smallbun.screw.core.engine.EngineConfig;
import cn.smallbun.screw.core.engine.EngineFileType;
import cn.smallbun.screw.core.engine.EngineTemplateType;
import cn.smallbun.screw.core.execute.DocumentationExecute;
import cn.smallbun.screw.core.process.ProcessConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

public class SqlDocumentUtils {

    /**
     * 获取引擎
     * @param sqlAutoParam
     * @return
     */
    public static EngineConfig getEngineConfigByFileType(SqlAutoParam sqlAutoParam){
        if (StringUtils.isNotBlank(sqlAutoParam.getFileType())){
            if (sqlAutoParam.getFileType().equals(FileTypeEnum.WORD.getCode())){
                // 生成配置
                EngineConfig engineConfig = EngineConfig.builder()
                        // 生成文件路径
                        .fileOutputDir(sqlAutoParam.getFileOutputDir())
                        // 打开目录 设置为true执行完代码后会自动打开对应路径文件夹
                        .openOutputDir(false)
                        // 文件类型,目前有doc跟html两种类型(WORD为doc文档，MD为Markdown文档，HTML则为HTML的网页格式)
                        .fileType(EngineFileType.WORD)
                        // 生成模板实现
                        .produceType(EngineTemplateType.freemarker).build();
                return engineConfig;
            }else if (sqlAutoParam.getFileType().equals(FileTypeEnum.HTML.getCode())){
                // 生成配置
                EngineConfig engineConfig = EngineConfig.builder()
                        // 生成文件路径
                        .fileOutputDir(sqlAutoParam.getFileOutputDir())
                        // 打开目录 设置为true执行完代码后会自动打开对应路径文件夹
                        .openOutputDir(true)
                        // 文件类型,目前有doc跟html两种类型(WORD为doc文档，MD为Markdown文档，HTML则为HTML的网页格式)
                        .fileType(EngineFileType.HTML)
                        // 生成模板实现
                        .produceType(EngineTemplateType.freemarker).build();
                return engineConfig;
            }else {
                // 生成配置
                EngineConfig engineConfig = EngineConfig.builder()
                        // 生成文件路径
                        .fileOutputDir(sqlAutoParam.getFileOutputDir())
                        // 打开目录 设置为true执行完代码后会自动打开对应路径文件夹
                        .openOutputDir(true)
                        // 文件类型,目前有doc跟html两种类型
                        .fileType(EngineFileType.WORD)
                        // 生成模板实现
                        .produceType(EngineTemplateType.freemarker).build();
                return engineConfig;
            }
        }

        // 生成配置
        EngineConfig engineConfig = EngineConfig.builder()
                // 生成文件路径
                .fileOutputDir(sqlAutoParam.getFileOutputDir())
                // 打开目录 设置为true执行完代码后会自动打开对应路径文件夹
                .openOutputDir(true)
                // 文件类型,目前有doc跟html两种类型
                .fileType(EngineFileType.WORD)
                // 生成模板实现
                .produceType(EngineTemplateType.freemarker).build();
        return engineConfig;
    }

    /**
     * 生成数据库文档
     * @param sqlAutoParam
     */
    public static void createSqlDocument (SqlAutoParam sqlAutoParam){
        // 数据源
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(sqlAutoParam.getDriverClassName());
        hikariConfig.setJdbcUrl(sqlAutoParam.getJdbcUrl());
        hikariConfig.setUsername(sqlAutoParam.getUserName());
        hikariConfig.setPassword(sqlAutoParam.getPassword());
        // 设置可以获取tables remarks信息
        hikariConfig.addDataSourceProperty("useInformationSchema", "true");
        hikariConfig.setMinimumIdle(2);
        hikariConfig.setMaximumPoolSize(5);
        DataSource dataSource = new HikariDataSource(hikariConfig);
        // 生成配置
        EngineConfig engineConfig = getEngineConfigByFileType(sqlAutoParam);

        // 忽略表,这些表不会在文档中生成
        ArrayList<String> ignoreTableName = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(sqlAutoParam.getIgnoreTableNames())){
            sqlAutoParam.getIgnoreTableNames().forEach(t -> {
                ignoreTableName.add(t);
            });
        }

        // 忽略表前缀,这些表不会在文档中生成
        ArrayList<String> ignorePrefix = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(sqlAutoParam.getIgnorePrefixs())){
            sqlAutoParam.getIgnorePrefixs().forEach(t -> {
                ignorePrefix.add(t);
            });
        }

        // 忽略表后缀,这些表不会在文档中生成
        ArrayList<String> ignoreSuffix = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(sqlAutoParam.getIgnoreSuffixs())){
            sqlAutoParam.getIgnoreSuffixs().forEach(t -> {
                ignoreSuffix.add(t);
            });
        }


        ProcessConfig processConfig = ProcessConfig.builder()
                // 忽略表名
                .ignoreTableName(ignoreTableName)
                // 忽略表前缀
                .ignoreTablePrefix(ignorePrefix)
                // 忽略表后缀
                .ignoreTableSuffix(ignoreSuffix).build();
        // 配置
        Configuration config = Configuration.builder()
                // 版本
                .version(sqlAutoParam.getVersion())
                // 描述
                .description(sqlAutoParam.getDescription())
                // 数据源
                .dataSource(dataSource)
                // 生成配置
                .engineConfig(engineConfig)
                // 生成配置
                .produceConfig(processConfig).build();
        // 执行生成
        new DocumentationExecute(config).execute();
    }

    public static void main(String[] args) {

        // 忽略表
        List<String> ignoreTableNames = new ArrayList<>();
        ignoreTableNames.add("test_user");
        ignoreTableNames.add("test_group");

        // 忽略表前缀
        List<String> ignorePrefixs = new ArrayList<>();
        ignorePrefixs.add("test_");

        // 忽略表后缀
        List<String> ignoreSuffixs = new ArrayList<>();
        ignoreSuffixs.add("_test");

        SqlAutoParam sqlAutoParam = SqlAutoParam.builder()
                .driverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver") // 数据库驱动
                .jdbcUrl("jdbc:sqlserver://49.232.74.234:1433;DatabaseName=flyerp_qdmetro_20200422") // 数据库链接
                .userName("sa") //数据库名称
                .password("Flyedt@qd2022sql") //数据库密码
                .fileOutputDir("D:\\adb\\") //文档保存路径
                .ignoreTableNames(ignoreTableNames)
                .ignorePrefixs(ignorePrefixs)
                .ignoreSuffixs(ignoreSuffixs)
                .fileType(FileTypeEnum.WORD.getCode()) //格式：word
                .version("1.0.0") // 版本号
                .description("数据库文档1.0.0").build(); //文档描述

        SqlDocumentUtils.createSqlDocument(sqlAutoParam);

    }

}
