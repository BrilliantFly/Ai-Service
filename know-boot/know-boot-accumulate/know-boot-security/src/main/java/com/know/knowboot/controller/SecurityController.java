package com.know.knowboot.controller;

import com.alibaba.fastjson.JSONObject;
import com.know.knowboot.bean.AuthorityUser;
import com.know.knowboot.jwt.enums.JwtEnum;
import com.know.knowboot.jwt.enums.PlatformEnum;
import com.know.knowboot.jwt.enums.SystemEnum;
import com.know.knowboot.jwt.service.JJwtService;
import com.know.knowboot.redis.service.RedisService;
import com.know.knowboot.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/authority")
public class SecurityController {

    @Resource
    private RedisService redisService;

    @Autowired
    private JJwtService jJwtService;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {

        log.info("------------------------------- login permission初始化开始 --------------------------------------");

        Map<String, List<String>> permission = new HashMap<>(2);
        List<String> superPermissions = new ArrayList<>();
        superPermissions.add("/security/authority/test");
        superPermissions.add("/security/authority/permission");
        permission.put("init", superPermissions);

        AuthorityUser authUser = new AuthorityUser();
        // 角色权限
        List<String> roles = new ArrayList<>();
        roles.add("admin");
        authUser.getRoles().addAll(roles);
        // 链接菜单权限
        authUser.setPermission(permission);
        // 按钮权限
        List<String> btnPermission = new ArrayList<>();
        btnPermission.add("permission:search");



        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtEnum.CLAIM_KEY_USER.getCode(),authUser);
        String token = jJwtService.generateToken(SystemEnum.KNOW, PlatformEnum.PC, claims);
        redisService.set(token,authUser);

        log.info("------------------------------- login permission初始化结束 --------------------------------------");

        return token;

    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test() {

        AuthorityUser authorityUser = UserUtils.getCurrentUser();
        log.info("---------------------------- 当前登录用户 -------------------------------> {}", JSONObject.toJSONString(authorityUser));

        return "hello world";

    }


//    @PreAuthorize("hasAuthority('permission:search')")
    @PreAuthorize("hasRole('admin1')")
    @RequestMapping(value = "/permission", method = RequestMethod.GET)
    public String permission() {

        AuthorityUser authorityUser = UserUtils.getCurrentUser();
        log.info("---------------------------- 当前登录用户 -------------------------------> {}", JSONObject.toJSONString(authorityUser));

        return "hello world";

    }

}
