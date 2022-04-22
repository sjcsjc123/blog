package com.example.myblog.controller.com;

import com.example.myblog.exception.MyProjectException;
import com.example.myblog.service.AdminUserService;
import com.example.myblog.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author SJC
 */
@Controller
@RequestMapping("/blog")
public class LoginController {

    @Autowired
    private AdminUserService adminUserService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping("/login")
    @ResponseBody
    public String hello(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        @RequestParam("isRememberMe") boolean isRememberMe,
                        HttpSession session, HttpServletResponse response){
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            session.setAttribute("errorMsg", "用户名或密码不能为空");
            return "error/505";
        }
        try{
            adminUserService.login(username, password);
            session.setAttribute("username", username);
            String token = jwtTokenUtil.createToken(username);
            response.setHeader(JwtTokenUtil.AUTH_HEADER_KEY,token);
            if (isRememberMe){
                Cookie cookie = new Cookie("token",token);
                cookie.setMaxAge(7 * 24 * 60 * 60);
                cookie.setDomain("blog.com");
                cookie.setPath("/");
                response.addCookie(cookie);
            }
        }catch (MyProjectException e){
            session.setAttribute("errorMsg",e.getMyProjectExceptionEnum().getMsg());
            return "error/505";
        }
        return "redirect:/";
    }

    @RequestMapping("/toLogin")
    public String toLogin(){
        return "admin/login";
    }

    @RequestMapping("/")
    public String index(Model model){
        return "admin/index";
    }

    @RequestMapping("/loginOut")
    public String loginOut(HttpSession session){
        session.removeAttribute("username");
        return "redirect:/";
    }

    @PostMapping("/register")
    public String register(@RequestParam("username") String username,
                           @RequestParam("phone") String phone,
                           @RequestParam("password") String password,
                           @RequestParam("confirm") String confirm,
                           HttpSession session){
        try {
            adminUserService.register(username,password,phone,confirm);
        }catch (MyProjectException e){
            session.setAttribute("errorMsg",e.getMyProjectExceptionEnum().getMsg());
            return "error/505";
        }
        return "admin/login";
    }

    @GetMapping("/toRegister")
    public String toRegister(){
        return "admin/register";
    }
}
