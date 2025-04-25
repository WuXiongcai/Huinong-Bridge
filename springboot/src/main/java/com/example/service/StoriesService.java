package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.entity.Account;
import com.example.entity.Stories;
import com.example.mapper.StoriesMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 扶贫资讯表业务处理
 **/
@Service
public class StoriesService {

    @Resource
    private StoriesMapper storiesMapper;

    /**
     * 新增
     */
    public void add(Stories stories) {
        stories.setTime(DateUtil.format(new Date(), "yyyy-MM-dd"));
        storiesMapper.insert(stories);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        storiesMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            storiesMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Stories stories) {
        storiesMapper.updateById(stories);
    }

    /**
     * 根据ID查询
     */
    public Stories selectById(Integer id) {
        return storiesMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Stories> selectAll(Stories stories) {
        return storiesMapper.selectAll(stories);
    }

    /**
     * 分页查询
     */
    public PageInfo<Stories> selectPage(Stories stories, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Stories> list = storiesMapper.selectAll(stories);
        return PageInfo.of(list);
    }

    public List<Stories> top6() {
        return storiesMapper.selectTop6();
    }
}