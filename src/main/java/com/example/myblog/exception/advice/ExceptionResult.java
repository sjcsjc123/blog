package com.example.myblog.exception.advice;

import com.example.myblog.exception.constant.MyProjectExceptionEnum;
import lombok.Data;

@Data
public class ExceptionResult {
    private int status;
    private String message;
    private Long timestamp;//时间戳
    //根据状态码和msg创建，所以创建一个构造函数

    public ExceptionResult(MyProjectExceptionEnum em){
        this.status=em.getCode();
        this.message=em.getMsg();
        this.timestamp=System.currentTimeMillis();

    }
}
