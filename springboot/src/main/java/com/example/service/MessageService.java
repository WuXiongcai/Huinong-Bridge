package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.entity.Message;
import com.example.mapper.MessageMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MessageService {

    @Resource
    private MessageMapper messageMapper;

    public void add(Message message) {
        message.setTime(DateUtil.now());
        message.setStatus("Pending");
        messageMapper.insert(message);
    }

    public void deleteById(Integer id) {
        messageMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            messageMapper.deleteById(id);
        }
    }

    public void updateById(Message message) {
        messageMapper.updateById(message);
    }

    public Message selectById(Integer id) {
        return messageMapper.selectById(id);
    }

    public List<Message> selectAll(Message message) {
        return messageMapper.selectAll(message);
    }

    public PageInfo<Message> selectPage(Message message, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Message> list = messageMapper.selectAll(message);
        return PageInfo.of(list);
    }

    public List<Message> selectByUserId(Integer userId) {
        return messageMapper.selectByUserId(userId);
    }

    public MessageService(MessageMapper messageMapper) {
        this.messageMapper = messageMapper;
    }

    public Map<String, Integer> getStatistics() {
        Map<String, Integer> statistics = new HashMap<>();
        statistics.put("totalMessages", messageMapper.countTotal());
        statistics.put("monthlyNewMessages", messageMapper.countMonthlyNew());
        statistics.put("pendingMessages", messageMapper.countPending());
        return statistics;
    }
}