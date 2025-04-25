package com.example.controller;

import com.example.common.Result;
import com.example.entity.Handicraft;
import com.example.service.HandicraftService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/handicraft")
public class HandicraftController {

    @Resource
    private HandicraftService handicraftService;

    @PostMapping("/add")
    public Result add(@RequestBody Handicraft handicraft) {
        handicraftService.add(handicraft);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        handicraftService.deleteById(id);
        return Result.success();
    }

    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        handicraftService.deleteBatch(ids);
        return Result.success();
    }

    @PutMapping("/update")
    public Result updateById(@RequestBody Handicraft handicraft) {
        handicraftService.updateById(handicraft);
        return Result.success();
    }

    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Handicraft handicraft = handicraftService.selectById(id);
        return Result.success(handicraft);
    }

    @GetMapping("/selectAll")
    public Result selectAll(Handicraft handicraft) {
        List<Handicraft> list = handicraftService.selectAll(handicraft);
        return Result.success(list);
    }

    @GetMapping("/selectPage")
    public Result selectPage(Handicraft handicraft,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Handicraft> page = handicraftService.selectPage(handicraft, pageNum, pageSize);
        return Result.success(page);
    }

    @GetMapping("/category/{category}")
    public Result selectByCategory(@PathVariable String category,
                                   @RequestParam(defaultValue = "6") Integer limit) {
        List<Handicraft> list = handicraftService.selectByCategory(category, limit);
        return Result.success(list);
    }

    @GetMapping("/latest")
    public Result selectLatest(@RequestParam(defaultValue = "6") Integer limit) {
        List<Handicraft> list = handicraftService.selectLatest(limit);
        return Result.success(list);
    }
}