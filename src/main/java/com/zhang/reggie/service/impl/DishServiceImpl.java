package com.zhang.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhang.reggie.dto.DishDto;
import com.zhang.reggie.entity.Dish;
import com.zhang.reggie.entity.DishFlavor;
import com.zhang.reggie.mapper.DishMapper;
import com.zhang.reggie.service.DishFlavorService;
import com.zhang.reggie.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
* @author 13269
* @description 针对表【dish(菜品管理)】的数据库操作Service实现
* @createDate 2022-08-20 14:04:24
*/
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;


    /**
     * 新增菜品，同时保存对应的口味数据
     * @param dishDto
     */
    @Transactional //事务，遇到异常回滚
    public void saveWithFlavor(DishDto dishDto) {
        //保存菜品的基本信息到菜品表dish
        this.save(dishDto);

        Long dishId = dishDto.getId();//菜品id

        //菜品口味
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());

        //保存菜品口味数据到菜品口味表dish_flavor
        dishFlavorService.saveBatch(flavors);

    }

    /**
     * 修改数据，回显
     * @param id
     * @return
     */
    @Override
    public DishDto getByIdWithFlavor(Long id) {

        //根据id查询数据
        Dish byId = this.getById(id);
        DishDto dishDto=new DishDto();
        //拷贝
        BeanUtils.copyProperties(byId,dishDto);

        //查询当前菜品对应的口味信息
        LambdaQueryWrapper<DishFlavor> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(DishFlavor::getDishId,byId);
        List<DishFlavor> list = dishFlavorService.list(wrapper);
        dishDto.setFlavors(list);

        return dishDto;
    }

    /**
     * 修改后保存信息
     * @param dishDto
     */
    @Override
    public void updateWithFlavor(DishDto dishDto) {
        //获取dish基本信息
        Long id = dishDto.getId();

        this.updateById(dishDto);

        //根据id查询口味对应信息
        LambdaQueryWrapper<DishFlavor> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(DishFlavor::getDishId,id);
        //先删除对应的口味表
        dishFlavorService.remove(wrapper);

        //添加修改后的口味数据
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item) -> {
            item.setDishId(id);
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);

    }


}




