package com.game.log.grow;

public enum ActType {
    active(1,"激活"),
    levelUp(2,"升级"),
    train(3,"培养"),
    stageUp(4,"升阶"),
    change(5,"蜕变"),
    evolution(6,"进化"),
    intensify_active(7,"强化激活"),
    soul_active(8,"附魂激活"),
    star_up(9, "升星"),
    reset(10, "重置"),
    wake(11, "觉醒"),
    unlock(12, "解锁"),
    goBack(13,"收回"),


    equip_wear(1,"装备穿戴"),
    equip_synthesis(2,"装备合成"),
    equip_intensify(3,"装备强化"),
    equip_wash(4,"装备洗练"),
    gem_inlay(5,"宝石镶嵌"),
    gem_level_up(6,"宝石升级"),
    equip_suit_active(7,"装备套装激活"),
    equip_soul(8,"装备附魂"),
    gem_refine(9,"宝石精炼"),

    //1穿戴2合成3强化4洗练5宝石镶嵌6宝石升级7套装激活
    ;
    public int type;
    public String desc;
    private ActType(int type, String desc){
        this.type = type;
        this.desc = desc;
    }
}
