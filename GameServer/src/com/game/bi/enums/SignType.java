package com.game.bi.enums;

/**
 * @Auther: gouzhongliang
 * @Date: 2021/5/31 16:55
 */
public enum SignType {

    WELFARE(1,"福利每日签到"),
    FestvialWish(2,"节日祝福"),
    ;
    private int id;
    private String name;
    SignType(int id, String name){
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
