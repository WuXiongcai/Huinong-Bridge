package com.example.mapper;

import com.example.entity.Message;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface MessageMapper {

    int insert(Message message);

    int deleteById(Integer id);

    int updateById(Message message);

    Message selectById(Integer id);

    List<Message> selectAll(Message message);

    @Select("select * from message where user_id = #{userId} order by id desc")
    List<Message> selectByUserId(Integer userId);

    @Select("SELECT COUNT(*) FROM message")
    int countTotal();

    @Select("SELECT COUNT(*) FROM message WHERE time >= DATE_FORMAT(CURDATE(), '%Y-%m-01')")
    int countMonthlyNew();

    @Select("SELECT COUNT(*) FROM message WHERE status = 'Pending'")
    int countPending();
}