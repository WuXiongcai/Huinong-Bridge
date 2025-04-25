package com.example.mapper;

import com.example.entity.Sight;
import org.apache.ibatis.annotations.Select;
import java.util.List;

public interface SightMapper {
    int insert(Sight sight);
    int deleteById(Integer id);
    int updateById(Sight sight);
    Sight selectById(Integer id);
    List<Sight> selectAll(Sight sight);

    @Select("select * from sight order by id desc limit 6")
    List<Sight> selectTop6();
}