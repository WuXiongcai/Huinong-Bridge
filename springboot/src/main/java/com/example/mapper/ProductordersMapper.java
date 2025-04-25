package com.example.mapper;

import com.example.entity.Productorders;

import java.util.List;
import java.math.BigDecimal;

/**
 * 操作productorders相关数据接口
 */
public interface ProductordersMapper {

    /**
     * 新增
     */
    int insert(Productorders productorders);

    /**
     * 删除
     */
    int deleteById(Integer id);

    /**
     * 修改
     */
    int updateById(Productorders productorders);

    /**
     * 根据ID查询
     */
    Productorders selectById(Integer id);

    /**
     * 查询所有
     */
    List<Productorders> selectAll(Productorders productorders);

    /**
     * 获取总订单数
     */
    int getTotalOrders();

    /**
     * 获取待发货订单数
     */
    int getPendingShipments();

    /**
     * 获取本月销售额
     */
    BigDecimal getMonthlySales();
}