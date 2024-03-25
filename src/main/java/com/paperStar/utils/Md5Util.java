package com.paperStar.utils;

import lombok.Data;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.stereotype.Component;

/**
 * 用于对密码进行加密和解密的工具类
 */
@Component
@Data
public class Md5Util {
    private static final String algorithmName = "MD5";//加密算法为md5
    private static final int hashIteration = 128;//迭代次数

    //加密
    public String encode(String password,String salt){
        String hashPassword = new SimpleHash(algorithmName, password, salt, hashIteration).toString();
        return hashPassword;
    }

    //比较明文密码是否与数据库中加密的密码相等
    public boolean decode (String password,String salt,String dbPassword) {
        if(dbPassword.equals(encode(password,salt))){
            return true;
        }
        return false;
    }
}
