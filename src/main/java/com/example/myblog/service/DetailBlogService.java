package com.example.myblog.service;

import com.example.myblog.domain.DetailBlog;
import com.example.myblog.vo.UserMsgVo;
import com.example.myblog.vo.ArticleVo;

public interface DetailBlogService {

    DetailBlog showById(Long id,String username);

    void save(ArticleVo articleVo, String username);

    void deleteById(Long id);

    UserMsgVo getDetailUserMsg(String username);
}
