package com.example.controller;

import com.example.common.Result;
import com.example.entity.Food;
import com.example.service.FoodService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/food")
public class FoodController {

    @Resource
    private FoodService foodService;

    @PostMapping("/add")
    public Result add(@RequestBody Food food) {
        foodService.add(food);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        foodService.deleteById(id);
        return Result.success();
    }

    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        foodService.deleteBatch(ids);
        return Result.success();
    }

    @PutMapping("/update")
    public Result updateById(@RequestBody Food food) {
        foodService.updateById(food);
        return Result.success();
    }

    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Food food = foodService.selectById(id);
        return Result.success(food);
    }

    @GetMapping("/selectAll")
    public Result selectAll(Food food) {
        List<Food> list = foodService.selectAll(food);
        return Result.success(list);
    }

    @GetMapping("/selectPage")
    public Result selectPage(Food food,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Food> page = foodService.selectPage(food, pageNum, pageSize);
        return Result.success(page);
    }

    @GetMapping("/category/{category}")
    public Result selectByCategory(@PathVariable String category,
                                   @RequestParam(defaultValue = "6") Integer limit) {
        List<Food> list = foodService.selectByCategory(category, limit);
        return Result.success(list);
    }

    @GetMapping("/latest")
    public Result selectLatest(@RequestParam(defaultValue = "6") Integer limit) {
        List<Food> list = foodService.selectLatest(limit);
        return Result.success(list);
    }
}