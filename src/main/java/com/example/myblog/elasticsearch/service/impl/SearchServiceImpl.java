package com.example.myblog.elasticsearch.service.impl;

import com.example.myblog.domain.IndexBlog;
import com.example.myblog.domain.ParentComment;
import com.example.myblog.elasticsearch.entity.IndexBlogEs;
import com.example.myblog.elasticsearch.mapper.SearchMapper;
import com.example.myblog.elasticsearch.service.SearchService;
import com.example.myblog.mapper.MDParentCommentMapper;
import com.example.myblog.mapper.MDReplyCommentMapper;
import com.example.myblog.service.impl.CategoryServiceImpl;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Field;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private SearchMapper searchMapper;
    @Autowired
    private ElasticsearchRestTemplate restTemplate;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private MDParentCommentMapper mdParentCommentMapper;
    @Autowired
    private MDReplyCommentMapper mdReplyCommentMapper;

    @Override
    public List<IndexBlogEs> show() {
        FieldSortBuilder sortBuilder = SortBuilders.fieldSort(
                "weight").order(SortOrder.ASC);
        FieldSortBuilder sortBuilder1 =
                SortBuilders.fieldSort("thumbUpNum").order(SortOrder.DESC);
        FieldSortBuilder sortBuilder2 =
                SortBuilders.fieldSort("starNum").order(SortOrder.DESC);
        FieldSortBuilder sortBuilder3 =
                SortBuilders.fieldSort("createTime").order(SortOrder.DESC);
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                .withSort(sortBuilder)
                .withSort(sortBuilder1)
                .withSort(sortBuilder2)
                .withSort(sortBuilder3).build();
        SearchHits<IndexBlogEs> searchHits =
                restTemplate.search(nativeSearchQuery, IndexBlogEs.class);
        return getContentBySearchHits(searchHits);
    }

    @Override
    public List<IndexBlogEs> showByKeywordAscCreateTime(String keyword) {
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("title",keyword);
        FieldSortBuilder sortBuilder = SortBuilders.fieldSort(
                "weight").order(SortOrder.ASC);
        FieldSortBuilder sortBuilder1 =
                SortBuilders.fieldSort("thumbUpNum").order(SortOrder.DESC);
        FieldSortBuilder sortBuilder2 =
                SortBuilders.fieldSort("starNum").order(SortOrder.DESC);
        FieldSortBuilder sortBuilder3 =
                SortBuilders.fieldSort("createTime").order(SortOrder.DESC);
        NativeSearchQuery nativeSearchQuery =
                new NativeSearchQueryBuilder()
                .withFilter(matchQueryBuilder)
                .withSort(sortBuilder)
                .withSort(sortBuilder1)
                .withSort(sortBuilder2)
                .withSort(sortBuilder3).build();
        SearchHits<IndexBlogEs> searchHits =
                restTemplate.search(nativeSearchQuery, IndexBlogEs.class);
        return getContentBySearchHits(searchHits);
    }

    @Override
    public List<IndexBlogEs> showByUsername(String username) {
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery(
                "author",username);
        FieldSortBuilder sortBuilder = SortBuilders.fieldSort(
                "weight").order(SortOrder.ASC);
        FieldSortBuilder sortBuilder1 =
                SortBuilders.fieldSort("thumbUpNum").order(SortOrder.DESC);
        FieldSortBuilder sortBuilder2 =
                SortBuilders.fieldSort("starNum").order(SortOrder.DESC);
        FieldSortBuilder sortBuilder3 =
                SortBuilders.fieldSort("createTime").order(SortOrder.DESC);
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                .withFilter(matchQueryBuilder)
                .withSort(sortBuilder)
                .withSort(sortBuilder1)
                .withSort(sortBuilder2)
                .withSort(sortBuilder3).build();
        SearchHits<IndexBlogEs> searchHits =
                restTemplate.search(nativeSearchQuery, IndexBlogEs.class);
        return getContentBySearchHits(searchHits);
    }

    @Override
    public List<IndexBlogEs> showOrderByTime() {
        SortBuilder<FieldSortBuilder> sortBuilder =
                SortBuilders.fieldSort("createTime").order(SortOrder.DESC);
        Pageable pageable = PageRequest.of(0,5);
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                .withSort(sortBuilder)
                .withPageable(pageable).build();
        SearchHits<IndexBlogEs> searchHits =
                restTemplate.search(nativeSearchQuery, IndexBlogEs.class);
        return getContentBySearchHits(searchHits);
    }

    @Override
    public List<IndexBlogEs> showOrderByWeight() {
        SortBuilder<FieldSortBuilder> sortBuilder =
                SortBuilders.fieldSort("weight").order(SortOrder.ASC);
        Pageable pageable = PageRequest.of(0,5);
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                .withSort(sortBuilder).withPageable(pageable).build();
        SearchHits<IndexBlogEs> searchHits =
                restTemplate.search(nativeSearchQuery, IndexBlogEs.class);
        return getContentBySearchHits(searchHits);
    }

    private List<IndexBlogEs> getContentBySearchHits(SearchHits<IndexBlogEs> searchHits){
        List<IndexBlogEs> indexBlogEs = new ArrayList<>();
        for (SearchHit<IndexBlogEs> searchHit : searchHits) {
            IndexBlogEs content = searchHit.getContent();
            BoundHashOperations<String, String, String> ops =
                    redisTemplate.boundHashOps("visitUser" + content.getId().toString());
            Long visitNum = ops.size();
            BoundHashOperations<String, String, String> ops1 =
                    redisTemplate.boundHashOps("thumbUpNum" + content.getId().toString());
            Long thumbUpNum = ops1.size();
            BoundHashOperations<String, String, String> ops2 =
                    redisTemplate.boundHashOps("starNum" + content.getId().toString());
            List<ParentComment> parentComments =
                    mdParentCommentMapper.findByArticleId(content.getId().toString());
            int parentCommentNum = parentComments.size();
            int replyCommentNum = 0;
            for (ParentComment parentComment : parentComments) {
                int countByParent = mdReplyCommentMapper
                        .countByParent(parentComment.getUsername());
                replyCommentNum = replyCommentNum + countByParent;
            }
            int commentNum = replyCommentNum + parentCommentNum;
            Long starNum = ops2.size();
            content.setStarNum(starNum.intValue());
            content.setVisitNum(visitNum.intValue());
            content.setThumbUpNum(thumbUpNum.intValue());
            content.setCommentNum(commentNum);
            indexBlogEs.add(content);
        }
        return indexBlogEs;
    }

}
