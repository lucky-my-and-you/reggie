package com.zhang.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhang.reggie.entity.ShoppingCart;
import com.zhang.reggie.service.ShoppingCartService;
import com.zhang.reggie.mapper.ShoppingCartMapper;
import org.springframework.stereotype.Service;

/**
* @author 13269
* @description 针对表【shopping_cart(购物车)】的数据库操作Service实现
* @createDate 2022-08-27 20:10:06
*/
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart>
    implements ShoppingCartService{

}




