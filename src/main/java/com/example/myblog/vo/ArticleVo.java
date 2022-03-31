package com.example.myblog.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 发布博客的内容进行封装
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleVo {

    private Long id;
    private String title;
    private String content;

}
