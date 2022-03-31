package com.example.myblog.vo;

import com.example.myblog.elasticsearch.entity.IndexBlogEs;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 展示关注作者的文章
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StarArticleVo {

    private String author;
    private List<IndexBlogEs> indexBlogEs;

}
