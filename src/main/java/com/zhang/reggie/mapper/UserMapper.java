package com.zhang.reggie.mapper;

import com.zhang.reggie.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 13269
* @description 针对表【user(用户信息)】的数据库操作Mapper
* @createDate 2022-08-26 21:09:39
* @Entity com.zhang.reggie.entity.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

}




