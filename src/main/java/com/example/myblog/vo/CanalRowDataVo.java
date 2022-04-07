package com.example.myblog.vo;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author SJC
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CanalRowDataVo {
    private JSONObject before;
    private JSONObject after;
}
