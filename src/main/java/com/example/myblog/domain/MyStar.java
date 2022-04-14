package com.example.myblog.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 关注列表
 * @author SJC
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyStar {

    private Long id;
    private String author;
    private String username;

}
