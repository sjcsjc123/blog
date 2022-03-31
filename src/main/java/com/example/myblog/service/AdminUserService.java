package com.example.myblog.service;

public interface AdminUserService {

    Long findIdByUsername(String username);

    void login(String username, String password);

    void register(String username,String password,String phone,
                  String confirm);
}
