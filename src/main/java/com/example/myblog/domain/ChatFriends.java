package com.example.myblog.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author SJC
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatFriends {

    private Long id;
    private String username;
    private String friend;
}
