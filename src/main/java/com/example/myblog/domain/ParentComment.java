package com.example.myblog.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

/**
 * 博客评论
 * @author SJC
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class ParentComment implements Serializable {

    /**
     * 评论id
     */
    @Id
    private String id;

    /**
     * articleId
     */
    private String articleId;

    /**
     * 评论
     */
    private String comment;

    /**
     * 评论用户名
     */
    private String username;

    /**
     * 格式转化后时间
     */
    private String createTime;
}
