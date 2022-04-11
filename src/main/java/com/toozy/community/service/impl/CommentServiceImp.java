package com.toozy.community.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.toozy.community.dto.CommentDTO;
import com.toozy.community.entity.Comment;
import com.toozy.community.entity.Notification;
import com.toozy.community.entity.Question;
import com.toozy.community.entity.User;
import com.toozy.community.enums.CommentTypeEnum;
import com.toozy.community.enums.NotificationStatusEnum;
import com.toozy.community.enums.NotificationTypeEnum;
import com.toozy.community.mapper.CommentMapper;
import com.toozy.community.mapper.NotificationMapper;
import com.toozy.community.mapper.QuestionMapper;
import com.toozy.community.mapper.UserMapper;
import com.toozy.community.service.CommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2022-03-26
 */
@Slf4j
@Service
public class CommentServiceImp extends ServiceImpl<CommentMapper, Comment> implements CommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private NotificationMapper notificationMapper;

    @Override
    public List<CommentDTO> listByQuestionId(Integer id) {

        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id",id).eq("type", CommentTypeEnum.QUESTION.getType());
        List<Comment> comments = commentMapper.selectList(queryWrapper);
        if (comments.size() == 0){
            return null;
        }

        List<CommentDTO> commentDTOS = new ArrayList<>();
        for (Comment comment : comments) {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment,commentDTO);
            commentDTO.setUser(userMapper.selectById(comment.getCommentator()));
            commentDTOS.add(commentDTO);
        }


        return commentDTOS;
    }

    @Override
    public void insertCommentAndIncCommentCount(Comment comment) {
        commentMapper.insert(comment);
        User user = userMapper.selectById(comment.getCommentator());


        if(comment.getType() == 1){
            //回复问题
            Question question = questionMapper.selectById(comment.getParentId());
            question.setCommentCount(question.getCommentCount()+1);
            question.setGmtModified(null);
            questionMapper.updateById(question);
            log.info("问题更新成功");



            //添加对于回复问题的通知
            Notification notification_question = new Notification();
            notification_question.setNotifier(comment.getCommentator());
            notification_question.setReceiver(question.getCreator().longValue());
            notification_question.setOuterid(comment.getParentId());
            notification_question.setType(NotificationTypeEnum.REPLY_QUESTION.getType());
            notification_question.setStatus(NotificationStatusEnum.UNREAD.getStatus());
            notification_question.setNotifierName(user.getName());
            notification_question.setOuterTitle(question.getTitle());
            notificationMapper.insert(notification_question);


        }else {
            //回复评论
            Comment parentComment = commentMapper.selectById(comment.getParentId());
            parentComment.setCommentCount(parentComment.getCommentCount()+1);
            commentMapper.updateById(parentComment);

            //添加对于回复评论的通知
            Notification notification_comment = new Notification();
            notification_comment.setNotifier(comment.getCommentator());
            notification_comment.setReceiver(parentComment.getCommentator());
            notification_comment.setOuterid(comment.getParentId());
            notification_comment.setType(NotificationTypeEnum.REPLY_COMMENT.getType());
            notification_comment.setStatus(NotificationStatusEnum.UNREAD.getStatus());
            notification_comment.setNotifierName(user.getName());
            notification_comment.setOuterTitle(parentComment.getContent());
            notificationMapper.insert(notification_comment);

        }


    }

    @Override
    public List<CommentDTO> listByTargetId(Long id, CommentTypeEnum comment) {

        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id",id).eq("type", CommentTypeEnum.COMMENT.getType());
        List<Comment> comments = commentMapper.selectList(queryWrapper);

        if (comments.size() == 0){
            return null;
        }

        List<CommentDTO> commentDTOS = new ArrayList<>();
        for (Comment comment1 : comments) {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment1,commentDTO);
            commentDTO.setUser(userMapper.selectById(comment1.getCommentator()));
            commentDTOS.add(commentDTO);
        }


        return commentDTOS;
    }


}
