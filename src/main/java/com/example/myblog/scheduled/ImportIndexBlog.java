package com.example.myblog.scheduled;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.myblog.domain.IndexBlog;
import com.example.myblog.elasticsearch.entity.IndexBlogEs;
import com.example.myblog.elasticsearch.mapper.SearchMapper;
import com.example.myblog.mapper.IndexBlogMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImportIndexBlog {

    @Autowired
    private SearchMapper searchMapper;
    @Autowired
    private IndexBlogMapper indexBlogMapper;

    //@Scheduled(cron = "0/5 * * * * ? ")
    public void importAll(){
        QueryWrapper<IndexBlog> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("weight");
        queryWrapper.orderByDesc("create_time");
        List<IndexBlog> indexBlogs = indexBlogMapper.selectList(queryWrapper);
        for (IndexBlog indexBlog : indexBlogs) {
            if (!searchMapper.existsById(indexBlog.getId())){
                IndexBlogEs target = new IndexBlogEs();
                BeanUtils.copyProperties(indexBlog, target);
                searchMapper.save(target);
            }
        }
    }

}
