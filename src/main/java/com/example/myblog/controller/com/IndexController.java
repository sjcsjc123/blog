package com.example.myblog.controller.com;

import com.example.myblog.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpCookie;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author SJC
 */
@Controller
public class IndexController {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @GetMapping("/")
    public String index(HttpSession session,
                        @CookieValue(value = "token",required = false) String token){
        if (token!=null){
            String username =
                    jwtTokenUtil.getUsernameFromToken(token);
            session.setAttribute("username",username);
            return "admin/index";
        }
        if (session.getAttribute("username") == null){
            return "index";
        }else {
            return "admin/index";
        }
    }
}
