package com.example.myblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.myblog.domain.ParentComment;
import com.example.myblog.domain.ReplyComment;
import com.example.myblog.mapper.*;
import com.example.myblog.service.BlogCommentService;
import com.example.myblog.vo.FirstCommentVo;
import com.example.myblog.vo.ReplyCommentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 优化：
 *      将从数据库增删改查改为从mongoDB中增删改查
 */
@Service
public class BlogCommentServiceImpl implements BlogCommentService {

    @Autowired
    private IndexBlogMapper indexBlogMapper;
    @Autowired
    private MDParentCommentMapper mdParentCommentMapper;
    @Autowired
    private MDReplyCommentMapper mdReplyCommentMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;

    private List<FirstCommentVo> firstCommentVos = new ArrayList<>();
    private List<ReplyCommentVo> replyCommentVos = new ArrayList<>();

    //@CacheEvict(cacheNames = "comment",allEntries = true)
    @Override
    public void comment(String comment, Long parentId, Long articleId,
                        String username) {
        if (parentId == 0){
            //父评论，插入到一级评论表
            ParentComment parentComment = new ParentComment();
            parentComment.setComment(comment);
            parentComment.setArticleId(articleId.toString());
            parentComment.setCreateTime(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            parentComment.setUsername(username);
            mdParentCommentMapper.save(parentComment);
            //parentCommentMapper.insert(parentComment);
        }else {
            //子评论，插入到回复表
            ReplyComment replyComment = new ReplyComment();
            replyComment.setCreateTime(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            replyComment.setParentId(parentId.toString());
            ParentComment parentComment =
                    mdParentCommentMapper.findById(parentId.toString()).get();
            /*QueryWrapper<ParentComment> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id",parentId);
            ParentComment parentComment = parentCommentMapper.selectOne
             (queryWrapper);*/
            replyComment.setParent(parentComment.getUsername());
            replyComment.setChild(username);
            replyComment.setComment(comment);
            mdReplyCommentMapper.save(replyComment);
            //replyCommentMapper.insert(replyComment);
        }
    }

    //@Cacheable(cacheNames = "comment",key = "'comment:'+#articleId")
    @Override
    public List<FirstCommentVo> show(Long articleId) {
        //查询所有一级评论
        /*QueryWrapper<ParentComment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("article_id",articleId);
        List<ParentComment> parentComments =
                parentCommentMapper.selectList(queryWrapper);*/
        List<ParentComment> parentComments =
                mdParentCommentMapper.findByArticleId(articleId.toString());
        return generate(parentComments);
    }

    private List<FirstCommentVo> generate(List<ParentComment> parentComments){
        for (ParentComment parentComment : parentComments) {
            /*QueryWrapper<ReplyComment> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("parent_id",parentComment.getId());
            List<ReplyComment> replyComments =
                    replyCommentMapper.selectList(queryWrapper);*/
            List<ReplyComment> replyComments =
                    mdReplyCommentMapper.findByParentId(parentComment.getId());
            for (ReplyComment replyComment : replyComments) {
                findChildComment(replyComment);
            }
            firstCommentVos.add(new FirstCommentVo(parentComment,replyCommentVos));
        }
        List<FirstCommentVo> firstCommentVos1 = new ArrayList<>();
        for (FirstCommentVo firstCommentVo : firstCommentVos) {
            firstCommentVos1.add(firstCommentVo);
        }
        firstCommentVos = new ArrayList<>();
        replyCommentVos = new ArrayList<>();
        return firstCommentVos1;
    }

    private void findChildComment(ReplyComment comment){
        /*QueryWrapper<ReplyComment> wrapper = new QueryWrapper<>();
        wrapper.eq("parent",comment.getChild());
        List<ReplyComment> childComments =
                replyCommentMapper.selectList(wrapper);*/
        List<ReplyComment> childComments =
                mdReplyCommentMapper.findByParent(comment.getChild());
        replyCommentVos.add(new ReplyCommentVo(comment,childComments));
    }

}
