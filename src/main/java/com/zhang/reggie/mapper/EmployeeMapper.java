package com.zhang.reggie.mapper;

import com.zhang.reggie.entity.Employee;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 13269
* @description 针对表【employee(员工信息)】的数据库操作Mapper
* @createDate 2022-08-11 22:43:53
* @Entity com.zhang.reggie.entity.Employee
*/
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {

}




