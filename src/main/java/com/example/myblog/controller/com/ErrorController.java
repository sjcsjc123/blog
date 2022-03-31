package com.example.myblog.controller.com;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/error")
public class ErrorController {

    @RequestMapping("/505")
    public String error(){
        return "error/505";
    }

    /**
     * 未登录错误统一返回结果
     */
    @RequestMapping("/noLogin")
    public String noLogin(HttpSession session){
        session.setAttribute("errorMsg","请登录");
        return "error/505";
    }
}
