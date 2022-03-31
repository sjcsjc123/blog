package com.example.myblog.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminUser implements Serializable {

    @TableId
    private Long userId;
    private String username;
    private String password;
    private String phone;
    private String nickName;
    private int locked;

}
