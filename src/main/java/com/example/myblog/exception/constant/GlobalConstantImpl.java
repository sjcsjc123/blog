package com.example.myblog.exception.constant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class GlobalConstantImpl implements GlobalConstant{

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public boolean isBlackToken(String token) {
        Boolean aBoolean =
                redisTemplate.hasKey(GlobalConstant.JWT_TOKEN_BLACK + token);
        if (aBoolean){
            return true;
        }
        return false;
    }
}
