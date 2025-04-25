package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.entity.Video;
import com.example.mapper.VideoMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 扶贫助农视频表业务处理
 **/
@Service
public class VideoService {

    @Resource
    private VideoMapper videoMapper;

    /**
     * 新增
     */
    public void add(Video video) {
        video.setUploadTime(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));

        // 处理视频文件路径
        if (video.getVideoUrl() != null && !video.getVideoUrl().startsWith("http")) {
            video.setVideoUrl("/files/" + video.getVideoUrl());
        }

        videoMapper.insert(video);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        videoMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            videoMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Video video) {
        // 处理视频文件路径
        if (video.getVideoUrl() != null && !video.getVideoUrl().startsWith("http")) {
            video.setVideoUrl("/files/" + video.getVideoUrl());
        }

        videoMapper.updateById(video);
    }

    /**
     * 根据ID查询
     */
    public Video selectById(Integer id) {
        return videoMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Video> selectAll(Video video) {
        return videoMapper.selectAll(video);
    }

    /**
     * 分页查询
     */
    public PageInfo<Video> selectPage(Video video, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Video> list = videoMapper.selectAll(video);
        return PageInfo.of(list);
    }

    public List<Video> top6() {
        return videoMapper.selectTop6();
    }
}