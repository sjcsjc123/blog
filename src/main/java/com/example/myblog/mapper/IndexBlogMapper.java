package com.example.myblog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.myblog.domain.IndexBlog;
import com.example.myblog.vo.IndexBlogVo;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IndexBlogMapper extends BaseMapper<IndexBlog> {

    @Select("select count(*) from index_blog where author = #{author}")
    int countArticleNum(String author);
}
