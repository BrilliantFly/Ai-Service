package com.know.knowboot.entity;//package com.know.knowboot.example.tree.entity;
//
//import com.fasterxml.jackson.annotation.JsonFormat;
//import lombok.Data;
//import org.springframework.format.annotation.DateTimeFormat;
//
//import java.util.Date;
//
//@Data
//public class SysUserInfo {
//
//    /**登录账号**/
//    private String username;
//
//    /**真实姓名**/
//    private String realname;
//
//    /**生日**/
//    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")  //解析与返回时间参数，格式化json格式的 yyyy-MM-dd HH:mm:ss
//    @DateTimeFormat(pattern = "yyyy-MM-dd") //用于请求体非json格式的请求
//    private Date birthday;
//
//    /**电子邮件**/
//    private String email;
//
//    /**电话**/
//    private String phone;
//
//    /**状态 (1-正常,2-冻结)**/
//    private Integer status;
//
//    /**id**/
//    private String id;
//
//    /**删除状态（0，正常，1已删除）**/
//    private Integer delFlag;
//
//    /**创建人**/
//    private String createBy;
//
//    /**创建时间**/
//    private Date createTime;
//
//    /**更新人**/
//    private String updateBy;
//
//    /**更新时间**/
//    private Date updateTime;
//
//}
