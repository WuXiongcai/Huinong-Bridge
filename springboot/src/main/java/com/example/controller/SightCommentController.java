package com.example.controller;

import com.example.common.Result;
import com.example.entity.SightComment;
import com.example.service.SightCommentService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/sightcomment")
public class SightCommentController {

    @Resource
    private SightCommentService sightCommentService;

    @PostMapping("/add")
    public Result add(@RequestBody SightComment comment) {
        sightCommentService.add(comment);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        sightCommentService.deleteById(id);
        return Result.success();
    }

    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        sightCommentService.deleteBatch(ids);
        return Result.success();
    }

    @PutMapping("/update")
    public Result updateById(@RequestBody SightComment comment) {
        sightCommentService.updateById(comment);
        return Result.success();
    }

    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        SightComment comment = sightCommentService.selectById(id);
        return Result.success(comment);
    }

    @GetMapping("/selectAll")
    public Result selectAll(SightComment comment) {
        List<SightComment> list = sightCommentService.selectAll(comment);
        return Result.success(list);
    }

    @GetMapping("/page")
    public Result selectPage(SightComment comment,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<SightComment> page = sightCommentService.selectPage(comment, pageNum, pageSize);
        return Result.success(page);
    }

    @GetMapping("/statistics")
    public Result getStatistics() {
        return Result.success(sightCommentService.getStatistics());
    }

    @PostMapping("/like/{id}")
    public Result like(@PathVariable Integer id, @RequestParam(defaultValue = "true") boolean isLike) {
        sightCommentService.toggleLike(id, isLike);
        return Result.success();
    }

    @GetMapping("/list")
    public Result list() {
        SightComment comment = new SightComment();
        comment.setParentId(0);  // 只获取主评论
        List<SightComment> comments = sightCommentService.selectAll(comment);
        // 获取每个主评论的回复
        for (SightComment mainComment : comments) {
            SightComment query = new SightComment();
            query.setParentId(mainComment.getId());
            List<SightComment> replies = sightCommentService.selectAll(query);
            mainComment.setReplies(replies);
        }
        return Result.success(comments);
    }

    @PostMapping("/reply")
    public Result reply(@RequestBody SightComment comment) {
        sightCommentService.add(comment);
        return Result.success();
    }
}