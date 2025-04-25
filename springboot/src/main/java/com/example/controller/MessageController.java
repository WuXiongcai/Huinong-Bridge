package com.example.controller;

import com.example.common.Result;
import com.example.entity.Message;
import com.example.service.MessageService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/message")
public class MessageController {

    @Resource
    private MessageService messageService;

    @PostMapping("/add")
    public Result add(@RequestBody Message message) {
        messageService.add(message);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        messageService.deleteById(id);
        return Result.success();
    }

    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        messageService.deleteBatch(ids);
        return Result.success();
    }

    @PutMapping("/update")
    public Result updateById(@RequestBody Message message) {
        messageService.updateById(message);
        return Result.success();
    }

    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Message message = messageService.selectById(id);
        return Result.success(message);
    }

    @GetMapping("/selectAll")
    public Result selectAll(Message message) {
        List<Message> list = messageService.selectAll(message);
        return Result.success(list);
    }

    @GetMapping("/selectPage")
    public Result selectPage(Message message,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Message> page = messageService.selectPage(message, pageNum, pageSize);
        return Result.success(page);
    }

    @GetMapping("/selectByUserId")
    public Result selectByUserId(@RequestParam Integer userId) {
        List<Message> list = messageService.selectByUserId(userId);
        return Result.success(list);
    }

    /**
     * 获取统计数据
     */
    @GetMapping("/statistics")
    public Result getStatistics() {
        try {
            Map<String, Integer> statistics = messageService.getStatistics();
            return Result.success(statistics);
        } catch (Exception e) {
            return Result.error("500", "获取统计数据失败: " + e.getMessage());
        }
    }
}