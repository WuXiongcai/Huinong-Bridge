package com.example.service;

import com.example.entity.Productorders;
import com.example.mapper.ProductordersMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 * 农产品订单管理业务处理
 **/
@Service
public class ProductordersService {

    @Resource
    private ProductordersMapper productordersMapper;

    /**
     * 新增
     */
    public void add(Productorders productorders) {
        productordersMapper.insert(productorders);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        productordersMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            productordersMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Productorders productorders) {
        productordersMapper.updateById(productorders);
    }

    /**
     * 根据ID查询
     */
    public Productorders selectById(Integer id) {
        return productordersMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Productorders> selectAll(Productorders productorders) {
        return productordersMapper.selectAll(productorders);
    }

    /**
     * 分页查询
     */
    public PageInfo<Productorders> selectPage(Productorders productorders, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Productorders> list = productordersMapper.selectAll(productorders);
        return PageInfo.of(list);
    }

    /**
     * 获取订单统计数据
     */
    public Map<String, Object> getStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("totalOrders", productordersMapper.getTotalOrders());
        statistics.put("pendingShipments", productordersMapper.getPendingShipments());
        statistics.put("monthlySales", productordersMapper.getMonthlySales());
        return statistics;
    }
}