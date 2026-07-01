package com.game.chat.structs;

/**
 * 分享类型枚举
 *
 * @author luosv
 * Created on 2018/6/7 0007.
 */
public class ShareType {

    /**
     * 获得装备(参数：物品配置ID)
     */
    public static final int SHARE_GET_EQUIP = 1;

    /**
     * 等级提升(参数：升级前等级)
     */
    public static final int SHARE_UP_LEVEL = 2;

    /**
     * 玩家转职(参数：当前转职阶数)
     */
    public static final int SHARE_CHANGE_JOB = 3;

    /**
     * 获得时装(参数：时装配置ID)
     */
    public static final int SHARE_GET_FASHION = 4;

    /**
     * 获得坐骑(参数：坐骑配置ID)
     */
    public static final int SHARE_GET_HORSE = 5;

    /**
     * 获得翅膀(参数：翅膀配置ID)
     */
    public static final int SHARE_GET_WING = 6;

    /**
     * 众神遗迹(参数：BOSS配置ID)
     */
    public static final int SHARE_KILL_BOSS = 7;

    /**
     * 创世神殿(参数：地图配置ID)
     */
    public static final int SHARE_CLEARANCE = 8;

}
