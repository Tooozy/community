package com.toozy.community.service;

import com.toozy.community.dto.NotificationDTO;
import com.toozy.community.dto.PaginationDTO;
import com.toozy.community.entity.Notification;
import com.baomidou.mybatisplus.extension.service.IService;
import com.toozy.community.entity.User;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author baomidou
 * @since 2022-03-26
 */
public interface NotificationService extends IService<Notification> {

    PaginationDTO queryAllRepliesForId(Integer id, Integer page, Integer size);

    Long unreadCount(Integer id);

    NotificationDTO read(Long id, User user);

    Integer getQuestionId(Long id);
}
