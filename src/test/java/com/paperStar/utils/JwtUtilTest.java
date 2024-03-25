package com.paperStar.utils;

import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class JwtUtilTest {
    @Test
    public void jwtTest(){
        JwtUtil jwtUtil = new JwtUtil();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("name","张三");
        String jwt = jwtUtil.createJwt(hashMap);
        System.out.println(jwt);
        System.out.println(jwtUtil.parseJwt(jwt));
        System.out.println(jwtUtil.getPayLoadALSOExcludeExpAndIat(jwt));
        Map<String,Object> map = jwtUtil.getPayLoadALSOExcludeExpAndIat(jwt);
        System.out.println(map.get("name"));
    }
}