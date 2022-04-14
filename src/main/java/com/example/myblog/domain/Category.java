package com.example.myblog.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author SJC
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category implements Serializable {

    /**
     * 一个专栏对应多个文章，一个文章可以有两个专栏
     */
    private Long id;
    private Long articleId;
    private int categoryId;
    private String categoryName;

}
