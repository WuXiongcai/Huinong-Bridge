package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.entity.Heritage;
import com.example.mapper.HeritageMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class HeritageService {

    @Resource
    private HeritageMapper heritageMapper;

    public void add(Heritage heritage) {
        heritage.setCreateTime(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        heritageMapper.insert(heritage);
    }

    public void deleteById(Integer id) {
        heritageMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            heritageMapper.deleteById(id);
        }
    }

    public void updateById(Heritage heritage) {
        heritageMapper.updateById(heritage);
    }

    public Heritage selectById(Integer id) {
        return heritageMapper.selectById(id);
    }

    public List<Heritage> selectAll(Heritage heritage) {
        return heritageMapper.selectAll(heritage);
    }

    public PageInfo<Heritage> selectPage(Heritage heritage, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Heritage> list = heritageMapper.selectAll(heritage);
        return PageInfo.of(list);
    }
}