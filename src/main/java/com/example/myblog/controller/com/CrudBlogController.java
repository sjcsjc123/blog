package com.example.myblog.controller.com;

import com.example.myblog.service.BlogCommentService;
import com.example.myblog.service.CategoryService;
import com.example.myblog.service.DetailBlogService;
import com.example.myblog.service.IndexBlogService;
import com.example.myblog.utils.IDWorker;
import com.example.myblog.vo.ArticleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * @author SJC
 */
@Controller
@RequestMapping("/blog")
public class CrudBlogController {

    @Autowired
    private IndexBlogService indexBlogService;
    @Autowired
    private DetailBlogService detailBlogService;
    @Autowired
    private IDWorker idWorker;
    @Autowired
    private BlogCommentService commentService;
    @Autowired
    private CategoryService categoryService;

    @RequestMapping("/toUpdate")
    public String toUpdate(Model model){
        return "admin/addBlog";
    }

    @GetMapping("/toAdd")
    public String toAdd(){
        return "admin/addBlog";
    }

    @PostMapping("/publish")
    public String publish(@RequestParam("title") String title,
                          @RequestParam("content") String content,
                          HttpSession session){
        String username = (String) session.getAttribute("username");
        ArticleVo articleVo = new ArticleVo(idWorker.nextId(),title, content);
        indexBlogService.save(articleVo,username);
        return "redirect:/search/all";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        indexBlogService.deleteById(id);
        detailBlogService.deleteById(id);
        categoryService.deleteById(id);
        return "redirect:/blog/own";
    }

    @GetMapping("/top/{id}")
    public String top(@PathVariable Long id){
        indexBlogService.top(id);
        return "redirect:/blog/own";
    }
}
