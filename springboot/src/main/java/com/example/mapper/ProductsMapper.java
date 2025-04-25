package com.example.mapper;

import com.example.entity.Products;

import java.util.List;

/**
 * 操作products相关数据接口
 */
public interface ProductsMapper {

    /**
     * 新增
     */
    int insert(Products products);

    /**
     * 删除
     */
    int deleteById(Integer id);

    /**
     * 修改
     */
    int updateById(Products products);

    /**
     * 根据ID查询
     */
    Products selectById(Integer id);

    /**
     * 查询所有
     */
    List<Products> selectAll(Products products);

    /**
     * 获取总产品数
     */
    int getTotalProducts();

    /**
     * 获取本月新增产品数
     */
    int getMonthlyNewProducts();
}