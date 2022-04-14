package com.example.myblog.controller.com;

import com.example.myblog.domain.History;
import com.example.myblog.elasticsearch.entity.IndexBlogEs;
import com.example.myblog.elasticsearch.service.SearchService;
import com.example.myblog.exception.MyProjectException;
import com.example.myblog.exception.constant.MyProjectExceptionEnum;
import com.example.myblog.service.CategoryService;
import com.example.myblog.service.HistoryService;
import com.example.myblog.vo.HistoryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author SJC
 */
@Controller
@RequestMapping("/history")
public class HistoryController {

    @Autowired
    private HistoryService historyService;
    @Autowired
    private SearchService searchService;
    @Autowired
    private CategoryService categoryService;

    @RequestMapping("/list")
    public String list(HttpSession session, Model model){
        String username = (String) session.getAttribute("username");
        if (username.isEmpty()){
            session.setAttribute("errorMsg",
                    MyProjectExceptionEnum.USERNAME_NOT_LOGIN.getMsg());
            return "error/505";
        }
        List<HistoryVo> historyVos = historyService.list(username);
        model.addAttribute("histories",historyVos);
        List<IndexBlogEs> newBlogs = searchService.showOrderByTime();
        model.addAttribute("newBlogs",newBlogs);
        List<IndexBlogEs> indexBlogList1 = searchService.showOrderByWeight();
        model.addAttribute("weight",indexBlogList1);
        List<String> categoryNames = categoryService.showByWeight();
        model.addAttribute("categoryNames",categoryNames);
        return "admin/history";
    }

}
