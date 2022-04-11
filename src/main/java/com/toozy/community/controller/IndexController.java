package com.toozy.community.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.toozy.community.dto.PaginationDTO;
import com.toozy.community.dto.QuestionDTO;
import com.toozy.community.entity.User;
import com.toozy.community.mapper.UserMapper;
import com.toozy.community.service.QuestionService;
import com.toozy.community.service.impl.QuestionServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController {
    @Autowired
    UserMapper userMapper;
    @Autowired
    private QuestionService questionService;

    @GetMapping("/")
    public String index(HttpServletRequest request,
                        Model model,
                        @RequestParam(value= "page",defaultValue="1")Integer page,
                        @RequestParam(value= "size",defaultValue="8")Integer size,
                        @RequestParam(value= "search",required = false)String search
    ){

        PaginationDTO paginationDTO= questionService.queryAllQuestionForPage(search,page, size);
        model.addAttribute("pagination",paginationDTO);




        List<QuestionDTO> popQuestions = questionService.selectPop();
        if (popQuestions.size()>15){
            model.addAttribute("popQuestions", popQuestions.subList(0,15));

        }else {
            model.addAttribute("popQuestions",popQuestions);
        }







        Cookie[] cookies = request.getCookies();
        if (cookies != null ){
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")){
                    String token = cookie.getValue();
                    QueryWrapper<User> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("token",token);
                    User user = userMapper.selectOne(queryWrapper);
                    if (user != null){
                        request.getSession().setAttribute("user",user);
                    }
                    break;
                }
            }
        } else {
            return "index";
        }


        return "index";
    }


    @GetMapping("/search")
    public String search(HttpServletRequest request, Model model,
                        @RequestParam(value= "page",defaultValue="1")Integer page,
                        @RequestParam(value= "size",defaultValue="8")Integer size,
                        @RequestParam(value= "search",required = false)String search){

        PaginationDTO paginationDTO= questionService.queryQuestionForSearch(search,page, size);
        model.addAttribute("pagination",paginationDTO);


        Cookie[] cookies = request.getCookies();
        if (cookies != null ){
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")){
                    String token = cookie.getValue();
                    QueryWrapper<User> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("token",token);
                    User user = userMapper.selectOne(queryWrapper);
                    if (user != null){
                        request.getSession().setAttribute("user",user);
                    }
                    break;
                }
            }
        } else {
            return "index";
        }


        return "index";
    }
}
