package com.zhang.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhang.reggie.common.R;
import com.zhang.reggie.dto.SetmealDto;
import com.zhang.reggie.entity.Category;
import com.zhang.reggie.entity.Setmeal;
import com.zhang.reggie.service.CategoryService;
import com.zhang.reggie.service.SetmealDishService;
import com.zhang.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/setmeal")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private CategoryService categoryService;


    /**
     * 添加
     *
     * @param setmealDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto) {
        setmealService.saveWithDish(setmealDto);
        return R.success("添加成功");
    }

    /**
     * 分页查询
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {

        //构造分页器
        Page<Setmeal> setmealPage = new Page<>(page, pageSize);
        Page<SetmealDto> setmealDtoPage = new Page<>();

        //构造查询条件
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        //模糊查询
        queryWrapper.like(name != null, Setmeal::getName, name);
        //添加排序条件
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        //执行查询
        setmealService.page(setmealPage, queryWrapper);

        //拷贝分页
        BeanUtils.copyProperties(setmealPage, setmealDtoPage, "records");

        List<Setmeal> records = setmealPage.getRecords();

        List<SetmealDto> list = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            //拷贝数据
            BeanUtils.copyProperties(item, setmealDto);

            Long categoryId = item.getCategoryId();

            //根据分类id查询分类对象
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                //分类名称
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }
            return setmealDto;
        }).collect(Collectors.toList());

        setmealDtoPage.setRecords(list);

        return R.success(setmealDtoPage);
    }

    /**
     * (批量）停售/起售
     *
     * @param status
     * @param ids
     * @return
     */
    @PostMapping("status/{status}")
    public R<String> upstatus(@PathVariable Integer status, Long[] ids) {

        for (Long id : ids) {
            Setmeal byId = setmealService.getById(id);
            byId.setStatus(status);
            setmealService.updateById(byId);
        }
        return R.success("更新成功");
    }

    /**
     * （批量） 删除套餐,同时删除对应的套餐分类
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(Long[] ids) {

        for (Long id : ids) {

            Setmeal byId = setmealService.getById(id);
            Integer status = byId.getStatus();
            if (status == 0) {
                setmealService.removeById(id);

                //添加条件查询
                LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(Category::getId, id);
                categoryService.remove(queryWrapper);
            }else {
                return R.error("删除失败");
            }
        }
        return R.success("删除成功");
    }

    /**
     * 移动端查询套餐
     * @return
     */
    @GetMapping("/list")
    public R<List<Setmeal>> list(Setmeal setmeal){
        LambdaQueryWrapper<Setmeal> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(setmeal.getCategoryId()!=null,Setmeal::getCategoryId,setmeal.getCategoryId());
        queryWrapper.eq(setmeal.getStatus()!=0,Setmeal::getStatus,setmeal.getStatus());
        List<Setmeal> list = setmealService.list(queryWrapper);
        return R.success(list);
    }
}
