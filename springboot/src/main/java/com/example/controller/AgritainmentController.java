package com.example.controller;

import com.example.common.Result;
import com.example.entity.Agritainment;
import com.example.service.AgritainmentService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/agritainment")
public class AgritainmentController {

    @Resource
    private AgritainmentService agritainmentService;

    @PostMapping("/add")
    public Result add(@RequestBody Agritainment agritainment) {
        agritainmentService.add(agritainment);
        return Result.success();
    }


    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        agritainmentService.deleteById(id);
        return Result.success();
    }

    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        agritainmentService.deleteBatch(ids);
        return Result.success();
    }

    @PutMapping("/update")
    public Result updateById(@RequestBody Agritainment agritainment) {
        agritainmentService.updateById(agritainment);
        return Result.success();
    }

    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Agritainment agritainment = agritainmentService.selectById(id);
        return Result.success(agritainment);
    }

    @GetMapping("/selectAll")
    public Result selectAll(Agritainment agritainment) {
        List<Agritainment> list = agritainmentService.selectAll(agritainment);
        return Result.success(list);
    }

    @GetMapping("/selectPage")
    public Result selectPage(Agritainment agritainment,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Agritainment> page = agritainmentService.selectPage(agritainment, pageNum, pageSize);
        return Result.success(page);
    }
}