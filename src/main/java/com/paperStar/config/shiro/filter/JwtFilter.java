package com.paperStar.config.shiro.filter;

import com.paperStar.config.shiro.jwt.JwtToken;
import com.paperStar.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

@Slf4j
public class JwtFilter extends BasicHttpAuthenticationFilter {

    //处理登录、注册请求,true直接放行，false则执行onAccessDenied
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        //判断请求的请求头是否带上 "Token"
        if (isLoginAttempt(request, response)) {
            log.info("请求头中存在token,将使用onAccessDenied进行认证");
            return false;
        }
        //如果请求头不存在 Token，则可能是执行登陆操作或者是游客状态访问，无需检查 token，直接返回 true
        log.info("请求头中没有token，请求的是register或login接口，直接放行");
        return true;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse, Object mappedValue) throws Exception {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String jwt = request.getHeader("token");
        log.info("----jwtFilter的onAccessDenied()方法----");
        log.info("请求的 Header 中含有 jwtToken {}", jwt);
        JwtToken jwtToken = new JwtToken(jwt);

        try {
            // 委托 realm 进行登录认证
            //所以这个地方最终还是调用JwtRealm进行的认证
            getSubject(request, servletResponse).login(jwtToken);
        } catch (Exception e) {
            e.printStackTrace();
            onLoginFail(servletResponse);
            //调用下面的方法向客户端返回错误信息
            return false;
        }
        return true;

    }



    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        HttpServletRequest req = (HttpServletRequest) request;
        String token = req.getHeader("token");
        if(token == null){
            return false;
        }
        return !token.isEmpty();
    }

    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String token = httpServletRequest.getHeader("token");
        JwtToken jwtToken = new JwtToken(token);
        // 提交给realm进行登入，如果错误他会抛出异常并被捕获
        getSubject(request, response).login(jwtToken);
        // 如果没有抛出异常则代表登入成功，返回true
        return true;
    }


    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        return super.preHandle(request, response);
    }

    private void responseError(ServletResponse response,String message){
        try {
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            //设置编码，否则中文字符在重定向时会变为空字符串
            message = URLEncoder.encode(message, "UTF-8");
            httpServletResponse.sendRedirect("/unauthorized/" + message);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void onLoginFail(ServletResponse response) throws IOException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        httpResponse.getWriter().write(Result.error("login error").toString());
    }

}
