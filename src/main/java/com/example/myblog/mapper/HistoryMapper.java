package com.example.myblog.mapper;

import com.example.myblog.domain.History;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface HistoryMapper extends MongoRepository<History,String> {

    List<History> findByUsernameOrderByCreateTimeDesc(String username);

}
