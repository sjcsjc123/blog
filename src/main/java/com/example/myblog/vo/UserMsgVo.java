package com.example.myblog.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 作者的一些信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserMsgVo {

    private String username;
    private int fansNum;
    private int starNum;
    private int articleNum;

}
