package com.example.myblog.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 浏览历史记录
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(value = "history")
public class History {

    @Id
    private String articleId;
    @Indexed
    private String username;
    private String title;
    private String description;
    private String createTime;//作为分类标准

}
