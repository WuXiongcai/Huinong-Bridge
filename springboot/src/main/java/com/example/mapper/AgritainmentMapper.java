package com.example.mapper;

import com.example.entity.Agritainment;
import org.apache.ibatis.annotations.Select;
import java.util.List;

public interface AgritainmentMapper {
    int insert(Agritainment agritainment);
    int deleteById(Integer id);
    int updateById(Agritainment agritainment);
    Agritainment selectById(Integer id);
    List<Agritainment> selectAll(Agritainment agritainment);

    @Select("select * from agritainment order by id desc limit 6")
    List<Agritainment> selectTop6();
}