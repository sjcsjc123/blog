package com.example.myblog.service;

import com.example.myblog.vo.FirstCommentVo;
import com.example.myblog.vo.ResultCommentVo;

import java.util.List;

public interface BlogCommentService {

    /**
     * 评论
     * @param comment
     * @param parentId
     * @param articleId
     * @param username
     */
    void comment(String comment,Long parentId,Long articleId,String username);

    List<FirstCommentVo> show(Long articleId);

}
