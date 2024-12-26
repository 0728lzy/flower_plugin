package com.appcatdog.translations.bean.dj;

import java.io.Serializable;

//开通会员
public class WNCDOpenMemberBean implements Serializable {

    private int id;  //商品原价
    private int type;  //商品类型，0：普通商品、1：续费商品、2：试用产品
    private double costPrice; //商品原价
    private double currPrice;//商品现价
    private String name;//商品名称
    private String description;//商品描述
    private boolean isSelected;

    private WNCDOpenMemberBean renewGoodsVO;


    public WNCDOpenMemberBean getRenewGoodsVO() {
        return renewGoodsVO;
    }

    public void setRenewGoodsVO(WNCDOpenMemberBean renewGoodsVO) {
        this.renewGoodsVO = renewGoodsVO;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(double costPrice) {
        this.costPrice = costPrice;
    }

    public double getCurrPrice() {
        return currPrice;
    }

    public void setCurrPrice(double currPrice) {
        this.currPrice = currPrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "OpenMemberBean{" +
                "id=" + id +
                ", type=" + type +
                ", costPrice=" + costPrice +
                ", currPrice=" + currPrice +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", isSelected=" + isSelected +
                ", renewGoodsVO=" + renewGoodsVO +
                '}';
    }
}
