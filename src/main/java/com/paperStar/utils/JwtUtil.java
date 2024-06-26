package com.paperStar.utils;

import io.jsonwebtoken.SignatureAlgorithm;
;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SimpleTimeZone;

/**
 * 生成jwt令牌的工具类
 */
@Component
public class JwtUtil {
    //    @Value("{jwt.skin.key}")
    //key的大小必须大于或等于256bit,需要32位英文字符，一个英文字符为：8bit,一个中文字符为12bit
    private static String key="paperStarForLearningShiroAndJwtAndRedis";
    //设置加密算法
    private SignatureAlgorithm signatureAlgorithm=SignatureAlgorithm.HS256;
    public static int sessionTime = 30*60*1000;//token有效时间为30分钟

    /**
     * 获取转换后的私钥对象
     * @return
     */
    public static SecretKey getSecretKey(){
        return Keys.hmacShaKeyFor(key.getBytes());
    }
    /**
     * 生成JWT
     * @param payLoad 携带的数据
     * @return
     */
    public String createJwt(Map<String,Object> payLoad){
        return Jwts.builder()
                .setClaims(payLoad)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+sessionTime))
                .signWith(getSecretKey(),signatureAlgorithm)
                .compact();
    }

    /**
     * 解析JWS，返回一个布尔结果
     * @param jwsString
     * @return
     */
    public static Boolean parseJwt(String jwsString){
        boolean result= false;
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSecretKey())
                    .build()
                    .parseClaimsJws(jwsString);
            result=true;
        }catch (JwtException e){
            result=false;
        }finally {
            return result;
        }
    }

    /**
     * 解析Jws,返回一个Jws对象
     * @param jwsString
     * @return
     */
    public Jws parseJwtResultJws(String jwsString){
        Jws<Claims> claims=null;
        try {
            claims = Jwts.parserBuilder()
                    .setSigningKey(getSecretKey())
                    .build()
                    .parseClaimsJws(jwsString);
        }catch (JwtException e){
            e.printStackTrace();
        }
        return claims;
    }
    /**
     * 获取header中的数据
     * @param jwsString
     * @return
     */
    public Map<String,Object> getHeader(String jwsString){
        return parseJwtResultJws(jwsString).getHeader();
    }

    /**
     * 获取PayLoad中携带的数据
     * @param jwsString
     * @return
     */
    public Map<String,Object> getPayLoad(String jwsString){
        return ((Map<String, Object>) (parseJwtResultJws(jwsString)).getBody());
    }

    /**
     * 获取除去exp和iat的数据，
     * exp：过期时间
     * iat：签发时间
     * @param jwsString
     * @return
     */
    public Map<String,Object> getPayLoadALSOExcludeExpAndIat(String jwsString){
        Map<String, Object> map = getPayLoad(jwsString);
        map.remove("exp");
        map.remove("iat");
        return map;
    }

    /**
     * 获取token的过期时间
     * exp：过期时间
     * @param jwsString
     * @return
     */
    public String getExpTime(String jwsString){
        return getPayLoad(jwsString).get("exp").toString();
    }

    /**
     * 获取token的签发时间
     * @param jwsString
     * @return
     */
    public String getIatTime(String jwsString){
        return getPayLoad(jwsString).get("iat").toString();
    }
}



