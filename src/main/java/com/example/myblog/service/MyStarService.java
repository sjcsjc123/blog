package com.example.myblog.service;

import com.example.myblog.domain.MyStar;

import java.util.List;

public interface MyStarService {

    void star(String author,String username);

    void cancel(String author,String username);

    List<MyStar> findByUsername(String username);
}
