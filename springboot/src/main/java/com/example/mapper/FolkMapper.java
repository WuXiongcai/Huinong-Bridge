package com.example.mapper;

import com.example.entity.Folk;
import org.apache.ibatis.annotations.Select;
import java.util.List;

public interface FolkMapper {
    int insert(Folk folk);
    int deleteById(Integer id);
    int updateById(Folk folk);
    Folk selectById(Integer id);
    List<Folk> selectAll(Folk folk);

    @Select("select * from folk order by id desc limit 6")
    List<Folk> selectTop6();
}