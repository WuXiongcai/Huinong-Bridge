package com.example.controller;

import com.example.common.Result;
import com.example.entity.Productorders;
import com.example.service.ProductordersService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 农产品订单管理前端操作接口
 **/
@RestController
@RequestMapping("/productorders")
public class ProductordersController {

    @Resource
    private ProductordersService productordersService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody Productorders productorders) {
        productordersService.add(productorders);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        productordersService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        productordersService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody Productorders productorders) {
        productordersService.updateById(productorders);
        return Result.success();
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Productorders productorders = productordersService.selectById(id);
        return Result.success(productorders);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(Productorders productorders) {
        List<Productorders> list = productordersService.selectAll(productorders);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Productorders productorders,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Productorders> page = productordersService.selectPage(productorders, pageNum, pageSize);
        return Result.success(page);
    }

    /**
     * 获取订单统计数据
     */
    @GetMapping("/statistics")
    public Result getStatistics() {
        Map<String, Object> statistics = productordersService.getStatistics();
        return Result.success(statistics);
    }
}