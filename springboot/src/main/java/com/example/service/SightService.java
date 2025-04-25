package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.entity.Sight;
import com.example.mapper.SightMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class SightService {

    @Resource
    private SightMapper sightMapper;

    public void add(Sight sight) {
        String now = DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss");
        sight.setCreateTime(now);
        sight.setUpdateTime(now);
        sightMapper.insert(sight);
    }

    public void deleteById(Integer id) {
        sightMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            sightMapper.deleteById(id);
        }
    }

    public void updateById(Sight sight) {
        sight.setUpdateTime(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        sightMapper.updateById(sight);
    }

    public Sight selectById(Integer id) {
        return sightMapper.selectById(id);
    }

    public List<Sight> selectAll(Sight sight) {
        return sightMapper.selectAll(sight);
    }

    public PageInfo<Sight> selectPage(Sight sight, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Sight> list = sightMapper.selectAll(sight);
        return PageInfo.of(list);
    }
}