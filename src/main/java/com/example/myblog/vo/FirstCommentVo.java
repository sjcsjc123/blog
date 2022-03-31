package com.example.myblog.vo;

import com.example.myblog.domain.ParentComment;
import com.example.myblog.domain.ReplyComment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 封装评论对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FirstCommentVo {

    private ParentComment parentComment;
    private List<ReplyCommentVo> childComments;

}
