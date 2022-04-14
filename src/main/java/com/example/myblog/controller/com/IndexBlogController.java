package com.example.myblog.controller.com;

import com.example.myblog.domain.DetailBlog;
import com.example.myblog.interceptor.BlogInterceptor;
import com.example.myblog.vo.UserMsgVo;
import com.example.myblog.elasticsearch.entity.IndexBlogEs;
import com.example.myblog.elasticsearch.service.SearchService;
import com.example.myblog.exception.MyProjectException;
import com.example.myblog.service.*;
import com.example.myblog.vo.FirstCommentVo;
import com.example.myblog.vo.TempUserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author SJC
 */
@Controller
@RequestMapping("/blog")
public class IndexBlogController {

    @Autowired
    private IndexBlogService indexBlogService;
    @Autowired
    private DetailBlogService detailBlogService;
    @Autowired
    private BlogCommentService commentService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private SearchService searchService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @GetMapping("/list/{id}")
    public String listById(@PathVariable Long id, Model model,HttpSession session){
        String username = (String) session.getAttribute("username");
        String userKey = null;
        if (username == null){
            TempUserVo tempUserVo = BlogInterceptor.threadLocal.get();
            userKey = tempUserVo.getUserKey();
        }else {
            userKey = username;
            historyService.createHistory(id,username);
        }
        DetailBlog detailBlog = detailBlogService.showById(id,userKey);
        model.addAttribute("detailBlog",detailBlog);
        List<FirstCommentVo> firstCommentVos = commentService.show(id);
        model.addAttribute("firstCommentVos",firstCommentVos);
        UserMsgVo userMsgVo =
                detailBlogService.getDetailUserMsg(detailBlog.getUsername());
        model.addAttribute("userMsgVo", userMsgVo);
        if (username ==null){
            return "detail";
        }else {
            return "admin/detail";
        }
    }

    @GetMapping("/own")
    public String own(HttpSession session,Model model){
        String username = (String) session.getAttribute("username");
        /*List<IndexBlogVo> indexBlogVos =
                indexBlogService.showByUsername(username);*/
        if (username == null){
            session.setAttribute("errorMsg","请登录");
            return "error/505";
        }
        List<IndexBlogEs> indexBlogs = searchService.showByUsername(username);
        model.addAttribute("indexBlogs",indexBlogs);
        return "admin/own";
    }

    @GetMapping("/thumb/{id}")
    public String thumb(@PathVariable Long id,HttpSession session){
        String username = (String) session.getAttribute("username");
        try {
            indexBlogService.updateThumbNum(id,username);
        }catch (MyProjectException e){
            session.setAttribute("errorMsg",e.getMyProjectExceptionEnum().getMsg());
            return "error/505";
        }
        return "redirect:/blog/list/"+id;
    }

    @GetMapping("/cancel/{id}")
    public String cancel(@PathVariable Long id,HttpSession session){
        String username = (String) session.getAttribute("username");
        try{
            indexBlogService.cancelThumbUp(id,username);
        }catch (MyProjectException e){
            session.setAttribute("errorMsg",e.getMyProjectExceptionEnum().getMsg());
            return "error/505";
        }
        return "redirect:/blog/list/"+id;
    }

    @GetMapping("/star/{id}")
    public String star(@PathVariable Long id,HttpSession session){
        String username = (String) session.getAttribute("username");
        try {
            indexBlogService.star(id,username);
        }catch (MyProjectException e){
            session.setAttribute("errorMsg",e.getMyProjectExceptionEnum().getMsg());
            return "error/505";
        }
        return "redirect:/blog/list/"+id;
    }

    @GetMapping("/cancelStar/{id}")
    public String cancelStar(@PathVariable Long id,HttpSession session){
        String username = (String) session.getAttribute("username");
        try {
            indexBlogService.cancelStar(id,username);
        }catch (MyProjectException e){
            session.setAttribute("errorMsg",e.getMyProjectExceptionEnum().getMsg());
            return "error/505";
        }
        return "redirect:/blog/list/"+id;
    }

}
