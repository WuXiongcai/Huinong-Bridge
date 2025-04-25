package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.entity.FarmList;
import com.example.mapper.FarmListMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class FarmListService {

    @Resource
    private FarmListMapper farmListMapper;

    public void add(FarmList farmList) {
        farmList.setCreateTime(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        farmListMapper.insert(farmList);
    }

    public void deleteById(Integer id) {
        farmListMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            farmListMapper.deleteById(id);
        }
    }

    public void updateById(FarmList farmList) {
        farmListMapper.updateById(farmList);
    }

    public FarmList selectById(Integer id) {
        return farmListMapper.selectById(id);
    }

    public List<FarmList> selectAll(FarmList farmList) {
        return farmListMapper.selectAll(farmList);
    }

    public PageInfo<FarmList> selectPage(FarmList farmList, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<FarmList> list = farmListMapper.selectAll(farmList);
        return PageInfo.of(list);
    }


    public List<FarmList> selectTop20() {
        return farmListMapper.selectTop20();
    }
}