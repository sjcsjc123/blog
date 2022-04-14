package com.example.myblog.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author SJC
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailBlog implements Serializable {

    @TableId
    private Long id;
    private String title;
    private String content;
    private String contentHtml;
    private String username;
}
