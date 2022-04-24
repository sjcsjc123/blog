package com.example.myblog.controller.com;

import com.example.myblog.exception.constant.ResponseJson;
import com.example.myblog.service.AdminUserService;
import com.example.myblog.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * @author SJC
 */
@Controller
public class ChatController {

    @Autowired
    private ChatService chatService;

    @GetMapping("/chat/{friend}")
    public String toChat(HttpSession session, @PathVariable String friend){
        String username = (String) session.getAttribute("username");
        chatService.bindFriend(username,friend);
        return "admin/chatroom";
    }

    /**
     * 描述：登录成功跳转页面后，调用此接口获取用户信息
     * @return
     */
    @GetMapping("/getUserinfo")
    @ResponseBody
    public ResponseJson getUserInfo(HttpSession session) {
        String username =(String) session.getAttribute("username");
        return chatService.loadUserInfo(username);
    }

    @GetMapping("/chat")
    public String chat(){
        return "admin/chatroom";
    }
}
