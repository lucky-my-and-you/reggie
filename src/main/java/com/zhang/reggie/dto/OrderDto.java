package com.zhang.reggie.dto;

import com.zhang.reggie.entity.OrderDetail;
import com.zhang.reggie.entity.Orders;
import lombok.Data;

import java.util.List;
 

@Data
public class OrderDto extends Orders {
 
    private List<OrderDetail> orderDetails;
}