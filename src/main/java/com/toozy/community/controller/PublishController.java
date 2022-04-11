package com.toozy.community.controller;

import com.toozy.community.entity.Question;
import com.toozy.community.entity.User;
import com.toozy.community.mapper.QuestionMapper;
import com.toozy.community.mapper.UserMapper;
import com.toozy.community.service.QuestionService;
import com.toozy.community.service.impl.QuestionServiceImp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Controller
public class PublishController {
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    QuestionService questionService;


    @GetMapping("/publish")
    public String publish() {
        return "publish";
    }

    @PostMapping("/publish")
    public String doPublish(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "tag", required = false) String tag,
            @RequestParam(value = "id", required = false) Integer id,
            HttpServletRequest request,
            Model model
    ) {

        model.addAttribute("title", title);
        model.addAttribute("description", description);
        model.addAttribute("tag", tag);
        if (title == null || title == "") {
            model.addAttribute("error", "标题不能为空");
            return "publish";
        }
        if (description == null || description == "") {
            model.addAttribute("error", "描述不能为空");
            return "publish";
        }
        if (tag == null || tag == "") {
            model.addAttribute("error", "标签不能为空");
            return "publish";
        }

        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            model.addAttribute("error", "用户未登陆");
        }
        Question question = new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        question.setCreator(user.getId());
        question.setId(id);

        questionService.doCreateOrUpdate(question);


        return "redirect:/";
    }


    @GetMapping("/publish/{id}")
    public String edit(@PathVariable(name = "id") Integer id,
                       Model model) {

        Question questionById = questionMapper.selectById(id);

        model.addAttribute("title", questionById.getTitle());
        model.addAttribute("description", questionById.getDescription());
        model.addAttribute("tag", questionById.getTag());
        model.addAttribute("id", id);


        return "publish";
    }
}
