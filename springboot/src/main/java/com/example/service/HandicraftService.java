package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.entity.Handicraft;
import com.example.mapper.HandicraftMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class HandicraftService {

    @Resource
    private HandicraftMapper handicraftMapper;

    public void add(Handicraft handicraft) {
        handicraft.setCreateTime(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        handicraftMapper.insert(handicraft);
    }

    public void deleteById(Integer id) {
        handicraftMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            handicraftMapper.deleteById(id);
        }
    }

    public void updateById(Handicraft handicraft) {
        handicraftMapper.updateById(handicraft);
    }

    public Handicraft selectById(Integer id) {
        return handicraftMapper.selectById(id);
    }

    public List<Handicraft> selectAll(Handicraft handicraft) {
        return handicraftMapper.selectAll(handicraft);
    }

    public PageInfo<Handicraft> selectPage(Handicraft handicraft, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Handicraft> list = handicraftMapper.selectAll(handicraft);
        return PageInfo.of(list);
    }

    public List<Handicraft> selectByCategory(String category, Integer limit) {
        return handicraftMapper.selectByCategory(category, limit);
    }

    public List<Handicraft> selectLatest(Integer limit) {
        return handicraftMapper.selectLatest(limit);
    }
}