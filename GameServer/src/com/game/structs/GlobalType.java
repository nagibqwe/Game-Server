package com.game.structs;

import com.data.Global;
import com.data.struct.ReadArray;
import com.game.utils.ServerParamUtil;

/**
 * 全局变量参数
 */
public class GlobalType {

    //设置输出收包协议的输出详细信息
    public static boolean isOutNetMess = false;
    //设置连接的具体调试信息
    public static boolean isOutMoreRead = false;
    //设置是否输出流量统计代码
    public static boolean isOutNetStreamInfo = false;


    public static float AttackSpeedOffset = 2000.0f; //攻击分母

    //切换地图cd
    public static final int ChangeMapCD = 3000;

    // 整理仓库和包裹的时间间隔(毫秒)
    public static int ClearUp_Ttime_Interval = 10 * 1000;

    /**
     * 服务器心跳参数字符串
     */
    public static String HEART_PARA = "c=%s&sid=%d&state=%d&tip=%s";

    /**
     * 服务器心跳地址字符串
     */
    public static String HEART_WEB = "";

    public static int ForceDivorceNeedGold = 500;//强制离婚所需元宝

    /**
     * 一天有多少秒
     */
    public static int SECOND_PER_DAY = 86400;
    /**
     * 一分钟有多少毫秒
     */
    public static long MILLIS_PER_MINUTE = 60000L;
    /**
     * 一个小时有多少毫秒
     */
    public static long MILLIS_PER_HOUR = 3600000L;
    /**
     * 一天有多少毫秒
     */
    public static long MILLIS_PER_DAY = 86400000L;

    private static volatile int worldLevel = 79;

    private final static Object worldLevelObj = new Object();

    public static int getSexByCareer(int career) {
        for (ReadArray<Integer> ss1: Global.JobSex.getValuees())
        {
            if (ss1.get(0) == career) {
                return ss1.get(1) ;
            }
        }
        return 0;
    }

    //巅峰转职要求
    public static int grade = 4;
    public static int gradeLevel = 370;


    public static boolean setWorldLevel(int level) {
        synchronized (worldLevelObj) {
            int oldlv = getWorldLevel();
            if (oldlv > level) {
                return false;
            }
            ServerParamUtil.worldLv = level;
            ServerParamUtil.saveWorldLv();
        }
        return true;
    }

    public static int getWorldLevel() {
        synchronized (worldLevelObj) {
            int level = ServerParamUtil.worldLv;
            if (level == 0) {
                level = worldLevel;
            }
            return level;
        }
    }
}
