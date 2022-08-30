package com.zhang.reggie.service;

import com.zhang.reggie.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 13269
* @description 针对表【category(菜品及套餐分类)】的数据库操作Service
* @createDate 2022-08-19 15:44:29
*/
public interface CategoryService extends IService<Category> {

    public void remove(Long ids);

}
