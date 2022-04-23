package com.example.myblog.utils;


import io.jsonwebtoken.*;

import java.util.Date;

/**
 * @author SJC
 */
public class JwtTokenUtil {

    //定义token返回头部
    public static final String AUTH_HEADER_KEY = "Authorization";
    public static final String REFRESH_HEADER_KEY = "RefreshToken";

    //签名密钥
    public static final String KEY = "q3t6w9z$C&F)J@NcQfTjWnZr4u7x";

    //有效期默认为 2hour
    public static final Long EXPIRATION_TIME = 1000L*60*60*2;

    //有效期默认为 30天
    public static final Long REFRESH_EXPIRATION_TIME = 1000L*60*60*24*30;

    /**
     * 生成token
     */
    public String createToken(String username){
        Date nowTime = new Date();
        Date expirationTime = new Date(nowTime.getTime()+EXPIRATION_TIME);
        return Jwts.builder().
                setHeaderParam("type","jwt")
                .setSubject(username)
                .setIssuedAt(nowTime)
                .setExpiration(expirationTime)
                .signWith(SignatureAlgorithm.HS512,KEY)
                .compact();
    }

    /**
     * 生成refreshToken
     * 只在登录逻辑中生成
     */
    public String createRefreshToken(String username){
        Date nowTime = new Date();
        Date expirationTime = new Date(nowTime.getTime()+REFRESH_EXPIRATION_TIME);
        return Jwts.builder().
                setHeaderParam("type","jwt")
                .setSubject(username)
                .setIssuedAt(nowTime)
                .setExpiration(expirationTime)
                .signWith(SignatureAlgorithm.HS512,KEY)
                .compact();
    }

    /**
     *校验token
     */
    public Boolean validateToken(String token,String username){
        String usernameFromToken = getUsernameFromToken(token);
        return usernameFromToken.equals(username) && !isTokenExpire(token);
    }

    /**
     * 从令牌中获取用户名
     */
    public String getUsernameFromToken(String token){
        String username = null;
        try {
            Claims claims = getClaimsFromToken(token);
            username = claims.getSubject();
        }catch (Exception e){
            username = null;
        }
        return username;
    }

    /**
     * 从token中获取JWT中的负载
     */
    private Claims getClaimsFromToken(String token) {
        Claims claims = null;
        try {
            JwtParser parser = Jwts.parser();
            parser.setSigningKey(KEY);
            Jws<Claims> claimsJws = parser.parseClaimsJws(token);
            claims = claimsJws.getBody();
        } catch (Exception e) {
        }
        return claims;
    }

    /**
     * 从token中获取过期时间
     */
    private Date getExpiredDateFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getExpiration();
    }

    /**
     * 判断token是否失效
     */
    public boolean isTokenExpire(String token){
        Date expiredDate = getExpiredDateFromToken(token);
        return expiredDate.before(new Date());
    }

}
