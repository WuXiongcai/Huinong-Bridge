package com.example.service;

import com.example.entity.Products;
import com.example.mapper.ProductsMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 农产品表业务处理
 */
@Service
public class ProductsService {

    @Resource
    private ProductsMapper productsMapper;

    /**
     * 新增
     */
    public void add(Products products) {
        productsMapper.insert(products);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        productsMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            productsMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Products products) {
        productsMapper.updateById(products);
    }

    /**
     * 根据ID查询
     */
    public Products selectById(Integer id) {
        return productsMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Products> selectAll(Products products) {
        return productsMapper.selectAll(products);
    }

    /**
     * 分页查询
     */
    public PageInfo<Products> selectPage(Products products, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Products> list = productsMapper.selectAll(products);
        return PageInfo.of(list);
    }

    /**
     * 获取统计数据
     */
    public Map<String, Integer> getStatistics() {
        Map<String, Integer> statistics = new HashMap<>();
        statistics.put("totalProducts", productsMapper.getTotalProducts());
        statistics.put("monthlyNewProducts", productsMapper.getMonthlyNewProducts());
        statistics.put("helpedFarmers", 99); // Fixed value as requested
        return statistics;
    }
}