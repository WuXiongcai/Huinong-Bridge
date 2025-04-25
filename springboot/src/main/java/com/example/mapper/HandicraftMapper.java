package com.example.mapper;

import com.example.entity.Handicraft;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface HandicraftMapper {
    int insert(Handicraft handicraft);
    int deleteById(Integer id);
    int updateById(Handicraft handicraft);
    Handicraft selectById(Integer id);
    List<Handicraft> selectAll(Handicraft handicraft);

    List<Handicraft> selectByCategory(@Param("category") String category, @Param("limit") Integer limit);

    @Select("select * from handicraft order by create_time desc limit #{limit}")
    List<Handicraft> selectLatest(Integer limit);
}