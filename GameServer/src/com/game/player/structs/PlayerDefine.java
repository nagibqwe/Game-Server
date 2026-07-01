package com.game.player.structs;

public class PlayerDefine {

    public static final String[] CAREER_DESC = {"玄剑", "天英"};
    public static final int CAREER_All = 9;//所有职业通用

    public static final int SEX_WOMAN = 0;//性别女
    public static final int SEX_MAN = 1;//性别男
    public static final int SEX_ALL = 2;//表示通用==人妖

    /**
     * 和平模式
     */
    public static final int PkStatePeace = 0;
    /**
     * 全体模式(队伍)
     */
    public static final int PkStateTeam = 1;
    /**
     * 服务器模式
     */
    public static final int PkStateServer = 2;
    /**
     * 战场模式
     */
    public static final int PkStateCamp = 3;

    /**
     * 公会模式
     */
    public static final int PkStateGuild = 4;
    /**
     *最大PK模式
     */
    public static final int PkStateMax = 5;



    //pk相关
    //1、杀一个白名玩家 增加 100点
    //2、60秒清除 10点
    public static final int PkAddValue = 100;
    public static final int PkDecValue = 10;
    public static final int PkRedName = 200;
    public static final int PkGoodEvil = 1000;//善恶值达到一千
    public static final int PkCleanTime = 60000; //60秒清除10点
    public static final int PlayerHatredTime = 10000; //60秒检测自卫列表
    public static final int PkValueMax = 1000; //pk值上限


}
