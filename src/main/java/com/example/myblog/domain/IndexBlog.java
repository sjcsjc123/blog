package com.example.myblog.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IndexBlog implements Serializable {

    @TableId
    private Long id;

    /**
     * 博客标题
     */
    private String title;

    /**
     * 博客简介
     */
    private String description;

    /**
     * 作者
     */
    private String author;

    /**
     * 权重
     */
    private int weight;

    /**
     * 创建时间
     */
    private String createTime;

}
