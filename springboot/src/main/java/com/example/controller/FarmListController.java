package com.example.controller;

import com.example.common.Result;
import com.example.entity.FarmList;
import com.example.service.FarmListService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/farmList")
public class FarmListController {

    @Resource
    private FarmListService farmListService;

    @PostMapping("/add")
    public Result add(@RequestBody FarmList farmList) {
        farmListService.add(farmList);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        farmListService.deleteById(id);
        return Result.success();
    }

    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        farmListService.deleteBatch(ids);
        return Result.success();
    }

    @PutMapping("/update")
    public Result updateById(@RequestBody FarmList farmList) {
        farmListService.updateById(farmList);
        return Result.success();
    }

    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        FarmList farmList = farmListService.selectById(id);
        return Result.success(farmList);
    }

    @GetMapping("/selectAll")
    public Result selectAll(FarmList farmList) {
        List<FarmList> list = farmListService.selectAll(farmList);
        return Result.success(list);
    }

    @GetMapping("/selectPage")
    public Result selectPage(FarmList farmList,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<FarmList> page = farmListService.selectPage(farmList, pageNum, pageSize);
        return Result.success(page);
    }

    @GetMapping("/selectTop20")
    public Result selectTop20() {
        List<FarmList> list = farmListService.selectTop20();
        return Result.success(list);
    }
}