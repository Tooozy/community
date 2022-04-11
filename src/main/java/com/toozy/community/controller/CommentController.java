package com.toozy.community.controller;

import com.toozy.community.dto.CommentDTO;
import com.toozy.community.dto.ResultDTO;
import com.toozy.community.entity.Comment;
import com.toozy.community.entity.User;
import com.toozy.community.enums.CommentTypeEnum;
import com.toozy.community.exception.CustomizeErrorCode;
import com.toozy.community.service.CommentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
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
public class CommentController {

    @Autowired
    private CommentService commentService;

    @ResponseBody
    @RequestMapping(value = "/comment",method = RequestMethod.POST)
    public Object post(@RequestBody CommentDTO commentDTO,
                       HttpServletRequest request){

        User user = (User) request.getSession().getAttribute("user");

        if (user == null){
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }

        if (commentDTO ==null || StringUtils.isBlank(commentDTO.getContent())){
            return ResultDTO.errorOf(CustomizeErrorCode.CONTENT_IS_EMPTY);
        }

        Comment comment = new Comment();
        comment.setParentId(commentDTO.getParentId());
        comment.setType(commentDTO.getType());
        comment.setCommentator(user.getId().longValue());
        comment.setLikeCount(0L);
        comment.setContent(commentDTO.getContent());
        commentService.insertCommentAndIncCommentCount(comment);
        return ResultDTO.okOf();
    }

    @ResponseBody
    @RequestMapping(value = "/comment/{id}", method = RequestMethod.GET)
    public ResultDTO<List<CommentDTO>> comments(@PathVariable(name = "id") Long id) {
        List<CommentDTO> commentDTOS = commentService.listByTargetId(id, CommentTypeEnum.COMMENT);
        return ResultDTO.okOf(commentDTOS);
    }


}
