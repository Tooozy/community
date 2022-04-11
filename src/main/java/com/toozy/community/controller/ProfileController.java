package com.toozy.community.controller;


import com.toozy.community.dto.NotificationDTO;
import com.toozy.community.dto.PaginationDTO;
import com.toozy.community.entity.User;
import com.toozy.community.service.NotificationService;
import com.toozy.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ProfileController {
    @Autowired
    private QuestionService questionService;

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/profile/{action}")
    public String profile(@PathVariable(name = "action", value = "") String action,
                          Model model,
                          HttpServletRequest request,
                          @RequestParam(value= "page",defaultValue="1")Integer page,
                          @RequestParam(value= "size",defaultValue="8")Integer size) {

        User user = (User) request.getSession().getAttribute("user");
        if (user==null){
            return "redirect:/";
        }


        if ("questions".equals(action)) {
            model.addAttribute("section", "questions");
            model.addAttribute("sectionName", "我的提问");
            PaginationDTO paginationDTO = questionService.queryAllQuestionForId(user.getId(), page, size);
            model.addAttribute("pagination",paginationDTO);
        } else if ("replies".equals(action)) {
            PaginationDTO paginationDTO = notificationService.queryAllRepliesForId(user.getId(), page, size);
            Long unreadCount = notificationService.unreadCount(user.getId());
            model.addAttribute("section", "replies");
            model.addAttribute("sectionName", "最新回复");
            model.addAttribute("unreadCount",unreadCount);
            model.addAttribute("pagination",paginationDTO);
        }




        return "profile";

    }
}
