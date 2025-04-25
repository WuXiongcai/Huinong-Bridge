package com.example.mapper;

import com.example.entity.Orders;

import java.util.List;
import java.math.BigDecimal;
/**
 * 操作orders相关数据接口
*/
public interface OrdersMapper {

    /**
      * 新增
    */
    int insert(Orders orders);

    /**
      * 删除
    */
    int deleteById(Integer id);

    /**
      * 修改
    */
    int updateById(Orders orders);

    /**
      * 根据ID查询
    */
    Orders selectById(Integer id);

    /**
      * 查询所有
    */
    List<Orders> selectAll(Orders orders);


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