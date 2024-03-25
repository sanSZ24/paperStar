package com.paperStar.config.shiro.jwt;

import lombok.Data;
import org.apache.shiro.authc.AuthenticationToken;

import java.security.Principal;

//使用jwtToken来进行对象发送至jwtRealm进行验证
@Data
public class JwtToken implements AuthenticationToken {

    private String token;

    public JwtToken(String jwt){
        this.token = jwt;
    }

    @Override
    //用户名
    public Object getPrincipal() {
        return token;
    }

    //密码
    @Override
    public Object getCredentials() {
        return token;
    }


}
