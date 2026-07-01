package com.game.log.grow;

public enum BiType {
    grow(1,"成长"),
    equip(2,"装备"),
    biRealm(3,"境界任务/成就/称号"),
    ;
    private int type;
    private String desc;
    BiType(int type, String desc){
        this.type = type;
        this.desc = desc;
    }

    public int getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }
}
