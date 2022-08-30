package com.zhang.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhang.reggie.common.BaseContext;
import com.zhang.reggie.common.R;
import com.zhang.reggie.dto.OrderDto;
import com.zhang.reggie.entity.OrderDetail;
import com.zhang.reggie.entity.Orders;
import com.zhang.reggie.service.OrderDetailService;
import com.zhang.reggie.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/order")
public class OrdersController {

    @Autowired
    private OrdersService ordersService;
    @Autowired
    private OrderDetailService orderDetailService;

    /**
     * 用户下单
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        log.info("订单数据：{}",orders);
        ordersService.submit(orders);
        return R.success("下单成功");
    }

    @GetMapping("userPage")
    public R<Page> page(int page, int pageSize){
        log.info("page = {},pageSize= {},name= {}", page, pageSize);

        //分页构造器
        Page<Orders> pageInfo = new Page<>(page,pageSize);
        Page<OrderDto> orderDtoPage = new Page<>();

        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getUserId, BaseContext.getCurrentId());
        //这里是直接把当前用户分页的全部结果查询出来，要添加用户id作为查询条件，否则会出现用户可以查询到其他用户的订单情况
        //添加排序条件，根据更新时间降序排列
        queryWrapper.orderByDesc(Orders::getOrderTime);
        ordersService.page(pageInfo,queryWrapper);

        //获取页面数据
        List<Orders> records = pageInfo.getRecords();

        List<OrderDto> list = records.stream().map((itsm) -> {
            OrderDto orderDto = new OrderDto();
            //拷贝
            BeanUtils.copyProperties(itsm, orderDto);
            //获取orderd的id
            Long id = itsm.getId();
            //根据id查询OrderDetail数据
            LambdaQueryWrapper<OrderDetail> Wrapper=new LambdaQueryWrapper<>();
            Wrapper.eq(id!=null,OrderDetail::getOrderId,id);
            List<OrderDetail> list1 = orderDetailService.list(Wrapper);
            // //对orderDto进行OrderDetails属性的赋值
            orderDto.setOrderDetails(list1);
            return orderDto;
        }).collect(Collectors.toList());

        orderDtoPage.setRecords(list);
        return R.success(orderDtoPage);
    }
}
