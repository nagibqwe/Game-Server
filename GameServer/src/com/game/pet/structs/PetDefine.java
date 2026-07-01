package com.game.pet.structs;

public class PetDefine {
    /**
     * 宠物初始阶级
     */
    public static final int INIT_DEGREE = 1;

    /**
     * 宠物激活条件--任务
     */
    public static final int ACTIVE_TYPE_TASK = 0;
    /**
     * 宠物激活条件--前置满阶
     */
    public static final int ACTIVE_TYPE_PRE = 1;
    /**
     * 宠物激活条件--道具
     */
    public static final int ACTIVE_TYPE_ITEM = 2;
    /**
     * 宠物激活条件--宠物功能开启
     */
    public static final int ACTIVE_TYPE_FUNCTION = 3;

    /**
     * 宠物操作--激活
     */
    public static final int PET_OPERATION_ACTIVE = 1;
    /**
     * 宠物操作--强化
     */
    public static final int PET_OPERATION_STRNGTH = 2;
}
