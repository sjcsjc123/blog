package com.example.myblog.vo;

import com.example.myblog.domain.ReplyComment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReplyCommentVo {

    private ReplyComment replyComment;
    private List<ReplyComment> replyComments;

}
