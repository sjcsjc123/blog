package com.example.myblog.controller.com;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

/**
 * @author SJC
 */
@Controller
public class IndexController {

    @GetMapping("/")
    public String index(HttpSession session){
        if (session.getAttribute("username") == null){
            return "index";
        }else {
            return "admin/index";
        }
    }
}
