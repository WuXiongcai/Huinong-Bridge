package com.example.entity;

import java.io.Serializable;
import java.util.List;

public class SightComment implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer userId;
    private String title;
    private String content;
    private String time;
    private Integer parentId;
    private Integer likes;
    private String userName;
    private String avatar;
    private List<SightComment> replies;

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public List<SightComment> getReplies() {
        return replies;
    }

    public void setReplies(List<SightComment> replies) {
        this.replies = replies;
    }
}