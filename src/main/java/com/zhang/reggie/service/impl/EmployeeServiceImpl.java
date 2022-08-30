package com.zhang.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhang.reggie.entity.Employee;
import com.zhang.reggie.service.EmployeeService;
import com.zhang.reggie.mapper.EmployeeMapper;
import org.springframework.stereotype.Service;

/**
* @author 13269
* @description 针对表【employee(员工信息)】的数据库操作Service实现
* @createDate 2022-08-11 22:43:53
*/
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService{

}




