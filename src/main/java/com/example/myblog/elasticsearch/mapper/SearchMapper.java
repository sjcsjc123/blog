package com.example.myblog.elasticsearch.mapper;

import com.example.myblog.elasticsearch.entity.IndexBlogEs;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface SearchMapper extends ElasticsearchRepository<IndexBlogEs,Long> {
}
