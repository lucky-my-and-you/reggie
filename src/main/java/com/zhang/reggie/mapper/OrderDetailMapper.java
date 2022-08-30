package com.zhang.reggie.mapper;

import com.zhang.reggie.entity.OrderDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 13269
* @description 针对表【order_detail(订单明细表)】的数据库操作Mapper
* @createDate 2022-08-28 13:42:57
* @Entity com.zhang.reggie.entity.OrderDetail
*/
@Mapper
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {

}




