package com.example.mapper;

import com.example.entity.SightComment;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import java.util.List;

public interface SightCommentMapper {
    int insert(SightComment comment);
    int deleteById(Integer id);
    int updateById(SightComment comment);
    SightComment selectById(Integer id);
    List<SightComment> selectAll(SightComment comment);

    @Select("SELECT COUNT(*) FROM sight_comment")
    int getTotalComments();

    @Select("SELECT COUNT(*) FROM sight_comment WHERE time >= DATE_FORMAT(CURDATE(), '%Y-%m-01')")
    int getMonthlyNewComments();

    @Select("SELECT SUM(likes) FROM sight_comment")
    int getTotalLikes();

    @Update("UPDATE sight_comment SET likes = likes + 1 WHERE id = #{id}")
    int incrementLikes(Integer id);

    @Update("UPDATE sight_comment SET likes = likes - 1 WHERE id = #{id}")
    int decrementLikes(Integer id);
}