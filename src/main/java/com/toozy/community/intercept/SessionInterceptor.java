package com.toozy.community.intercept;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.toozy.community.entity.User;
import com.toozy.community.mapper.UserMapper;
import com.toozy.community.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


@Service
public class SessionInterceptor implements HandlerInterceptor {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private NotificationService notificationService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();
        if (cookies != null ){
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")){
                    String token = cookie.getValue();
                    QueryWrapper<User> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("token",token);
                    List<User> users = userMapper.selectList(queryWrapper);

                    if (users.size() != 0){
                        request.getSession().setAttribute("user",users.get(0));
                        Long unreadCount = notificationService.unreadCount(users.get(0).getId());
                        request.getSession().setAttribute("unreadCount",unreadCount);
                    }
                    break;
                }
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
