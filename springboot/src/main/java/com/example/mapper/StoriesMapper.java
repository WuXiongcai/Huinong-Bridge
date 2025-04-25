package com.example.mapper;

import com.example.entity.Stories;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 操作stories相关数据接口
 */
public interface StoriesMapper {

    /**
     * 新增
     */
    int insert(Stories stories);

    /**
     * 删除
     */
    int deleteById(Integer id);

    /**
     * 修改
     */
    int updateById(Stories stories);

    /**
     * 根据ID查询
     */
    Stories selectById(Integer id);

    /**
     * 查询所有
     */
    List<Stories> selectAll(Stories stories);

    @Select("select * from stories order by id desc limit 6")
    List<Stories> selectTop6();
}