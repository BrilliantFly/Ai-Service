package com.know.knowboot.screw;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SqlAutoParam {

    /**
     * 数据库驱动
     */
    private String driverClassName;

    /**
     * 数据库链接
     */
    private String jdbcUrl;

    /**
     * 数据库用户名
     */
    private String userName;

    /**
     * 数据库密码
     */
    private String password;

    /**
     * 文档类型
     */
    private String fileType;

    /**
     * 忽略的表
     */
    private List<String> ignoreTableNames;

    /**
     * 忽略的表前缀
     */
    private List<String> ignorePrefixs;

    /**
     * 忽略表后缀
     */
    private List<String> ignoreSuffixs;

    /**
     * 版本号
     */
    private String version;

    /**
     * 描述
     */
    private String description;

    /**
     * 输出路径
     */
    private String fileOutputDir;

}
