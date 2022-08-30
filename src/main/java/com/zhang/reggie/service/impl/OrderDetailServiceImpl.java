package com.zhang.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhang.reggie.entity.OrderDetail;
import com.zhang.reggie.service.OrderDetailService;
import com.zhang.reggie.mapper.OrderDetailMapper;
import org.springframework.stereotype.Service;

/**
* @author 13269
* @description 针对表【order_detail(订单明细表)】的数据库操作Service实现
* @createDate 2022-08-28 13:42:57
*/
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail>
    implements OrderDetailService{

}




