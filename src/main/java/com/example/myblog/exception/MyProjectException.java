package com.example.myblog.exception;

import com.example.myblog.exception.constant.MyProjectExceptionEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MyProjectException extends RuntimeException{
    private MyProjectExceptionEnum myProjectExceptionEnum;
}
