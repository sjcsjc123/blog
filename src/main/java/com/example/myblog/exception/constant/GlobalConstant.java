package com.example.myblog.exception.constant;

/**
 * @author SJC
 *
 * 全局变量
 */
public interface GlobalConstant {

    String VISIT_USER_REDIS_KEY = "visitUser:";
    String ONLINE_USER_REDIS_KEY = "onlineUser:";
    String THUMB_UP_NUM_REDIS_KEY = "thumbUpNum:";
    String FANS_REDIS_KEY = "fans:";
    String JWT_TOKEN_BLACK = "blackToken:";
    String REFRESH_TOKEN = "refreshToken:";
    String JWT_TOKEN = "token:";

    boolean isBlackToken(String token);
}
