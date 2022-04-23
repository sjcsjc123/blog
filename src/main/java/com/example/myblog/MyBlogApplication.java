package com.example.myblog;

import com.example.myblog.exception.constant.ChatMap;
import com.example.myblog.utils.IDWorker;
import com.example.myblog.utils.JwtTokenUtil;
import com.example.myblog.utils.MarkdownUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@MapperScan("com.example.myblog.mapper")
public class MyBlogApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyBlogApplication.class, args);
    }

    @Bean
    public IDWorker idWorker(){
        return new IDWorker(1,1);
    }

    @Bean
    public MarkdownUtil markdownUtil(){
        return new MarkdownUtil();
    }

    @Bean
    public JwtTokenUtil jwtTokenUtil(){
        return new JwtTokenUtil();
    }
}
