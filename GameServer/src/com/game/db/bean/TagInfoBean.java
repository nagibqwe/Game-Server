package com.game.db.bean;

import game.core.db.BaseBean;

public class TagInfoBean extends BaseBean {

    private int id;//标签ID

    private String name;//标签名称

    private String icon;//标签图标

    private int style;//UI风格

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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getStyle() {
        return style;
    }

    public void setStyle(int style) {
        this.style = style;
    }
}
