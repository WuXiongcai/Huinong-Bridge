package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.entity.Folk;
import com.example.mapper.FolkMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 民俗文化管理服务
 **/
@Service
public class FolkService {

    @Resource
    private FolkMapper folkMapper;

    /**
     * 新增
     */
    public void add(Folk folk) {
        folk.setCreateTime(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        folkMapper.insert(folk);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        folkMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            folkMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Folk folk) {
        folkMapper.updateById(folk);
    }

    /**
     * 根据ID查询
     */
    public Folk selectById(Integer id) {
        return folkMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Folk> selectAll(Folk folk) {
        return folkMapper.selectAll(folk);
    }

    /**
     * 分页查询
     */
    public PageInfo<Folk> selectPage(Folk folk, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Folk> list = folkMapper.selectAll(folk);
        return PageInfo.of(list);
    }
}