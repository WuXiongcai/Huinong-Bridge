package com.example.mapper;

import com.example.entity.FarmList;
import org.apache.ibatis.annotations.Select;
import java.util.List;

public interface FarmListMapper {
    int insert(FarmList farmList);
    int deleteById(Integer id);
    int updateById(FarmList farmList);
    FarmList selectById(Integer id);
    List<FarmList> selectAll(FarmList farmList);

    @Select("select * from farm_list order by rating desc, monthly_visitors desc limit 20")
    List<FarmList> selectTop20();

}