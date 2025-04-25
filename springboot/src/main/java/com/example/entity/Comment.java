package com.example.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 评论信息表
*/
public class Comment implements Serializable {
    private static final long serialVersionUID = 1L;

    /** ID */
    private Integer id;
    private Integer userId;
    private Integer policyId;
    private String time;
    private Integer parentId;
    private String content;
    private String tmp = "";

    private String userName;
    private String avatar;

    private List<Comment> children;

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

    public Integer getPolicyId() {
        return policyId;
    }

    public void setPolicyId(Integer policyId) {
        this.policyId = policyId;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public List<Comment> getChildren() {
        return children;
    }

    public void setChildren(List<Comment> children) {
        this.children = children;
    }

    public String getTmp() {
        return tmp;
    }

    public void setTmp(String tmp) {
        this.tmp = tmp;
    }
}