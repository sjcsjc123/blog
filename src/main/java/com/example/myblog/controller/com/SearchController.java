package com.example.myblog.controller.com;

import com.example.myblog.domain.IndexBlog;
import com.example.myblog.elasticsearch.entity.IndexBlogEs;
import com.example.myblog.elasticsearch.service.SearchService;
import com.example.myblog.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private SearchService searchService;
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/all")
    public String all(Model model, HttpSession session){
        List<IndexBlogEs> show = searchService.show();
        model.addAttribute("indexBlogs",show);
        List<IndexBlogEs> newBlogs = searchService.showOrderByTime();
        model.addAttribute("newBlogs",newBlogs);
        List<IndexBlogEs> indexBlogList1 = searchService.showOrderByWeight();
        model.addAttribute("weight",indexBlogList1);
        List<String> categoryNames = categoryService.showByWeight();
        model.addAttribute("categoryNames",categoryNames);
        if (session.getAttribute("username")==null){
            return "list";
        }else {
            return "admin/list";
        }
    }

    @PostMapping("/keyword")
    public String order1(@RequestParam("keyword") String keyword,Model model,
                         HttpSession session){
        List<IndexBlogEs> indexBlogs =
                searchService.showByKeywordAscCreateTime(keyword);
        model.addAttribute("indexBlogs",indexBlogs);
        List<IndexBlogEs> newBlogs = searchService.showOrderByTime();
        model.addAttribute("newBlogs",newBlogs);
        List<IndexBlogEs> indexBlogList1 = searchService.showOrderByWeight();
        model.addAttribute("weight",indexBlogList1);
        List<String> categoryNames = categoryService.showByWeight();
        model.addAttribute("categoryNames",categoryNames);
        if (session.getAttribute("username")==null){
            return "list";
        }else {
            return "admin/list";
        }
    }

    @GetMapping("/keyword/{keyword}")
    public String showByCategory(@PathVariable String keyword, Model model,
                         HttpSession session){
        List<IndexBlogEs> indexBlogs =
                searchService.showByKeywordAscCreateTime(keyword);
        model.addAttribute("indexBlogs",indexBlogs);
        List<IndexBlogEs> newBlogs = searchService.showOrderByTime();
        model.addAttribute("newBlogs",newBlogs);
        List<IndexBlogEs> indexBlogList1 = searchService.showOrderByWeight();
        model.addAttribute("weight",indexBlogList1);
        List<String> categoryNames = categoryService.showByWeight();
        model.addAttribute("categoryNames",categoryNames);
        if (session.getAttribute("username")==null){
            return "list";
        }else {
            return "admin/list";
        }
    }

    @PostMapping("/author")
    public String order2(@RequestParam("username") String username,Model model,
                         HttpSession session){
        List<IndexBlogEs> indexBlogs = searchService.showByUsername(username);
        model.addAttribute("indexBlogs",indexBlogs);
        List<IndexBlogEs> newBlogs = searchService.showOrderByTime();
        model.addAttribute("newBlogs",newBlogs);
        List<IndexBlogEs> indexBlogList1 = searchService.showOrderByWeight();
        model.addAttribute("weight",indexBlogList1);
        List<String> categoryNames = categoryService.showByWeight();
        model.addAttribute("categoryNames",categoryNames);
        if (session.getAttribute("username")==null){
            return "list";
        }else {
            return "admin/list";
        }
    }

}
