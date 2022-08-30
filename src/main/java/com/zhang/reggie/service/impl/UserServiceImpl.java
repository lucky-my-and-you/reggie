package com.zhang.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhang.reggie.entity.User;
import com.zhang.reggie.service.UserService;
import com.zhang.reggie.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
* @author 13269
* @description 针对表【user(用户信息)】的数据库操作Service实现
* @createDate 2022-08-26 21:09:39
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

}




