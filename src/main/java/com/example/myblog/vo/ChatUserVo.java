package com.example.myblog.vo;

import com.example.myblog.domain.AdminUser;
import lombok.Data;

import java.util.List;

/**
 * @author SJC
 */
@Data
public class ChatUserVo {

    private String username;
    private List<String> friends;
    
}
