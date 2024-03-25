package com.paperStar.utils;

import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.junit.Test;

import static org.junit.Assert.*;

public class Md5UtilTest {

    @Test
    public void testMd5(){
        String password = "123456";
        RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();
        String salt = randomNumberGenerator.nextBytes().toBase64();
//        System.out.println(salt);

        Md5Util md5Util = new Md5Util();
        System.out.println(md5Util.encode(password,"EkeysNN1T/hbbtw0v0EWMg=="));

        System.out.println(md5Util.decode("123456","EkeysNN1T/hbbtw0v0EWMg==",
                "a6116a0669a8453549f6411b555d62c2"));
    }


}