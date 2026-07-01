package com.game.dailyactive.structs;

import com.game.boss.struct.BossTypeConst;

public enum DailyActiveDefine {

    SingleTower(1, "万妖卷"),

    LeaderPreach(2, "掌门传道"),

    EXP_DAILY(3, "赏金之道"),

    WORLD_BOSS(4, "无极墟域", BossTypeConst.WORLD_BOSS),

    JJC(5, "竞技场"),

    FairyLand(6, "天禁之门"),

    StarCopy(7, "大能遗府"),

    ExpCopy(8, "凌云妖塔"),

    EquipCopy(9, "心魔幻境"),

    StrengthenCopy(10, "锁灵台"),

    StateBoss(11, "个人首领"),

    SUIT_BOSS(12, "套装boss", BossTypeConst.SUIT_BOSS),

    GemBoss(13, "宝石灵域"),

    SOUL_ANIMAL_ISLAND_BOSS(14, "年兽封域", BossTypeConst.SOULANIMAL_BOSS),

    MONEY_DAILY(15, "神兵日常"),

    ACTIVITY_LOCAl_SOULANIMALISLAND(16, "年兽封域（本服）"),

    GUILD_TASK(17, "仙盟任务"),

    WEEKT_TASK(18, "周常任务"),

    ELITE_BOSS(19, "精英狩猎"),

    Un_Limit_BOSS(20, "无限首领"),

    PeakPk(21, "巅峰竞技场"),

    CrossHosreBoss(22,"跨服坐骑BOSS" ,BossTypeConst.CROSS_HORSE_BOSS),

    QingyiZengSong(23,"情谊赠送"),

    HOME_BOSS(101, "VIP首领", BossTypeConst.HOME_BOSS),

    YZZD(102, "天芒鬼城"),

    GodDevilWar(103, "天道秘境"),

    WORLD_ANSWER_QUESTION(104, "心境博弈"),

    ACTIVITY_WORLD_BONFIRE(105, "日暮篝火"),

    EIGHT_DIAGRAMS(106, "八级阵图"),

    ACTIVITY_CROSS_MANORWAR(108, "本服领地"),

    ACTIVITY_CROSS_UNIVERSEWAR(109, "混沌之境"),

    ACTIVITY_GUILDBATTLE(110, "仙盟战"),

    ACTIVITY_GUILD_BOSS(111, "仙盟首领"),

    GUILD_LAST_BATTLE(112, "福地论剑"),

    CROSS_GUILD_FUD(113, "跨服福地"),

    CROSS_FUD_Devil(114, "魔王缝隙"),

    CROSS_Alien_Boss(116, "混沌虚空"),

    Marry(201, "婚姻副本"),

    NINE_DAYS_FOCUSED(202, "九天争锋"),

    GUILD_ACTIVITY_WAR(203, "帮会战"),

    GUILD_ACTIVITY_GUARD(204, "守护宗派"),

    ZP_WEEK(205, "宗派周常"),

    ZP_DAILY(206, "宗派日常"),

    FUD_ACTIVITY_BOSS(207, "福地争夺"),

    CRAZY_WEEK(208, "狂欢周"),

    COUPLE_ESCORT(209, "仙侣护送"),
    ;

    final int value;  //日常ID
    final int bossType;
    final String name;

    DailyActiveDefine(int value, String name) {
        this.value = value;
        this.name = name;
        this.bossType = 0;
    }

    DailyActiveDefine(int value, String name, int bossType) {
        this.value = value;
        this.name = name;
        this.bossType = bossType;
    }

    public static DailyActiveDefine find(int value) {
        for (DailyActiveDefine daily : DailyActiveDefine.values()) {
            if (daily.getValue() == value) {
                return daily;
            }
        }
        return null;
    }
    public static DailyActiveDefine findByBoss(int value) {
        for (DailyActiveDefine daily : DailyActiveDefine.values()) {
            if (daily.getBossType() == value) {
                return daily;
            }
        }
        return null;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public int getBossType() {
        return bossType;
    }
}
