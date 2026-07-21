//package com.know.knowboot.jwt.filter;
//
//import com.know.knowboot.jwt.service.JJwtService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//
//import javax.servlet.*;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@Slf4j
//public class JJwtFilter implements Filter {
//
//    private JJwtService jJwtService;
//
//    public JJwtFilter(JJwtService jJwtService) {
//        this.jJwtService = jJwtService;
//    }
//
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//
//    }
//
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//
//        // 转换
//        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
//        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
//        String token= httpServletRequest.getHeader("token");//获取请求头中的token
//        log.info("当前token为:{}",token);
//
////        String uri = httpServletRequest.getRequestURI();
////        if (uri.equals("/system/jjwt/createToken")){
////            //到下一个链，如果没有fiter便请求controller
////            //如果注销了，不能到达fiter，也不能请求controller
////            log.info("JwtFilter过滤器Uri执行前");
////            filterChain.doFilter(httpServletRequest, httpServletResponse);
////            log.info("JwtFilter过滤器Uri执行后");
////            //return 请求结束，如果不return，继续执行下面的方法
////            return;
////        }
////
////        try{
////            jJwtService.verify(token);//验证token
////            log.info("Jwt中Token通过");
////        }catch(ExpiredJwtException ex){
////            ex.printStackTrace();
////            log.info("Jwt中Token验证异常 -> {}","token过期");
////
////            //错误信息发送回前台
////            httpServletResponse.setContentType("application/json;charset=UTF-8");
////            httpServletResponse.getWriter().println("token过期");
////            return;
////        }catch(UnsupportedJwtException ex){
////            ex.printStackTrace();
////            log.info("Jwt中Token验证异常 -> {}","不支持Jwt");
////
////            //错误信息发送回前台
////            httpServletResponse.setContentType("application/json;charset=UTF-8");
////            httpServletResponse.getWriter().println("不支持Jwt");
////            return;
////        }catch(MalformedJwtException ex){
////            ex.printStackTrace();
////            log.info("Jwt中Token验证异常 -> {}","格式不正确");
////
////            //错误信息发送回前台
////            httpServletResponse.setContentType("application/json;charset=UTF-8");
////            httpServletResponse.getWriter().println("格式不正确");
////            return;
////        }catch(SignatureException ex){
////            ex.printStackTrace();
////            log.info("Jwt中Token验证异常 -> {}","无效签名");
////
////            //错误信息发送回前台
////            httpServletResponse.setContentType("application/json;charset=UTF-8");
////            httpServletResponse.getWriter().println("无效签名");
////            return;
////        }catch(IllegalArgumentException ex){
////            ex.printStackTrace();
////            log.info("Jwt中Token验证异常 -> {}","token算法不一致");
////
////            //错误信息发送回前台
////            httpServletResponse.setContentType("application/json;charset=UTF-8");
////            httpServletResponse.getWriter().println("token算法不一致");
////            return;
////        }catch(Exception ex){
////            ex.printStackTrace();
////            log.info("Jwt中Token验证异常 -> {}","token无效");
////
////            //错误信息发送回前台
////            httpServletResponse.setContentType("application/json;charset=UTF-8");
////            httpServletResponse.getWriter().println("token无效");
////            return;
////        }
//
//        log.info("JJwtFilter过滤器执行前");
//        //到下一个链，如果没有fiter便请求controller
//        //如果注销了，不能到达fiter，也不能请求controller
//        filterChain.doFilter(httpServletRequest, httpServletResponse);
//        log.info("JJwtFilter过滤器执行后");
//
//    }
//
//    @Override
//    public void destroy() {
//
//    }
//
//}
