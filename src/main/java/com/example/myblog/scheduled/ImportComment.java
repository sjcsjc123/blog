package com.example.myblog.scheduled;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.myblog.domain.ParentComment;
import com.example.myblog.domain.ReplyComment;
import com.example.myblog.mapper.MDParentCommentMapper;
import com.example.myblog.mapper.MDReplyCommentMapper;
import com.example.myblog.mapper.ParentCommentMapper;
import com.example.myblog.mapper.ReplyCommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 将mongodb的评论内容导入到MySQL中
 */
@Service
public class ImportComment {

    @Autowired
    private MDParentCommentMapper mdParentCommentMapper;
    @Autowired
    private MDReplyCommentMapper mdReplyCommentMapper;
    @Autowired
    private ParentCommentMapper parentCommentMapper;
    @Autowired
    private ReplyCommentMapper replyCommentMapper;

    @Scheduled(cron = "0 0 10,14,18 * * ?")
    public void importAll(){
        List<ParentComment> parentComments = mdParentCommentMapper.findAll();
        List<ReplyComment> replyComments = mdReplyCommentMapper.findAll();
        for (ParentComment parentComment : parentComments) {
            QueryWrapper<ParentComment> wrapper = new QueryWrapper<>();
            wrapper.eq("id",parentComment.getId());
            if (!parentCommentMapper.exists(wrapper)){
                parentCommentMapper.insert(parentComment);
            }
        }
        for (ReplyComment replyComment : replyComments) {
            QueryWrapper<ReplyComment> wrapper = new QueryWrapper<>();
            wrapper.eq("id",replyComment.getId());
            if (!replyCommentMapper.exists(wrapper)){
                replyCommentMapper.insert(replyComment);
            }
        }
    }

}
