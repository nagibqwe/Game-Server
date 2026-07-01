package com.game.guild.structs;

/**
 * 公会系统的配置数据
 */
public class GuildSysConfig {
    /**
     * 会长
     */
    public static final int TYPE_MASTER = 4;
    /**
     * 副会长
     */
    public static final int TYPE_VICE_MASTER = 3;
    /**
     * 长老
     */
    public static final int TYPE_OFFICIAL = 2;
    /**
     * 成员
     */
    public static final int TYPE_MEMBER = 1;


    /**
     * 大厅
     */
    public static final int TYPE_BASE = 1;
    /**
     * 商店
     */
    public static final int TYPE_SHOP = 2;
    /**
     * 驻地
     */
    public static final int TYPE_HOME = 3;

    /**
     * 公会建筑个数
     */
    public final static int TYPE_ALL = 3;


    /**
     * 公会创建
     */
    public static byte BaseLog_type1 = 1;
    /**
     * 公会解散
     */
    public static byte BaseLog_type2 = 2;
    /**
     * 加入公会
     */
    public static byte BaseLog_type3 = 3;
    /**
     * 自己离开
     */
    public static byte BaseLog_type4 = 4;
    /**
     * 被离开
     */
    public static byte BaseLog_type5 = 5;
    /**
     * 公会设置
     */
    public static byte BaseLog_type6 = 6;
    /**
     * 公会改名
     */
    public static byte BaseLog_type7 = 7;
    /**
     * 开始弹劾会长
     */
    public static byte BaseLog_type8 = 8;
    /**
     * 取消弹劾会长
     */
    public static byte BaseLog_type9 = 9;

    /**
     * 弹劾会长成功
     */
    public static byte BaseLog_type10 = 10;


}
