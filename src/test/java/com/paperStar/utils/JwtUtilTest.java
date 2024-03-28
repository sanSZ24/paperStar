package com.paperStar.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class JwtUtilTest {
    @Autowired
    JwtUtil jwtUtil;


    @Test
    public void jwtTest(){
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("name","张三");
        String jwt = jwtUtil.createJwt(hashMap);
        System.out.println(jwt);
        System.out.println(jwtUtil.getPayLoadALSOExcludeExpAndIat(jwt));

        //短时间内生成包含相同信息的token时，会导致token相同，因此sleep60秒来测试这个间隔是否会导致token相同
        //sleep60秒后生成包含相同信息的token不一致
        try {
            Thread.sleep(1000*60);   // 休眠6秒
            System.out.println("sleep 60 seconds");
        } catch (Exception e) {
            System.out.println("Got an exception!");
        }

        String valueJwt =  jwtUtil.createJwt(jwtUtil.getPayLoadALSOExcludeExpAndIat(jwt));
        System.out.println(valueJwt);
        System.out.println(jwtUtil.getPayLoadALSOExcludeExpAndIat(valueJwt));
        System.out.println(valueJwt.equals(jwt));

    }
}