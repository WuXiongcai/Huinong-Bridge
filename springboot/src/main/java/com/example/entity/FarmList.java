package com.example.entity;

public class FarmList {
    private Integer id;
    private String name;
    private Double rating;
    private Integer reviewCount;
    private Integer monthlyVisitors;
    private String tags;
    private String image;
    private String createTime;

    // Getters and Setters
    // ... 省略标准的 getter 和 setter 方法

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Integer getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(Integer reviewCount) {
        this.reviewCount = reviewCount;
    }

    public Integer getMonthlyVisitors() {
        return monthlyVisitors;
    }

    public void setMonthlyVisitors(Integer monthlyVisitors) {
        this.monthlyVisitors = monthlyVisitors;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
}