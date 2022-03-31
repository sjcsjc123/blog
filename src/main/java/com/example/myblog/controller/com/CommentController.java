package com.example.myblog.controller.com;

import com.example.myblog.domain.ParentComment;
import com.example.myblog.service.AdminUserService;
import com.example.myblog.service.BlogCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping("/blog")
public class CommentController {

    @Autowired
    private BlogCommentService commentService;
    @Autowired
    private AdminUserService adminUserService;

    @PostMapping("/comment/{articleId}/{parentId}")
    public String comment(@RequestParam("comment") String comment,
                          @PathVariable Long parentId,
                          @PathVariable Long articleId,
                          HttpSession session){
        String username = (String) session.getAttribute("username");
        commentService.comment(comment,parentId,articleId,username);
        return "redirect:/blog/list/"+articleId;
    }

}
