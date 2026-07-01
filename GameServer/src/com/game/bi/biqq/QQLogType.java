package com.game.bi.biqq;

public enum QQLogType {
    login(1, "登录"),
    register(2, "注册"),
    logout(9, "用户登出"),
//    roleLogin(11, "角色登录"),
    roleCreate(12, "创建角色"),
//    roleLogout(13, "角色退出")
    ;

    /**类型*/
    private int type;
    /**描述*/
    private String desc;

    QQLogType(int type, String desc){
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
