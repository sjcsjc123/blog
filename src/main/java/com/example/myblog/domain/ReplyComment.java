package com.example.myblog.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReplyComment implements Serializable {

    @TableId
    private String id;
    private String parentId;
    private String parent;
    private String createTime;
    private String child;
    private String comment;

}
