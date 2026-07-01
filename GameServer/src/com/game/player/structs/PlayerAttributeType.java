package com.game.player.structs;

/**
 * @author lw
 */
public enum PlayerAttributeType {
    BASE(1, "基本属性加成"),
    EQUIP(2, "装备属性"),
    BUFF(3, "BUFF属性"),
    MEDICINESATTRIBUTE(4, "属性药"),
    HORSE(5, "坐骑属性加成"),
    MARRIAGE(6, "婚姻"),
    PET(7, "宠物"),
    TITLE(8, "称号"),
    StateVip(9, "境界VIP"),
    Skill(10, "技能"),
    WING(11, "翅膀"),
    Task(12, "任务"),
    SOUL_BEAST(13, "魂兽"),
    AmuletTask(14, "符咒任务属性"),
    Talisman(15, "造化子功能法器"),
    Magic(16, "造化子功能阵法"),
    Weapon(17, "造化子功能神兵"),
    GEM(18, "宝石"),
    ShiHai(19, "识海"),
    Immortalsoul(20, "仙魂系统加成"),
    StifleFabao(21, "灵压法宝"),
    Spirit(22, "装备收集"),
    HolyEquip(23, "圣装系统加成"),
    XiSui(24, "洗髓属性"),
    ImmortalEquip(25, "仙甲属性"),
    HuaxinFlySword(26, "新化形属性"),
    NewFashion(27, "新时装属性"),
    SoulEquip(28, "魂甲系统"),
    DevilSeries(29,"魔魂系统"),
    Vip(30,"VIP系统"),
    UnrealEquip(31,"幻装系统"),
    ;

    final int value;
    final String name;

    PlayerAttributeType(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }


    @Override
    public String toString() {
        return "AttributeType{" +
                "value=" + value +
                ", name='" + name + '\'' +
                '}';
    }
}
