package com.example.mapper;

import com.example.entity.Poorapply;

import java.util.List;

/**
 * 操作poorapply相关数据接口
*/
public interface PoorapplyMapper {

    /**
      * 新增
    */
    int insert(Poorapply poorapply);

    /**
      * 删除
    */
    int deleteById(Integer id);

    /**
      * 修改
    */
    int updateById(Poorapply poorapply);

    /**
      * 根据ID查询
    */
    Poorapply selectById(Integer id);

    /**
      * 查询所有
    */
    List<Poorapply> selectAll(Poorapply poorapply);

    /**
     * 统计所有申请数量
     */
    int countAllApplications();

    /**
     * 统计待审核申请数量
     */
    int countPendingApplications();

    /**
     * 统计本月通过的申请数量
     */
    int countMonthlyApprovedApplications();


}