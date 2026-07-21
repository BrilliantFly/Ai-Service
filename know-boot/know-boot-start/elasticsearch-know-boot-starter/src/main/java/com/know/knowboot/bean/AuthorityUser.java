package com.know.knowboot.bean;

import lombok.Data;

import java.util.List;

@Data
public class AuthorityUser {

    /**
     * 用户名
     */
    private String userName;

    /**
     * 角色
     */
    private List<String> roles;

    /**
     * 权限接口
     */
    private List<String> urls;

}
