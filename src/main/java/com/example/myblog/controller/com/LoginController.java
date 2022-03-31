package com.example.myblog.controller.com;

import com.example.myblog.exception.MyProjectException;
import com.example.myblog.service.AdminUserService;
import com.example.myblog.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/blog")
public class LoginController {

    @Autowired
    private AdminUserService adminUserService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping("/login")
    public String hello(@RequestParam("username") String username,
                        @RequestParam("password") String password,
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
