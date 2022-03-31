package com.example.myblog.service.impl;

import com.example.myblog.domain.History;
import com.example.myblog.elasticsearch.entity.IndexBlogEs;
import com.example.myblog.elasticsearch.mapper.SearchMapper;
import com.example.myblog.mapper.HistoryMapper;
import com.example.myblog.service.HistoryService;
import com.example.myblog.vo.HistoryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class HistoryServiceImpl implements HistoryService {

    @Autowired
    private HistoryMapper historyMapper;
    @Autowired
    private SearchMapper searchMapper;

    @Override
    public void createHistory(Long articleId, String username) {
        History history = new History();
        String createTime =
                new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        history.setCreateTime(createTime);
        history.setArticleId(articleId.toString());
        IndexBlogEs indexBlogEs = searchMapper.findById(articleId).get();
        history.setDescription(indexBlogEs.getDescription());
        history.setUsername(username);
        history.setTitle(indexBlogEs.getTitle());
        historyMapper.save(history);
    }

    @Override
    public List<HistoryVo> list(String username) {
        List<History> histories =
                historyMapper.findByUsernameOrderByCreateTimeAsc(username);
        List<HistoryVo> historyVos = new ArrayList<>();
        List<String> categories = new ArrayList<>();
        for (History history : histories) {
            if (!categories.contains(history.getCreateTime())){
                categories.add(history.getCreateTime());
            }
        }
        for (String category : categories) {
            List<History> historyList = new ArrayList<>();
            for (History history : histories) {
                if (history.getCreateTime().equals(category)){
                    historyList.add(history);
                }
            }
            HistoryVo historyVo = new HistoryVo(historyList, category);
            historyVos.add(historyVo);
        }
        return historyVos;
    }

    @Override
    public void deleteList(List<String> articleIds) {
        for (String articleId : articleIds) {
            historyMapper.deleteById(articleId);
        }
    }
}
