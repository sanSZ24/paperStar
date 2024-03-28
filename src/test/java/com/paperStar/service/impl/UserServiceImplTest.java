package com.paperStar.service.impl;

import com.paperStar.pojo.User;
import com.paperStar.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class UserServiceImplTest {
    @Autowired
    UserService userService;

    @Test
    public void login() {
        User user = new User();
        user.setUserName("test");
        user.setPassword("123456");
        System.out.println(user);
        System.out.println(userService.Login(user));
    }

    @Test
    @Rollback(false)
    public void logout() {
        User user = new User();
        user.setUserName("sanSZ");
        user.setPassword("123456");
        user.setId(1);
        System.out.println(user);
        System.out.println(userService.Logout(user.getId()));
    }

    @Test
    @Rollback(false)
    public void register() {
        User user = new User();
        user.setUserName("test");
        user.setPassword("123456");
        System.out.println(user);
        System.out.println(userService.Register(user));
    }

    @Test
    public void updateProfile() {
    }
}