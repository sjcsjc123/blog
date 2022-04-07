package com.example.myblog.elasticsearch.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.Date;

/**
 * @author SJC
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "blog")
public class IndexBlogEs implements Serializable {

    @Id
    private Long id;

    /**
     * 博客标题
     */
    @Field(analyzer = "ik_max_word",type = FieldType.Text,fielddata = true)
    private String title;

    /**
     * 博客简介
     */
    @Field(type = FieldType.Keyword)
    private String description;

    /**
     * 作者
     */
    @Field(type = FieldType.Keyword)
    private String author;

    /**
     * 权重
     */
    @Field(type = FieldType.Integer)
    private int weight;

    /**
     * 创建时间
     */
    @Field(type = FieldType.Keyword)
    private String createTime;

    /**
     * 点赞数
     */
    @Field(type = FieldType.Long)
    private int thumbUpNum;

    /**
     * 收藏数
     */
    @Field(type = FieldType.Long)
    private int starNum;

    /**
     * 访问量
     */
    @Field(type = FieldType.Long)
    private int visitNum;

    /**
     * 评论数量
     */
    @Field(type = FieldType.Long)
    private int commentNum;

}
