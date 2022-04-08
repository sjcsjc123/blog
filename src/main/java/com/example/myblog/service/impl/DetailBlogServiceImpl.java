package com.example.myblog.service.impl;

import com.example.myblog.domain.DetailBlog;
import com.example.myblog.vo.UserMsgVo;
import com.example.myblog.elasticsearch.entity.IndexBlogEs;
import com.example.myblog.mapper.DetailBlogMapper;
import com.example.myblog.mapper.IndexBlogMapper;
import com.example.myblog.service.DetailBlogService;
import com.example.myblog.utils.MarkdownUtil;
import com.example.myblog.vo.ArticleVo;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author SJC
 */
@Service
public class DetailBlogServiceImpl implements DetailBlogService {

    @Autowired
    private DetailBlogMapper detailBlogMapper;
    @Autowired
    private MarkdownUtil markdownUtil;
    @Autowired
    private IndexBlogMapper indexBlogMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private ElasticsearchRestTemplate restTemplate;

    @Cacheable(cacheNames = "detailBlog",key = "'detailBlog'+#id")
    @Override
    public DetailBlog showById(Long id, String username) {
        //半个小时重复不增加访问数量
        BoundHashOperations<String, String, String> visitUser =
                redisTemplate.boundHashOps("visitUser"+id.toString());
        if (visitUser.hasKey(username)) {
        } else {
            visitUser.put(username,username);
            //给rocketmq发送消息延时删除redis的key-value


        }
        DetailBlog detailBlog = detailBlogMapper.selectById(id);
        return detailBlog;
    }

    @Override
    public void save(ArticleVo articleVo, String username) {
        DetailBlog detailBlog = new DetailBlog();
        detailBlog.setId(articleVo.getId());
        detailBlog.setTitle(articleVo.getTitle());
        detailBlog.setUsername(username);
        String contentHtml = markdownUtil.markdownToHtml(articleVo.getContent());
        detailBlog.setContent(articleVo.getContent());
        detailBlog.setContentHtml(contentHtml);
        detailBlogMapper.insert(detailBlog);
    }

    @CacheEvict(cacheNames = "detailBlog",key = "'detailBlog'+#id",
            allEntries = true)
    @Override
    public void deleteById(Long id) {
        detailBlogMapper.deleteById(id);
    }

    @Override
    public UserMsgVo getDetailUserMsg(String username) {
        BoundHashOperations<String, String, String> ops =
                redisTemplate.boundHashOps(username + "fans");
        int fansNum = ops.size().intValue();
        int articleNum = indexBlogMapper.countArticleNum(username);
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery(
                "author", username);
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                .withFilter(matchQueryBuilder).build();
        SearchHits<IndexBlogEs> search =
                restTemplate.search(nativeSearchQuery, IndexBlogEs.class);
        int starNum = 0;
        for (SearchHit<IndexBlogEs> indexBlogEsSearchHit : search) {
            IndexBlogEs content = indexBlogEsSearchHit.getContent();
            starNum = starNum + content.getStarNum();
        }
        return new UserMsgVo(username, fansNum, starNum,
                articleNum);
    }
}
