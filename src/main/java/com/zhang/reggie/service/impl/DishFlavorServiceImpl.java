package com.zhang.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhang.reggie.entity.DishFlavor;
import com.zhang.reggie.service.DishFlavorService;
import com.zhang.reggie.mapper.DishFlavorMapper;
import org.springframework.stereotype.Service;

/**
* @author 13269
* @description 针对表【dish_flavor(菜品口味关系表)】的数据库操作Service实现
* @createDate 2022-08-22 14:03:29
*/
@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor>
    implements DishFlavorService{

}




