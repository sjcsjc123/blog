package com.example.myblog.utils;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtTokenUtil {

    //定义token返回头部
    public static final String AUTH_HEADER_KEY = "Authorization";

    //token前缀
    public static final String TOKEN_PREFIX = "Bearer ";

    //签名密钥
    public static final String KEY = "q3t6w9z$C&F)J@NcQfTjWnZr4u7x";

    //有效期默认为 2hour
    public static final Long EXPIRATION_TIME = 1000L*60*60*2;

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
            claims = Jwts.parser()
                    .setSigningKey(KEY)
                    .parseClaimsJws(token)
                    .getBody();
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
