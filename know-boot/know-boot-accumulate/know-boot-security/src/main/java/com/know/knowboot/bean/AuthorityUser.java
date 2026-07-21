package com.know.knowboot.bean;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class AuthorityUser {

    /**
     * 用户ID
     */
    private String id;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 角色
     */
    private List<String> roles = new ArrayList<>();

    /**
     * 按钮
     */
    private List<String> btnPermission = new ArrayList<>();

    /**
     * 权限
     */
    private Map<String, List<String>> permission = new HashMap<>();

}
