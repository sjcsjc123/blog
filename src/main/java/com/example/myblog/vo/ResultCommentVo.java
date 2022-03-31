package com.example.myblog.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultCommentVo {

    private List<FirstCommentVo> firstCommentVos;
    private List<ReplyCommentVo> replyCommentVos;

}
