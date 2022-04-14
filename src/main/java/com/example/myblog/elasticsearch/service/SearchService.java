package com.example.myblog.elasticsearch.service;

import com.example.myblog.domain.IndexBlog;
import com.example.myblog.elasticsearch.entity.IndexBlogEs;

import java.util.List;

/**
 * @author SJC
 */
public interface SearchService {

    List<IndexBlogEs> show();

    List<IndexBlogEs> showByKeywordAscCreateTime(String keyword);

    List<IndexBlogEs> showByUsername(String username);

    List<IndexBlogEs> showOrderByTime();

    List<IndexBlogEs> showOrderByWeight();

}
