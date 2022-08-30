package com.zhang.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhang.reggie.dto.SetmealDto;
import com.zhang.reggie.entity.Setmeal;
import com.zhang.reggie.entity.SetmealDish;
import com.zhang.reggie.mapper.SetmealMapper;
import com.zhang.reggie.service.SetmealDishService;
import com.zhang.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
* @author 13269
* @description 针对表【setmeal(套餐)】的数据库操作Service实现
* @createDate 2022-08-20 14:05:10
*/
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;

    @Override
    @Transactional //事务
    public void saveWithDish(SetmealDto setmealDto) {
        //将数据添加到setmeal中
        this.save(setmealDto);

        //将套餐菜品关系添加到setmealdish表中
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().map((item)->{
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        //保存
        setmealDishService.saveBatch(setmealDishes);
    }
}




