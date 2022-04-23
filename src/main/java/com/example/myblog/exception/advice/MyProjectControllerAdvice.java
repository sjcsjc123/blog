package com.example.myblog.exception.advice;

import com.example.myblog.exception.MyProjectException;
import com.example.myblog.exception.constant.MyProjectExceptionEnum;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author SJC
 */
@ControllerAdvice
public class MyProjectControllerAdvice {

    @ExceptionHandler(MyProjectException.class)
    public ResponseEntity<ExceptionResult> HandlerException(MyProjectException myProjectException){
        MyProjectExceptionEnum myProjectExceptionEnum = myProjectException.getMyProjectExceptionEnum();
        return ResponseEntity.status(myProjectException.getMyProjectExceptionEnum().getCode()).body(new ExceptionResult(myProjectException.getMyProjectExceptionEnum()));
    }

}
