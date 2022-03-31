package com.example.myblog.mapper;

import com.example.myblog.domain.ParentComment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MDParentCommentMapper extends MongoRepository<ParentComment, String> {

    List<ParentComment> findByArticleId(String articleId);

}
