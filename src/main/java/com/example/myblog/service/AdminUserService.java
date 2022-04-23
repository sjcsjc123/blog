package com.example.myblog.service;

/**
 * @author SJC
 */
public interface AdminUserService {

    /**
     * 用户登录
     * @param username
     * @param password
     */
    void login(String username, String password);

    /**
     * 用户注册
     * @param username
     * @param password
     * @param phone
     * @param confirm
     */
    void register(String username,String password,String phone,
                  String confirm);

}
