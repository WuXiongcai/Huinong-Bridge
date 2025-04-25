package com.example.controller;

import com.example.common.Result;
import com.example.entity.Heritage;
import com.example.service.HeritageService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/heritage")
public class HeritageController {

    @Resource
    private HeritageService heritageService;

    @PostMapping("/add")
    public Result add(@RequestBody Heritage heritage) {
        heritageService.add(heritage);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        heritageService.deleteById(id);
        return Result.success();
    }

    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        heritageService.deleteBatch(ids);
        return Result.success();
    }

    @PutMapping("/update")
    public Result updateById(@RequestBody Heritage heritage) {
        heritageService.updateById(heritage);
        return Result.success();
    }

    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Heritage heritage = heritageService.selectById(id);
        return Result.success(heritage);
    }

    @GetMapping("/selectAll")
    public Result selectAll(Heritage heritage) {
        List<Heritage> list = heritageService.selectAll(heritage);
        return Result.success(list);
    }

    @GetMapping("/selectPage")
    public Result selectPage(Heritage heritage,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Heritage> page = heritageService.selectPage(heritage, pageNum, pageSize);
        return Result.success(page);
    }
}