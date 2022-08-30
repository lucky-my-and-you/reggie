package com.zhang.reggie.service;

import com.zhang.reggie.entity.Orders;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 13269
* @description 针对表【orders(订单表)】的数据库操作Service
* @createDate 2022-08-28 13:42:53
*/
public interface OrdersService extends IService<Orders> {

    //用户下单
    public void submit(Orders orders);

}
