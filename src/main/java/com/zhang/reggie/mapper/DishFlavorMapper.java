package com.zhang.reggie.mapper;

import com.zhang.reggie.entity.DishFlavor;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 13269
* @description 针对表【dish_flavor(菜品口味关系表)】的数据库操作Mapper
* @createDate 2022-08-22 14:03:29
* @Entity com.zhang.reggie.entity.DishFlavor
*/
@Mapper
public interface DishFlavorMapper extends BaseMapper<DishFlavor> {

}




