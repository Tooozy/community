package com.toozy.community.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.toozy.community.dto.NotificationDTO;
import com.toozy.community.dto.PaginationDTO;
import com.toozy.community.dto.QuestionDTO;
import com.toozy.community.entity.Comment;
import com.toozy.community.entity.Notification;
import com.toozy.community.entity.Question;
import com.toozy.community.entity.User;
import com.toozy.community.enums.NotificationStatusEnum;
import com.toozy.community.enums.NotificationTypeEnum;
import com.toozy.community.mapper.CommentMapper;
import com.toozy.community.mapper.NotificationMapper;
import com.toozy.community.mapper.QuestionMapper;
import com.toozy.community.service.NotificationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2022-03-26
 */
@Service
public class NotificationServiceImp extends ServiceImpl<NotificationMapper, Notification> implements NotificationService {

    @Autowired
    private NotificationMapper notificationMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private CommentMapper commentMapper;
    @Override
    public PaginationDTO queryAllRepliesForId(Integer id, Integer pageNo, Integer pageSize) {

        Page<Notification> page = new Page<>(pageNo,pageSize);
        QueryWrapper<Notification> notificationQueryWrapper = new QueryWrapper<>();
        notificationQueryWrapper.eq("receiver",id).orderByDesc("gmt_create").orderByAsc("status");
        notificationMapper.selectPage(page,notificationQueryWrapper);
        List<Notification> notificationList = page.getRecords();


        List<NotificationDTO> notificationDTOList = new ArrayList<>();
        for (Notification notification : notificationList) {
            NotificationDTO notificationDTO = new NotificationDTO();
            BeanUtils.copyProperties(notification,notificationDTO);
            notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
            notificationDTOList.add(notificationDTO);
        }

        PaginationDTO<NotificationDTO> paginationDTO = new PaginationDTO();
        paginationDTO.setData(notificationDTOList);


        Integer totalPage = 0;
        Long aLong = notificationMapper.selectCount(new QueryWrapper<>());
        Integer total = aLong.intValue();

        if (total%pageSize == 0){
            totalPage = total/pageSize;
        }else {
            totalPage = total/pageSize +1;
        }
        paginationDTO.setPagination(totalPage,pageNo);

        return paginationDTO;
    }

    @Override
    public Long unreadCount(Integer id) {

        QueryWrapper<Notification> notificationQueryWrapper = new QueryWrapper<>();
        notificationQueryWrapper.eq("receiver",id).eq("status",0);
        Long count = notificationMapper.selectCount(notificationQueryWrapper);

        return count;
    }

    @Override
    public NotificationDTO read(Long id, User user) {
        Notification notification_tempo = notificationMapper.selectById(id);
        notification_tempo.setStatus(NotificationStatusEnum.READ.getStatus());
        notification_tempo.setGmtModified(null);
        notificationMapper.updateById(notification_tempo);

        Notification notification = notificationMapper.selectById(id);
        NotificationDTO notificationDTO = new NotificationDTO();
        BeanUtils.copyProperties(notification,notificationDTO);
        notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notificationDTO.getType()));
        return notificationDTO;
    }

    @Override
    public Integer getQuestionId(Long id) {

        Notification notification = notificationMapper.selectById(id);
        QueryWrapper<Comment> commentQueryWrapper = new QueryWrapper<>();
        commentQueryWrapper.eq("parent_id",notification.getOuterid()).eq("type",notification.getType());

        List<Comment> comments = commentMapper.selectList(commentQueryWrapper);
        Comment parentComment = commentMapper.selectById((comments.get(0)).getParentId());
        Question question = questionMapper.selectById(parentComment.getParentId());
        return question.getId();
    }
}
