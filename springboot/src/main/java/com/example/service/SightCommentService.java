package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.entity.SightComment;
import com.example.mapper.SightCommentMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SightCommentService {

    @Resource
    private SightCommentMapper sightCommentMapper;

    public void add(SightComment comment) {
        comment.setTime(DateUtil.now());
        comment.setLikes(0);
        sightCommentMapper.insert(comment);
    }

    public void deleteById(Integer id) {
        sightCommentMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            sightCommentMapper.deleteById(id);
        }
    }

    public void updateById(SightComment comment) {
        sightCommentMapper.updateById(comment);
    }

    public SightComment selectById(Integer id) {
        return sightCommentMapper.selectById(id);
    }

    public List<SightComment> selectAll(SightComment comment) {
        return sightCommentMapper.selectAll(comment);
    }

    public PageInfo<SightComment> selectPage(SightComment comment, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<SightComment> list = sightCommentMapper.selectAll(comment);
        return PageInfo.of(list);
    }

    public Map<String, Integer> getStatistics() {
        Map<String, Integer> statistics = new HashMap<>();
        statistics.put("totalComments", sightCommentMapper.getTotalComments());
        statistics.put("monthlyNewComments", sightCommentMapper.getMonthlyNewComments());
        statistics.put("totalLikes", sightCommentMapper.getTotalLikes());
        return statistics;
    }

    public void toggleLike(Integer id, boolean isLike) {
        if (isLike) {
            sightCommentMapper.incrementLikes(id);
        } else {
            sightCommentMapper.decrementLikes(id);
        }
    }
}