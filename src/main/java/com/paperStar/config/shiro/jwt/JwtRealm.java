package com.paperStar.config.shiro.jwt;

import com.paperStar.dao.UserDao;
import com.paperStar.pojo.User;
import com.paperStar.utils.JwtUtil;
import com.paperStar.utils.RedisUtil;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * 创建判断jwt是否有效的认证方式的Realm
 */
@Slf4j
@Component
public class JwtRealm extends AuthorizingRealm {

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserDao userDao;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public boolean supports(AuthenticationToken token) {
        //表示此Realm只支持JwtToken类型
        return token instanceof JwtToken;
    }

    /**
     * 根据用户名查询是否具有访问某些接口的权限
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        log.info("正在执行JwtRealm中的授权方法");

        //获取用户名
        String name = principalCollection.toString();

        log.info("正在给{}进行授权",name);

        //去数据库中查询用户的角色
        String role = userDao.getUserRole(name);

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        //根据数据库中的权限交付于shiro
        Set<String> roleSet=new HashSet<>();
        roleSet.add(role);
        info.setRoles(roleSet);

        return info;
    }

    /**
     * 用于判断请求头中的token是否有效，合法
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        log.info("正在执行JwtRealm中的doGetAuthenticationInfo(),验证携带的token是否合法");

        String keyJwt = (String) token.getPrincipal();

        //去redis中查找有没有这个token, 没有token就是过期了，需要重新登录
        if(!redisUtil.hasKey(keyJwt)){
            throw new AuthenticationException("token失效请重新登录");
        }

        //获取真正的valueToken
        String valueJwt = redisUtil.get(keyJwt);

        //查找用户
        User user = userDao.findUserByName(jwtUtil.getPayLoadALSOExcludeExpAndIat(valueJwt).get("name").toString());

        log.info("{}在使用token进行认证",user.getUserName());

        //token中储存的信息与数据库储存的不符
        if(user == null){
            throw new AuthenticationException("认证失败");
        }

        //当距离redis中存储的token过期还有30秒时，刷新token
        if(redisUtil.getExpire(keyJwt) < 60){
            log.info("token即将过期，为该用户刷新token");
            refreshToken(keyJwt);
        }
        return new SimpleAuthenticationInfo(user.getUserName(),keyJwt, "JwtRealm");
    }

    protected void refreshToken(String keyJwt){
        //创建一个新的，保存相同信息的jwt令牌
        String valueJwt = jwtUtil.createJwt(jwtUtil.getPayLoadALSOExcludeExpAndIat(keyJwt));

        //将新的jwt存到对应key的value中
        redisUtil.set(keyJwt,valueJwt);

        //更新这个key的过期时间
        redisUtil.expireAt(keyJwt,new Date(System.currentTimeMillis()+jwtUtil.sessionTime));
    }
}
