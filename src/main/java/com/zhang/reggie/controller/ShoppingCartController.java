package com.zhang.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhang.reggie.common.BaseContext;
import com.zhang.reggie.common.R;
import com.zhang.reggie.entity.ShoppingCart;
import com.zhang.reggie.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 添加购物车
     *
     * @param shoppingCart
     * @return
     */
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart) {
        log.info("购物车数据:{}", shoppingCart);

        //获取用户id，指定当前是哪个用户的购物车数据
        Long currentId = BaseContext.getCurrentId();
        shoppingCart.setUserId(currentId);

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, currentId);

        Long dishId = shoppingCart.getDishId();
        //如果不为空则是菜品，否则就是套餐
        if (dishId != null) {
            //添加到购物车的是菜品
            queryWrapper.eq(ShoppingCart::getDishId, dishId);

        } else {
            //添加到购物车的是套餐
            queryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }

        //查询当前菜品或者套餐是否在购物车中
        //SQL:select * from shopping_cart where user_id = ? and dish_id/setmeal_id = ?
        ShoppingCart cartServiceOne = shoppingCartService.getOne(queryWrapper);

        if (cartServiceOne != null) {
            //如果已经存在，就在原来数量基础上加一
            Integer number = cartServiceOne.getNumber();
            cartServiceOne.setNumber(number + 1);
            shoppingCartService.updateById(cartServiceOne);
        } else {
            //如果不存在，则添加到购物车，数量默认就是一
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            cartServiceOne = shoppingCart;
        }
        return R.success(cartServiceOne);
    }

    /**
     * 查看购物车
     *
     * @return
     */
    @GetMapping("/list")
    public R<List<ShoppingCart>> list(ShoppingCart shoppingCart) {
        log.info("查看购物车...");
        Long currentId = BaseContext.getCurrentId();
        Integer number = shoppingCart.getNumber();


        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(ShoppingCart::getCreateTime);
        queryWrapper.eq(ShoppingCart::getUserId, currentId);

        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);
        return R.success(list);
    }

    /**
     * 清空
     *
     * @return
     */
    @DeleteMapping("/clean")
    public R<String> delete() {
        //获取用户
        Long currentId = BaseContext.getCurrentId();

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, currentId);
        shoppingCartService.remove(queryWrapper);
        return R.success("删除成功");
    }

    /**
     * 客户端的套餐或者是菜品数量减少设置
     *
     * @param shoppingCart
     */
    @PostMapping("/sub")
    @Transactional
    public R<String> sub(@RequestBody ShoppingCart shoppingCart) {

        Long dishId = shoppingCart.getDishId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        //代表数量减少的是菜品数量
        if (dishId != null) {
            //通过dishId查出购物车对象
            queryWrapper.eq(ShoppingCart::getDishId, dishId);
            ShoppingCart cart1 = shoppingCartService.getOne(queryWrapper);
            cart1.setNumber(cart1.getNumber() - 1);
            //对数据进行更新操作
            shoppingCartService.updateById(cart1);
            return R.success("减少成功");
        }
        Long setmealId = shoppingCart.getSetmealId();
        if (setmealId != null) {
            //代表是套餐数量减少
            queryWrapper.eq(ShoppingCart::getSetmealId, setmealId);
            ShoppingCart cart2 = shoppingCartService.getOne(queryWrapper);
            cart2.setNumber(cart2.getNumber() - 1);
            //对数据进行更新操作
            shoppingCartService.updateById(cart2);
            return R.success("减少成功");
        }
        return R.error("操作异常");
    }
}
