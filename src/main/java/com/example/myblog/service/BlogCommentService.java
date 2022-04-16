package com.example.myblog.service;

import com.example.myblog.vo.FirstCommentVo;

import java.util.List;

public interface BlogCommentService {

    /**
     * 评论
     * @param comment
     * @param parentId
     * @param articleId
     * @param username
     */
    void comment(String comment,String parentId,Long articleId,String username);

    List<FirstCommentVo> show(Long articleId);

    /**
     * 删除评论
     * @param id
     */
    void delete(String id,Long articleId);
}
