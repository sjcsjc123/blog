package com.example.myblog.service;

import com.example.myblog.domain.IndexBlog;
import com.example.myblog.vo.ArticleVo;
import com.example.myblog.vo.IndexBlogVo;

import java.util.List;

public interface IndexBlogService {

    void save(ArticleVo articleVo, String username);

    void deleteById(Long id);

    void updateThumbNum(Long id,String username);

    void cancelThumbUp(Long id, String username);

    void star(Long id, String username);

    void cancelStar(Long id, String username);
}
