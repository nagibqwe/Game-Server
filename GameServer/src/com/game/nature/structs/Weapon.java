package com.game.nature.structs;
import java.util.ArrayList;
import java.util.List;

public class Weapon extends Nature {
    /**
     * 属性列表
     * */
    private List<WeaponAttribute> attributes = new ArrayList<>();

    /**
     * 状态 0没有初始化 1已经初始化
     */
    private int status;

    public static final int status_init = 1;

    public List<WeaponAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<WeaponAttribute> attributes) {
        this.attributes = attributes;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
