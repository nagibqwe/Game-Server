package com.game.bi.enums;

public enum ResourceType {
    OpenServerGrowUpPoint(1001,"成长之路积分"),
    GodDevilWarScore(100,"天道秘境积分"),
    GuildLastBattleScore(99,"福地论剑积分"),
    KaoShangLingScore(98,"犒赏令积分"),
    HolidayScoreRankScore(97,"积分排名活动积分"),
    RoleLevel(96,"角色等级"),
    ;

    private int id;

    private String name;

    ResourceType(int id, String name){
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
