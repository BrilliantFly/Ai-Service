package com.know.knowboot.tokenbucket.filter;

import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Slf4j
@WebFilter(filterName = "tokenbucketFilter", urlPatterns = "/*")
public class TokenbucketFilter implements Filter {


    @Resource(name = "rateLimiterAllSmoothWarmingUpRate")
    private RateLimiter rateLimiter;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        String uri = httpServletRequest.getRequestURI();
        if (uri.equals("/system/tokenbucket/getTokenBucket")) {
            if (rateLimiter.tryAcquire()) {
                log.info("------------------ 令牌桶 - 获取令牌成功 ----------------");
            } else {
                log.info("------------------ 令牌桶 - 获取令牌失败 ----------------");
                //错误信息发送回前台
                httpServletResponse.setContentType("application/json;charset=UTF-8");
                httpServletResponse.getWriter().println("获取令牌失败");
                return;
            }
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    @Override
    public void destroy() {

    }

}
