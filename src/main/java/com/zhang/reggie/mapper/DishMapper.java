package com.zhang.reggie.mapper;

import com.zhang.reggie.entity.Dish;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 13269
* @description 针对表【dish(菜品管理)】的数据库操作Mapper
* @createDate 2022-08-20 14:04:24
* @Entity com.zhang.reggie.entity.Dish
*/
@Mapper
public interface DishMapper extends BaseMapper<Dish> {

}




