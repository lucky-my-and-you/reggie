package com.zhang.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhang.reggie.common.R;
import com.zhang.reggie.entity.Category;
import com.zhang.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;


    /**
     * 添加菜品
     * @param category
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody Category category){
        categoryService.save(category);
        return R.success("添加成功");
    }

    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize){

        //构造分页构造器
        Page pageInfo = new Page(page,pageSize);

        //构造条件构造器
        LambdaQueryWrapper<Category> wrapper=new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Category::getSort);

        //执行查询
        categoryService.page(pageInfo,wrapper);

        return R.success(pageInfo);
    }

    /**
     * 删除
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(Long ids){

        log.info("删除的id {}",ids);

        categoryService.remove(ids);

        return R.success("删除成功");
    }

    /**
     * 修改
     * @param category
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody Category category){
        log.info(category.toString());
        categoryService.updateById(category);
        return R.success("修改成功");
    }

    /**
     * 根据条件查询分页数据
     * @param category
     * @return
     */
    @GetMapping("/list")
    public R<List<Category>> list(Category category){

        //构造条件构造器
        LambdaQueryWrapper<Category> wrapper=new LambdaQueryWrapper<>();
        //根据type查询
        wrapper.eq(category.getType()!=null,Category::getType,category.getType());
        //排序
        wrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        //查询符合条件的数据
        List<Category> list = categoryService.list(wrapper);

        return R.success(list);
    }
}
