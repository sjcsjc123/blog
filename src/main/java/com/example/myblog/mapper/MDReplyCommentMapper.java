package com.example.myblog.mapper;

import com.example.myblog.domain.ReplyComment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MDReplyCommentMapper extends MongoRepository<ReplyComment,String> {

    List<ReplyComment> findByParentId(String parentId);

    List<ReplyComment> findByParent(String parent);

    int countByParent(String parent);
}
