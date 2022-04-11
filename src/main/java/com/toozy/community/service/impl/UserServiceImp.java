package com.toozy.community.service.impl;

import com.toozy.community.entity.User;
import com.toozy.community.mapper.UserMapper;
import com.toozy.community.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2022-03-26
 */
@Service
public class UserServiceImp extends ServiceImpl<UserMapper, User> implements UserService {

}
