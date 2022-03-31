package com.example.myblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.myblog.domain.IndexBlog;
import com.example.myblog.elasticsearch.entity.IndexBlogEs;
import com.example.myblog.exception.MyProjectException;
import com.example.myblog.exception.constant.MyProjectExceptionEnum;
import com.example.myblog.mapper.IndexBlogMapper;
import com.example.myblog.elasticsearch.mapper.SearchMapper;
import com.example.myblog.service.BlogCommentService;
import com.example.myblog.service.CategoryService;
import com.example.myblog.service.DetailBlogService;
import com.example.myblog.service.IndexBlogService;
import com.example.myblog.vo.ArticleVo;
import com.example.myblog.vo.IndexBlogVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class IndexBlogServiceImpl implements IndexBlogService {

    @Autowired
    private IndexBlogMapper indexBlogMapper;
    @Autowired
    private BlogCommentService blogCommentService;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private DetailBlogService detailBlogService;
    @Autowired
    private SearchMapper searchMapper;

    @Override
    public void save(ArticleVo articleVo, String username) {
        IndexBlog indexBlog = new IndexBlog();
        indexBlog.setId(articleVo.getId());
        indexBlog.setAuthor(username);
        if (articleVo.getContent().length()>=20){
            indexBlog.setDescription(articleVo.getContent().substring(0,20));
        }else {
            indexBlog.setDescription(articleVo.getContent());
        }
        indexBlog.setTitle(articleVo.getTitle());
        indexBlog.setCreateTime(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        //如果是管理员发布的文章，则优先展示
        if (username.equals("admin")){
            indexBlog.setWeight(1);
        }else {
            indexBlog.setWeight(10);
        }
        IndexBlogEs source = new IndexBlogEs();
        source.setAuthor(username);
        source.setWeight(indexBlog.getWeight());
        source.setDescription(indexBlog.getDescription());
        source.setId(indexBlog.getId());
        source.setCreateTime(indexBlog.getCreateTime());
        source.setTitle(indexBlog.getTitle());
        source.setThumbUpNum(0);
        source.setVisitNum(0);
        source.setCommentNum(0);
        source.setStarNum(0);
        searchMapper.save(source);
        indexBlogMapper.insert(indexBlog);
        categoryService.save(articleVo,username);
        detailBlogService.save(articleVo,username);
    }

    @Override
    public void deleteById(Long id) {
        indexBlogMapper.deleteById(id);
        searchMapper.deleteById(id);
    }

    @Override
    public void updateThumbNum(Long id, String username) {
        BoundHashOperations<String, String, String> thumbUpNum =
                redisTemplate.boundHashOps("thumbUpNum"+id.toString());
        if (thumbUpNum.hasKey(username)){
            throw new MyProjectException(MyProjectExceptionEnum.REPEAT_THUMB);
        }else {
            thumbUpNum.put(username, username);
        }
    }

    @Override
    public void cancelThumbUp(Long id, String username) {
        BoundHashOperations<String, String, String> thumbUpNum =
                redisTemplate.boundHashOps("thumbUpNum"+id.toString());
        if (!thumbUpNum.hasKey(username)){
            //不存在表明未点赞
            throw new MyProjectException(MyProjectExceptionEnum.NO_THUMB);
        }else {
            thumbUpNum.delete(username);
        }
    }

    @Override
    public void star(Long id, String username) {
        BoundHashOperations<String, String, String> starNum =
                redisTemplate.boundHashOps("starNum"+id.toString());
        if (starNum.hasKey(username)){
            throw new MyProjectException(MyProjectExceptionEnum.REPEAT_STAR);
        }else {
            starNum.put(username,username);
        }
    }

    @Override
    public void cancelStar(Long id, String username) {
        BoundHashOperations<String, String, String> starNum =
                redisTemplate.boundHashOps("starNum"+id.toString());
        if (!starNum.hasKey(username)){
            throw new MyProjectException(MyProjectExceptionEnum.NO_STAR);
        }else {
            starNum.delete(username);
        }
    }
}
