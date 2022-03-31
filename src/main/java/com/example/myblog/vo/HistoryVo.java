package com.example.myblog.vo;

import com.example.myblog.domain.History;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoryVo {

    private List<History> histories;
    private String category;

}
