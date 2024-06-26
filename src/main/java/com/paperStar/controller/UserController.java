package com.paperStar.controller;

import com.paperStar.config.shiro.jwt.JwtToken;
import com.paperStar.dao.UserDao;
import com.paperStar.pojo.Result;
import com.paperStar.pojo.User;
import com.paperStar.service.UserService;
import com.paperStar.utils.JwtUtil;
import com.paperStar.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.security.SecurityUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.Map;

@Slf4j
@Controller
@ResponseBody
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    UserDao userDao;
    @Autowired
    RedisUtil redisUtil;

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public Result Login(@RequestBody User user){
        log.info("用户{}发起登录请求", user.getUserName());

        Result result = userService.Login(user);
        return result;
    }

    @RequestMapping(value = "/register",method = RequestMethod.POST)
    public Result register(@RequestBody User user){
        log.info("用户{}发起注册请求", user.getUserName());
        return userService.Register(user);
    }

    /**
     * 必须拥有user或admin权限才可访问该接口
     * @param user
     * @param token
     * @return
     */
    @RequestMapping(value = "/logout",method = RequestMethod.POST)
    @RequiresRoles(logical = Logical.OR, value = {"user"})
    public Result Logout(@RequestBody User user,@RequestHeader("token") String token){
        log.info("用户{}发起登出请求", user.getUserName());
        //从redis中获取真正的jwt
        String jwt = redisUtil.get(token);

        //将key从redis中删除
        redisUtil.delete(jwt);

        //获取jwt中的信息，这里拿了key为id的值
        return userService.Logout((int) jwtUtil.getPayLoadALSOExcludeExpAndIat(jwt).get("id"));
    }

    @RequestMapping(path = "/test")
    public Result test(){
        return Result.success(0,"test");
    }

    @RequestMapping(path = "/unauthorized/{message}")
    public Result unauthorized(@PathVariable String message) throws UnsupportedEncodingException {
        return Result.success(message);
    }
}
