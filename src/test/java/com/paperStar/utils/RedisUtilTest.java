package com.paperStar.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class RedisUtilTest {
    @Autowired
    RedisUtil redisUtil;

    @Test
    public void RedisUtil(){
        if(redisUtil.hasKey("张三")){
            System.out.println("存在张三");
            System.out.println(redisUtil.get("张三"));
        }
        else{
            System.out.println("not found");
        }
    }

    @Test
    public void testSet(){
        if(redisUtil.hasKey("李四")){
            System.out.println(redisUtil.get("李四"));
        }
        else{
            System.out.println("not found");
        }
    }

    @Test
    public void testExpire(){
        redisUtil.set("李四","test");
        redisUtil.expireAt("李四",new Date(System.currentTimeMillis()+60*1000));
    }

    @Test
    public void hasKey(){
        System.out.println(redisUtil.getExpire("李四"));

        if(redisUtil.hasKey("李四")){
            System.out.println(redisUtil.get("李四"));
        }
        else{
            System.out.println("not found");
        }
    }
}