package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.entity.Agritainment;
import com.example.mapper.AgritainmentMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class AgritainmentService {

    @Resource
    private AgritainmentMapper agritainmentMapper;

    public void add(Agritainment agritainment) {
        String now = DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss");
        agritainment.setCreateTime(now);
        agritainment.setUpdateTime(now);
        agritainmentMapper.insert(agritainment);
    }

    public void deleteById(Integer id) {
        agritainmentMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            agritainmentMapper.deleteById(id);
        }
    }

    public void updateById(Agritainment agritainment) {
        agritainment.setUpdateTime(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        agritainmentMapper.updateById(agritainment);
    }

    public Agritainment selectById(Integer id) {
        return agritainmentMapper.selectById(id);
    }

    public List<Agritainment> selectAll(Agritainment agritainment) {
        return agritainmentMapper.selectAll(agritainment);
    }

    public PageInfo<Agritainment> selectPage(Agritainment agritainment, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Agritainment> list = agritainmentMapper.selectAll(agritainment);
        return PageInfo.of(list);
    }
}