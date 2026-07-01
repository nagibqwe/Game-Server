package com.game.soulbeast.structs;

/**
 * Created by zcd on 2018/5/28.
 */
public enum  EquipColorEnum {
    COLOR_WHITE(1,"白"),
    COLOR_GREEN(2,"绿"),
    COLOR_BLUE(3,"蓝"),
    COLOR_PURPLE(4,"紫"),
    COLOR_ORANGE(5,"橙"),
    COLOR_GOLD(6,"金"),
    COLOR_RED(7,"红"),
    COLOR_PINK(8,"粉"),
    COLOR_DULLGOLD(9,"暗金"),
    COLOR_COLORFUL(10,"幻彩"),
    ;

    private EquipColorEnum(int colorNum, String colorDesc) {
        this.colorNum = colorNum;
        this.colorDesc = colorDesc;
    }

    private final int colorNum;
    private final String colorDesc;

    public int getColorNum() {
        return colorNum;
    }

    public String getColorDesc() {
        return colorDesc;
    }

    public static EquipColorEnum getByColorNum(int colorNum){
        for (EquipColorEnum equipColorEnum : EquipColorEnum.values()) {
            if (equipColorEnum.getColorNum() == colorNum){
                return equipColorEnum;
            }
        }
        return EquipColorEnum.COLOR_GOLD;
    }

}
