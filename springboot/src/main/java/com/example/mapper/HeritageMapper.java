package com.example.mapper;

import com.example.entity.Heritage;
import org.apache.ibatis.annotations.Select;
import java.util.List;

public interface HeritageMapper {
    int insert(Heritage heritage);
    int deleteById(Integer id);
    int updateById(Heritage heritage);
    Heritage selectById(Integer id);
    List<Heritage> selectAll(Heritage heritage);
}