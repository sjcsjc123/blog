package com.example.myblog.controller.com;

import com.example.myblog.domain.MyStar;
import com.example.myblog.elasticsearch.entity.IndexBlogEs;
import com.example.myblog.elasticsearch.service.SearchService;
import com.example.myblog.exception.constant.MyProjectExceptionEnum;
import com.example.myblog.service.AdminUserService;
import com.example.myblog.service.CategoryService;
import com.example.myblog.service.MyStarService;
import com.example.myblog.vo.StarArticleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 关注和取消关注功能的后台实现
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private MyStarService myStarService;
    @Autowired
    private SearchService searchService;
    @Autowired
    private CategoryService categoryService;

    @ResponseBody
    @GetMapping("/star/{username}")
    public String star(@PathVariable String username, HttpSession session,
                       HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username1 = (String) session.getAttribute("username");
        if (username1.equals(username)){
            /*session.setAttribute("errorMsg",
                    MyProjectExceptionEnum.ERROR_STAR.getMsg());
            response.sendRedirect("/error/505");*/
            return MyProjectExceptionEnum.ERROR_STAR.getMsg();
        }
        BoundHashOperations<String, String, String> ops =
                redisTemplate.boundHashOps(username+"fans");
        if (ops.hasKey(username1)){
            /*session.setAttribute("errorMsg",
                    MyProjectExceptionEnum.REPEAT_STAR1.getMsg());
            response.sendRedirect("/error/505");*/
            return MyProjectExceptionEnum.REPEAT_STAR1.getMsg();
        }else {
            myStarService.star(username,username1);
            ops.put(username1,username1);
        }
        /*response.getWriter().write("关注成功");*/
        return "关注成功";
    }

    @ResponseBody
    @GetMapping("/isStar/{username}")
    public boolean isStar(@PathVariable String username,HttpSession session){
        String username1 = (String) session.getAttribute("username");
        BoundHashOperations<String, String, String> ops =
                redisTemplate.boundHashOps(username+"fans");
        if (ops.hasKey(username1)){
            return true;
        }else {
            return false;
        }
    }

    @ResponseBody
    @GetMapping("/cancel/{username}")
    public String cancel(@PathVariable String username,HttpSession session){
        String username1 = (String) session.getAttribute("username");
        if (username1.equals(username)){
            return MyProjectExceptionEnum.ERROR_STAR.getMsg();
        }
        BoundHashOperations<String, String, String> ops =
                redisTemplate.boundHashOps(username+"fans");
        if (ops.hasKey(username1)){
            /*session.setAttribute("errorMsg",
                    MyProjectExceptionEnum.REPEAT_STAR1.getMsg());
            response.sendRedirect("/error/505");*/
            myStarService.cancel(username,username1);
            ops.delete(username1);
        }else {
            return MyProjectExceptionEnum.ERROR_STAR1.getMsg();
        }
        /*response.getWriter().write("关注成功");*/
        return "取消关注成功";
    }

    @GetMapping("/myStar")
    public String myStar(HttpSession session, Model model){
        String username = (String) session.getAttribute("username");
        List<MyStar> myStars = myStarService.findByUsername(username);
        List<StarArticleVo> starArticleVos = new ArrayList<>();
        for (MyStar myStar : myStars) {
            List<IndexBlogEs> indexBlogEs =
                    searchService.showByUsername(myStar.getAuthor());
            StarArticleVo starArticleVo =
                    new StarArticleVo(myStar.getAuthor(), indexBlogEs);
            starArticleVos.add(starArticleVo);
        }
        model.addAttribute("starArticleVos",starArticleVos);
        List<IndexBlogEs> newBlogs = searchService.showOrderByTime();
        model.addAttribute("newBlogs",newBlogs);
        List<IndexBlogEs> indexBlogList1 = searchService.showOrderByWeight();
        model.addAttribute("weight",indexBlogList1);
        List<String> categoryNames = categoryService.showByWeight();
        model.addAttribute("categoryNames",categoryNames);
        return "admin/star";

    }
}
