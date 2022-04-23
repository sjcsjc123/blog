package com.example.myblog.interceptor;

import com.example.myblog.exception.MyProjectException;
import com.example.myblog.exception.constant.GlobalConstant;
import com.example.myblog.exception.constant.MyProjectExceptionEnum;
import com.example.myblog.utils.JwtTokenUtil;
import com.example.myblog.vo.TempUserVo;
import org.apache.catalina.session.StandardSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 拦截器，实现未登录和登录的区分
 * @author SJC
 */
public class BlogInterceptor implements HandlerInterceptor {

    private final Logger logger =
            LoggerFactory.getLogger(BlogInterceptor.class);

    public static final ThreadLocal<TempUserVo> threadLocal = new ThreadLocal<>();

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private GlobalConstant globalConstant;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        TempUserVo tempUserVo = new TempUserVo();
        boolean flag = false;
        String username = (String) request.getSession().getAttribute("username");
        String refreshToken =
                redisTemplate.opsForValue().get(GlobalConstant.REFRESH_TOKEN+username);
        //先判断cookie是否存储token
        logger.info("拦截到的请求路径为"+request.getRequestURL());
        for (Cookie cookie : request.getCookies()) {
            if ("token".equals(cookie.getName())){
                logger.info("cookie已经包含登录信息");
                if(validToken(cookie.getValue(),refreshToken,request,response)){
                    flag = true;
                }
            }
        }
        if (!flag){
            logger.info("cookie没有登录信息");
            String token =
                    redisTemplate.opsForValue().get(GlobalConstant.JWT_TOKEN + username);
            if (token!=null){
                if(validToken(token,refreshToken,request,response)){
                    logger.info("token valid success,pass");
                    flag = true;
                }
            }
        }
        //用户未登录
        Cookie[] cookies = request.getCookies();
        if (cookies != null){
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("user-key")){
                    tempUserVo.setUserKey(cookie.getValue());
                }
            }
        }
        //即未登录也无user-key
        if (tempUserVo.getUserKey()== null){
            String userKey = UUID.randomUUID().toString().replace("-", "");
            tempUserVo.setUserKey(userKey);
            logger.info("setting userKey ...");
            //标记第一次访问
            tempUserVo.setFirst(true);
        }
        threadLocal.set(tempUserVo);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        TempUserVo tempUserVo = threadLocal.get();
        logger.info("postHandle");
        //如果是第一次访问
        if (tempUserVo.isFirst()){
            logger.info("user first visit");
            String userKey = tempUserVo.getUserKey();
            Cookie cookie = new Cookie("user-key", userKey);
            cookie.setMaxAge(60*60*24*30);
            response.addCookie(cookie);
        }
    }

    public boolean validToken(String token, String refreshToken,
                         HttpServletRequest request
            ,HttpServletResponse response) throws IOException {
        if (token != null && refreshToken != null){
            //表示用户已经登录
            logger.info("valid token");
            if (jwtTokenUtil.isTokenExpire(token) || globalConstant.isBlackToken(token)){
                logger.info("access token expire,start valid refresh token");
                //判断refreshToken是否过期或者在黑名单中
                if (jwtTokenUtil.isTokenExpire(refreshToken) || globalConstant.isBlackToken(refreshToken)){
                    logger.info("need login again");
                    //黑名单策略,每一个value设置三个小时的有效期，到期自动销毁，避免积累过多
                    redisTemplate.opsForValue().set(GlobalConstant.JWT_TOKEN_BLACK+token,token,3*60*60, TimeUnit.SECONDS);
                    redisTemplate.opsForValue().set(GlobalConstant.JWT_TOKEN_BLACK+refreshToken,refreshToken,3*60*60, TimeUnit.SECONDS);
                    HttpSession session = request.getSession();
                    session.setAttribute("errorMsg",
                            MyProjectExceptionEnum.USERNAME_NOT_LOGIN.getMsg());
                    response.sendRedirect("/error/505");
                }else {
                    //没过期的话，根据refreshToken刷新accessToken
                    String username =
                            jwtTokenUtil.getUsernameFromToken(refreshToken);
                    String newToken = jwtTokenUtil.createToken(username);
                    response.setHeader(JwtTokenUtil.AUTH_HEADER_KEY,newToken);
                }
            }else {
                logger.info("access token not expire");
            }
        }else {
            return false;
        }
        return true;
    }
}
