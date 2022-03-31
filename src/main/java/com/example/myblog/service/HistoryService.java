package com.example.myblog.service;

import com.example.myblog.domain.History;
import com.example.myblog.vo.HistoryVo;

import java.util.List;

public interface HistoryService {

    void createHistory(Long articleId,String username);

    List<HistoryVo> list(String username);

    void deleteList(List<String> articleIds);

}
