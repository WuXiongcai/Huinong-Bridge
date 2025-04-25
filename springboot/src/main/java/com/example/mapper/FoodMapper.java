package com.example.mapper;

import com.example.entity.Food;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface FoodMapper {
    int insert(Food food);
    int deleteById(Integer id);
    int updateById(Food food);
    Food selectById(Integer id);
    List<Food> selectAll(Food food);

    // 删除这个注解方法，改用xml中的实现
    // @Select("select * from food where category = #{category} order by create_time desc limit #{limit}")
    List<Food> selectByCategory(@Param("category") String category, @Param("limit") Integer limit);

    @Select("select * from food order by create_time desc limit #{limit}")
    List<Food> selectLatest(Integer limit);
}