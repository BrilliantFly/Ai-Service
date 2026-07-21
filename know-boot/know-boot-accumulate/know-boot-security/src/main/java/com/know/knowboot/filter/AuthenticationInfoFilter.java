package com.know.knowboot.filter;

import com.know.knowboot.bean.AuthorityUser;
import com.know.knowboot.redis.service.RedisService;
import com.know.knowboot.utils.ResponseUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.PatternMatchUtils;

import javax.naming.NoPermissionException;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 管理端token过滤
 */
@Slf4j
public class AuthenticationInfoFilter extends BasicAuthenticationFilter {

    private RedisService redisService;

    public AuthenticationInfoFilter(AuthenticationManager authenticationManager,
                                    RedisService redisService) {
        super(authenticationManager);
        this.redisService = redisService;

    }

    /**
     * 可以通过debug查看chain中的filter及执行顺序
     *
     * @param request
     * @param response
     * @param chain
     */
    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) {
        String requestUrl = request.getRequestURI();
        if (requestUrl.contains("login")) {
            chain.doFilter(request, response);
        }else {
            String jwt = request.getParameter("token");
            //验证token

            //获取用户信息，存入context
            UsernamePasswordAuthenticationToken authentication = getAuthentication(jwt, response);
            //自定义权限过滤
            if (authentication != null) {
                customAuthentication(request, response, authentication);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            chain.doFilter(request, response);
        }
    }

    /**
     * 自定义权限过滤
     *
     * @param request        请求
     * @param response       响应
     * @param authentication 用户信息
     */
    private void customAuthentication(HttpServletRequest request, HttpServletResponse response, UsernamePasswordAuthenticationToken authentication) throws NoPermissionException {

        AuthorityUser authUser = (AuthorityUser) authentication.getDetails();

        String requestUrl = request.getRequestURI();

        //获取缓存中的权限
        Map<String, List<String>> permission = authUser.getPermission();

        if (!match(permission.get("init"), requestUrl)) {
            ResponseUtil.output(response, ResponseUtil.resultMap(false, 400, "权限不足"));
            log.error("当前请求路径：{},所拥有权限：{}", requestUrl, permission.toString());
            throw new NoPermissionException("权限不足");
        }
    }

    /**
     * 校验权限
     *
     * @param permissions 权限集合
     * @param url         请求地址
     * @return 是否拥有权限
     */
    boolean match(List<String> permissions, String url) {
        if (permissions == null || permissions.isEmpty()) {
            return false;
        }
        return PatternMatchUtils.simpleMatch(permissions.toArray(new String[0]), url);
    }

    /**
     * 获取token信息
     *
     * @param jwt      token信息
     * @param response 响应
     * @return 获取鉴权对象
     */
    private UsernamePasswordAuthenticationToken getAuthentication(String jwt, HttpServletResponse response) {

        // 通过jwt获取AuthorityUser
        AuthorityUser authorityUser = (AuthorityUser) redisService.get(jwt);

        if (authorityUser != null) {
            List<String> roles = authorityUser.getRoles();
            //用户角色
            List<GrantedAuthority> auths = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(roles)) {
                roles.forEach(t -> {
                    auths.add(new SimpleGrantedAuthority("ROLE_" + t));
                });
            }
            //自定义权限（比如：按钮权限）
            List<String> btnPermission = authorityUser.getBtnPermission();
            if (CollectionUtils.isNotEmpty(btnPermission)) {
                btnPermission.forEach(t -> {
                    auths.add(new SimpleGrantedAuthority(t));
                });
            }
            //构造返回信息
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(authorityUser.getUserName(), null, auths);
            authentication.setDetails(authorityUser);
            return authentication;
        } else {
            //redis中没有权限 直接返回403，提示登录已失效，请重新登陆
            ResponseUtil.output(response, 403, ResponseUtil.resultMap(false, 403, "登录已失效，请重新登录"));
            return null;
        }

    }
}

