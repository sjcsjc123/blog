package com.example.myblog.vo;

import com.example.myblog.domain.IndexBlog;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 用于展示的封装类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IndexBlogVo {

    private IndexBlog indexBlog;
    private String createTime;
}
