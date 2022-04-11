package com.toozy.community.controller;

import com.toozy.community.dto.CommentDTO;
import com.toozy.community.dto.QuestionDTO;
import com.toozy.community.service.CommentService;
import com.toozy.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author baomidou
 * @since 2022-03-26
 */
@Controller

public class QuestionController {
    @Autowired
    private QuestionService questionService;
    @Autowired
    private CommentService commentService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id")Integer id,
                           Model model){
        questionService.incView(id);


        List<CommentDTO> commentDTOS = commentService.listByQuestionId(id);
        model.addAttribute("comments",commentDTOS);



        QuestionDTO questionDTO = questionService.getQuestionDTO(id);
        model.addAttribute("question",questionDTO);

        List<QuestionDTO> relatedQuestions = questionService.selectRelated(questionDTO);
        List<QuestionDTO> relatedQuestionsReplace = new ArrayList<>();

        if (relatedQuestions.size()> 10){
            relatedQuestionsReplace = relatedQuestions.subList(0, 10);
            model.addAttribute("relatedQuestions",relatedQuestionsReplace);
        }else {
            model.addAttribute("relatedQuestions",relatedQuestions);
        }






        return "question";
    }

}
