package com.example.myblog.vo;

import lombok.Data;

/**
 * 聊天历史记录
 * @author SJC
 */
@Data
public class ChatHistoryVo {

    private String content;
    private String from;
    private String to;
    private String sendTime;

}
