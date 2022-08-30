package com.zhang.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhang.reggie.dto.SetmealDto;
import com.zhang.reggie.entity.Setmeal;

/**
* @author 13269
* @description 针对表【setmeal(套餐)】的数据库操作Service
* @createDate 2022-08-20 14:05:10
*/
public interface SetmealService extends IService<Setmeal> {

    //添加菜品
    public void saveWithDish(SetmealDto setmealDto);

}
