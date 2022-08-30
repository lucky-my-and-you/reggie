package com.zhang.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhang.reggie.common.R;
import com.zhang.reggie.entity.Employee;
import com.zhang.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 登录
     *
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
        //1,1：获取页面密码
        String password = employee.getPassword();
        //1.2：将密码转换为byte类型
        byte[] b = password.getBytes();
        //1.3：将页面提供的密码进行md5加密
        password = DigestUtils.md5DigestAsHex(b);

        //2：根据页面提供的username查询用户
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        //Employee::getUsername就相当于创建一个Employee对象并调用其getUsername方法
        wrapper.eq(Employee::getUsername, employee.getUsername());
        //根据username查出整条数据
        Employee emp = employeeService.getOne(wrapper);

        //3.1：如果没有查到用户返回登录失败
        if (emp == null) {
            return R.error("账号错误");
        }
        //3.2：查询到以后判断密码是否正确
        if (!password.equals(emp.getPassword())) {
            return R.error("密码错误");
        }
        //3.3：判断用户的状态 0:禁用，1:正常
        if (emp.getStatus() == 0) {
            return R.error("用户已禁用");
        }

        //4：登录成功，将员工id存入Session中
        request.getSession().setAttribute("employee", emp.getId());
        return R.success(emp);
    }

    /**
     * 退出
     *
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    /**
     * 添加员工
     *
     * @param request
     * @param employee
     * @return
     */
    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee) {
        log.info("员工信息 {}", employee.toString());

        //初始密码”12345“进行md5加密
        employee.setPassword(DigestUtils.md5DigestAsHex("12345".getBytes()));

//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
//
//        long empId = (long) request.getSession().getAttribute("employee");
//
//        employee.setCreateUser(empId);
//        employee.setUpdateUser(empId);


        employeeService.save(employee);

        return R.success("添加成功");
    }

    /**
     * 员工信息分页查询和修改
     * @param page     查询页数
     * @param pageSize 查询条数
     * @param name     按名字查询
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        log.info("page = {},pageSize= {},name= {}", page, pageSize, name);

        //构造分页构造器
        Page pageInfo = new Page(page,pageSize);

        //构造条件构造器
        LambdaQueryWrapper<Employee> wrapper=new LambdaQueryWrapper<>();
        //添加过滤条件
        wrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);
        //添加排序条件  更新时间排序
        wrapper.orderByDesc(Employee::getUpdateTime);

        //执行查询
        employeeService.page(pageInfo,wrapper);

        return R.success(pageInfo);
    }

    /**
     * 根据id修改员工状态
     * @param request
     * @param employee
     * @return
     */
    @PutMapping
    public R<String> update(HttpServletRequest request ,@RequestBody Employee employee){
        log.info(employee.toString());
        //获取更新用户id
        long empId =(long) request.getSession().getAttribute("employee");

//        employee.setUpdateUser(empId);
//        employee.setUpdateTime(LocalDateTime.now());

        employeeService.updateById(employee);

        return R.success("更新成功");
    }

    /**
     * 根据id查询用户信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable long id){
        Employee byId = employeeService.getById(id);
        if(byId!=null){
            return R.success(byId);
        }
        return R.error("没有此用户");
    }
}
