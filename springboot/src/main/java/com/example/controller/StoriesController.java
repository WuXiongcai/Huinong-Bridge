package com.example.controller;

import com.example.common.Result;
import com.example.entity.Stories;
import com.example.service.StoriesService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 扶贫助农故事表前端操作接口
 **/
@RestController
@RequestMapping("/stories")
public class StoriesController {

    @Resource
    private StoriesService storiesService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody Stories stories) {
        storiesService.add(stories);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        storiesService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        storiesService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody Stories stories) {
        storiesService.updateById(stories);
        return Result.success();
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Stories stories = storiesService.selectById(id);
        return Result.success(stories);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(Stories stories) {
        List<Stories> list = storiesService.selectAll(stories);
        return Result.success(list);
    }

    @GetMapping("/top6")
    public Result top6() {
        List<Stories> list = storiesService.top6();
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Stories stories,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Stories> page = storiesService.selectPage(stories, pageNum, pageSize);
        return Result.success(page);
    }
}