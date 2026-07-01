package com.game.soulbeast.structs;

/**
 * Created by zcd on 2018/5/7.
 */
public enum PartEnum {
    TOU_KUI(1, "头盔"),
    XIANG_QUAN(2, "项圈"),
    KAI_JIA(3, "铠甲"),
    LI_ZHUA(4, "利爪"),
    YU_YI(5, "羽翼");

    private final int value;
    private final String name;

    // 构造器必须是私有的
    PartEnum(int value, String name) {
        this.name = name;
        this.value = value;
    }

    /**
     * 设置value.
     *
     * @return 返回value
     */
    public int getValue() {
        return value;
    }

    /**
     * 设置desc.
     *
     * @return 返回desc
     */
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "PartEnum{" +
                "value=" + value +
                ", name='" + name + '\'' +
                '}';
    }
}
