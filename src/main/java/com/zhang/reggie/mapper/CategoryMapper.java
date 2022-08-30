package com.zhang.reggie.mapper;

import com.zhang.reggie.entity.Category;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 13269
* @description 针对表【category(菜品及套餐分类)】的数据库操作Mapper
* @createDate 2022-08-19 15:44:29
* @Entity com.zhang.reggie.entity.Category
*/
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {

}




