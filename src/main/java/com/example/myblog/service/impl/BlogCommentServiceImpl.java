package com.example.myblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.myblog.domain.ParentComment;
import com.example.myblog.domain.ReplyComment;
import com.example.myblog.elasticsearch.entity.IndexBlogEs;
import com.example.myblog.elasticsearch.mapper.SearchMapper;
import com.example.myblog.mapper.*;
import com.example.myblog.service.BlogCommentService;
import com.example.myblog.vo.FirstCommentVo;
import com.example.myblog.vo.ReplyCommentVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * @author SJC
 */
@Service
public class BlogCommentServiceImpl implements BlogCommentService {

    private final Logger logger =
            LoggerFactory.getLogger(BlogCommentServiceImpl.class);

    @Autowired
    private MDParentCommentMapper mdParentCommentMapper;
    @Autowired
    private MDReplyCommentMapper mdReplyCommentMapper;
    @Autowired
    private SearchMapper searchMapper;

    private List<FirstCommentVo> firstCommentVos = new ArrayList<>();
    private List<ReplyCommentVo> replyCommentVos = new ArrayList<>();

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
            logger.info("parent comment sava success");
        }else {
            //子评论，插入到回复表
            ReplyComment replyComment = new ReplyComment();
            replyComment.setCreateTime(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            replyComment.setParentId(parentId.toString());
            ParentComment parentComment =
                    mdParentCommentMapper.findById(parentId.toString()).get();
            replyComment.setParent(parentComment.getUsername());
            replyComment.setChild(username);
            replyComment.setComment(comment);
            mdReplyCommentMapper.save(replyComment);
            logger.info("parent comment sava success");
        }
        logger.info("comment success");
        IndexBlogEs indexBlogEs = searchMapper.findById(articleId).get();
        indexBlogEs.setCommentNum(indexBlogEs.getCommentNum()+1);
        searchMapper.save(indexBlogEs);
    }

    @Override
    public List<FirstCommentVo> show(Long articleId) {
        //查询所有一级评论
        List<ParentComment> parentComments =
                mdParentCommentMapper.findByArticleId(articleId.toString());
        return generate(parentComments);
    }

    private List<FirstCommentVo> generate(List<ParentComment> parentComments){
        for (ParentComment parentComment : parentComments) {
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
        List<ReplyComment> childComments =
                mdReplyCommentMapper.findByParent(comment.getChild());
        replyCommentVos.add(new ReplyCommentVo(comment,childComments));
    }

}
