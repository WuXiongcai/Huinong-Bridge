package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.entity.Food;
import com.example.mapper.FoodMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class FoodService {

    @Resource
    private FoodMapper foodMapper;

    public void add(Food food) {
        food.setCreateTime(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        foodMapper.insert(food);
    }

    public void deleteById(Integer id) {
        foodMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            foodMapper.deleteById(id);
        }
    }

    public void updateById(Food food) {
        foodMapper.updateById(food);
    }

    public Food selectById(Integer id) {
        return foodMapper.selectById(id);
    }

    public List<Food> selectAll(Food food) {
        return foodMapper.selectAll(food);
    }

    public PageInfo<Food> selectPage(Food food, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Food> list = foodMapper.selectAll(food);
        return PageInfo.of(list);
    }

    public List<Food> selectByCategory(String category, Integer limit) {
        return foodMapper.selectByCategory(category, limit);
    }

    public List<Food> selectLatest(Integer limit) {
        return foodMapper.selectLatest(limit);
    }
}