package com.backend.demo.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Calendar;
import java.util.Map;

public class JwtUtils {

    private static final String SALT = "OIH@#nJOh*/LJ" ;

    public static String getToken(Map<String, String> map) {

        Calendar instance = Calendar.getInstance();
        // 一天後過期
        instance.add(Calendar.DATE, 1);

        JWTCreator.Builder builder = JWT.create();

        map.forEach((k, v)->{
            builder.withClaim(k,v);
        });

        String sign = builder.withExpiresAt(instance.getTime()).sign(Algorithm.HMAC256(SALT));

        return sign ;
    }

    public static void verify(String token) {
        // 如果驗證失敗 會拋異常
        JWT.require(Algorithm.HMAC256(SALT)).build().verify(token);
    }

    public static DecodedJWT getTokenInfo(String token) {
        DecodedJWT verify = JWT.require(Algorithm.HMAC256(SALT)).build().verify(token);
        return verify ;
    }

}
