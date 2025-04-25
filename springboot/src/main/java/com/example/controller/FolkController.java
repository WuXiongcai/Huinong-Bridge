package com.example.controller;

import com.example.common.Result;
import com.example.entity.Folk;
import com.example.service.FolkService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 民俗文化管理接口
 **/
@RestController
@RequestMapping("/folk")
public class FolkController {

    @Resource
    private FolkService folkService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody Folk folk) {
        folkService.add(folk);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        folkService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        folkService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody Folk folk) {
        folkService.updateById(folk);
        return Result.success();
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Folk folk = folkService.selectById(id);
        return Result.success(folk);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(Folk folk) {
        List<Folk> list = folkService.selectAll(folk);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Folk folk,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Folk> page = folkService.selectPage(folk, pageNum, pageSize);
        return Result.success(page);
    }
}