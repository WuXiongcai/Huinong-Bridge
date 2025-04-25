package com.example.controller;

import com.example.common.Result;
import com.example.entity.Sight;
import com.example.service.SightService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/sight")
public class SightController {

    @Resource
    private SightService sightService;

    @PostMapping("/add")
    public Result add(@RequestBody Sight sight) {
        sightService.add(sight);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        sightService.deleteById(id);
        return Result.success();
    }

    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        sightService.deleteBatch(ids);
        return Result.success();
    }

    @PutMapping("/update")
    public Result updateById(@RequestBody Sight sight) {
        sightService.updateById(sight);
        return Result.success();
    }

    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Sight sight = sightService.selectById(id);
        return Result.success(sight);
    }

    @GetMapping("/selectAll")
    public Result selectAll(Sight sight) {
        List<Sight> list = sightService.selectAll(sight);
        return Result.success(list);
    }

    @GetMapping("/selectPage")
    public Result selectPage(Sight sight,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Sight> page = sightService.selectPage(sight, pageNum, pageSize);
        return Result.success(page);
    }
}