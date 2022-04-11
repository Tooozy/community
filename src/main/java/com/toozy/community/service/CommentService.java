package com.toozy.community.service;

import com.toozy.community.dto.CommentDTO;
import com.toozy.community.entity.Comment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.toozy.community.enums.CommentTypeEnum;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author baomidou
 * @since 2022-03-26
 */
public interface CommentService extends IService<Comment> {

    List<CommentDTO> listByQuestionId(Integer id);

    void insertCommentAndIncCommentCount(Comment comment);


    List<CommentDTO> listByTargetId(Long id, CommentTypeEnum comment);
}
