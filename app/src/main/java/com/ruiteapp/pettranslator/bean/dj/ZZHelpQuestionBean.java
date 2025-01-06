package com.ruiteapp.pettranslator.bean.dj;

import java.io.Serializable;

//开通会员
public class ZZHelpQuestionBean implements Serializable {


    private int id;
    private String name;
    private boolean isSelected;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public String toString() {
        return "HelpQuestionBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", isSelected=" + isSelected +
                '}';
    }
}
