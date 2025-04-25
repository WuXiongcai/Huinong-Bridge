package com.example.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 扶贫商品表
*/
public class Goods implements Serializable {
    private static final long serialVersionUID = 1L;

    /** ID */
    private Integer id;
    private String name;
    private Double price;
    private Integer num;
    private Integer sales;
    private String img;
    private String category;
    private String farmer;
    private Date createTime;

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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Integer getSales() {
        return sales;
    }

    public void setSales(Integer sales) {
        this.sales = sales;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getFarmer() {
        return farmer;
    }

    public void setFarmer(String farmer) {
        this.farmer = farmer;
    }

    // getter 和 setter 方法
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}