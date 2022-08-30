package com.zhang.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhang.reggie.common.R;
import com.zhang.reggie.dto.DishDto;
import com.zhang.reggie.entity.Category;
import com.zhang.reggie.entity.Dish;
import com.zhang.reggie.entity.DishFlavor;
import com.zhang.reggie.service.CategoryService;
import com.zhang.reggie.service.DishFlavorService;
import com.zhang.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private CategoryService categoryService;

    /**
     * 添加菜品
     *
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {

        log.info(dishDto.toString());
        //添加  自定义方法
        dishService.saveWithFlavor(dishDto);

        return R.success("添加菜品成功");

    }


    /**
     * 员工信息分页查询和修改
     *
     * @param page     查询页数
     * @param pageSize 查询条数
     * @param name     按名字查询
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        log.info("page = {},pageSize= {},name= {}", page, pageSize, name);

        //构造分页构造器
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>();

        //构造条件构造器
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        //添加过滤条件  模糊查询
        wrapper.like(name != null, Dish::getName, name);
        //添加排序条件
        wrapper.orderByDesc(Dish::getUpdateTime);
        //执行查询
        dishService.page(pageInfo, wrapper);

        //对象拷贝
        BeanUtils.copyProperties(pageInfo, dishDtoPage, "records");

        List<Dish> records = pageInfo.getRecords();

        List<DishDto> list = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            Long categoryId = item.getCategoryId();//分类id
            //根据id查询分类对象
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());

        dishDtoPage.setRecords(list);

        return R.success(dishDtoPage);
    }

    /**
     * 修改数据
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id) {

        log.info(id.toString());
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return R.success(dishDto);
    }

    /**
     * 保存
     *
     * @param dishDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto) {
        log.info(dishDto.toString());

        dishService.updateWithFlavor(dishDto);
        return R.success("保存成功");
    }

    /**
     * (批量）停售/起售
     *
     * @param status
     * @param ids
     * @return
     */
    @PostMapping("/status/{status}")
    public R<String> upstatus(@PathVariable Integer status, Long[] ids) {
        //查询用户
        for (Long id : ids) {
            Dish byId = dishService.getById(id);
            byId.setStatus(status);
            dishService.updateById(byId);
        }
        return R.success("修改成功");
    }

    /**
     * （批量） 删除菜品,同时删除对应的口味信息
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(Long[] ids) {
        //查询用户
        for (Long id : ids) {
            dishService.removeById(id);
            LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(DishFlavor::getDishId, id);
            dishFlavorService.remove(queryWrapper);
        }
        return R.success("删除成功");
    }

    /**
     * 套餐添加里的菜品展示
     *
     * @param dishDto
     * @return
     */
    @GetMapping("/list")
    public R<List<DishDto>> list(DishDto dishDto) {
        //创建条件构造
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dishDto.getCategoryId() != null, Dish::getCategoryId, dishDto.getCategoryId());
        //判断菜品状态 1（起售）
        queryWrapper.eq(Dish::getStatus, 1);
        List<Dish> list = dishService.list(queryWrapper);

        List<DishDto> dishDtoList = list.stream().map((itsm) -> {
            DishDto dishDto1 = new DishDto();
            BeanUtils.copyProperties(itsm,dishDto1);
            //获取菜品id
            Long id = itsm.getId();
            if(id!=null){
                LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper=new LambdaQueryWrapper<>();
                dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId,id);
                //口味集合
                List<DishFlavor> list1 = dishFlavorService.list(dishFlavorLambdaQueryWrapper);
                dishDto1.setFlavors(list1);
            }
            return dishDto1;
        }).collect(Collectors.toList());

        return R.success(dishDtoList);
    }
}
