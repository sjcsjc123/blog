package com.example.myblog.service;

import com.example.myblog.vo.ArticleVo;

import java.util.List;

public interface CategoryService {

    void save(ArticleVo articleVo, String username);

    List<String> showByWeight();

}
