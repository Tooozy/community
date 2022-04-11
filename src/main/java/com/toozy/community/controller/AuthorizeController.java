package com.toozy.community.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.toozy.community.provider.GithubProvider;
import com.toozy.community.dto.AccessTokenDTO;
import com.toozy.community.dto.GithubUser;
import com.toozy.community.entity.User;
import com.toozy.community.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
public class AuthorizeController {
    @Autowired
    private GithubProvider githubProvider;

    @Autowired
    private UserMapper userMapper;

    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.client.secret}")
    private String clientSecret;

    @Value("${github.redirect.uri}")
    private String redirectUri;


    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletRequest request,
                           HttpServletResponse response) throws JsonProcessingException {


        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setCode(code);
        accessTokenDTO.setState(state);
        accessTokenDTO.setRedirect_uri(redirectUri);
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        GithubUser githubUser = githubProvider.getUser(accessToken);


        if (githubUser.getId() != null){
            User user = new User();
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setName(githubUser.getLogin());
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setAvatarUrl(githubUser.getAvatarUrl());

            //查询用户是否存在，然后执行创建用户还是更新用户
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("account_id",user.getAccountId());
            User queriedUser = userMapper.selectOne(queryWrapper);

            if (queriedUser == null){
                userMapper.insert(user);
                System.out.println("插入执行");

            }else {
               //更新用户
                UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("account_id",user.getAccountId());
                userMapper.update(user,updateWrapper);
                System.out.println("更新执行");
            }
            response.addCookie(new Cookie("token",token));
            return "redirect:/";
        }else {
            return "redirect:/";
        }

    }


    @GetMapping("/logout")
    public String logout(HttpServletRequest request,HttpServletResponse response){
        request.getSession().removeAttribute("user");
        Cookie cookie = new Cookie("token",null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        Cookie cookie2 = new Cookie("JSESSIONID",null);
        cookie2.setMaxAge(0);
        response.addCookie(cookie2);
        return "redirect:/";
    }




}
