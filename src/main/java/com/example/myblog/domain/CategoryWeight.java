package com.example.myblog.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author SJC
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryWeight {

    private Long id;
    private String category;
    private int weight;

}
