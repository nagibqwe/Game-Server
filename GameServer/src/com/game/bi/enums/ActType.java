package com.game.bi.enums;

public enum ActType {
    active(1,"激活"),
    levelUp(2,"升级"),
    train(3,"培养"),
    stageUp(4,"升阶"),
    change(5,"蜕变"),
    evolution(6,"进化"),
    ;
    public int type;
    public String desc;
    private ActType (int type, String desc){
        this.type = type;
        this.desc = desc;
    }
}
