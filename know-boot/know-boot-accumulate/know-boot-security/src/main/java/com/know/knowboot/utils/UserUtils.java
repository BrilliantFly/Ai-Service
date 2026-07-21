package com.know.knowboot.utils;

import com.know.knowboot.bean.AuthorityUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserUtils {

    /**
     * 获取当前登录用户信息
     * @return
     */
    public static AuthorityUser getCurrentUser() {
        // 获取登录信息
        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        AuthorityUser authorityUser = (AuthorityUser) authenticationToken.getDetails();
        return authorityUser;
    }

}
