package com.game.dailyactive.scripts;

import com.data.bean.Cfg_Daily_Bean;
import game.core.script.IScript;

public interface IDailyActiveScript extends IScript {


    boolean isActiveOpen(int dailyId);

    /**
     * 每分钟的心跳处理
     *
     * @param nowTime
     * @param lastTime 上次心跳检查时间
     */
    void timerTicker(long nowTime, long lastTime);

    /**
     * 活动开启
     * @param bean
     * @param index 开放时间下标索引，对应cloneID字段下标
     */
    void activeBegin(Cfg_Daily_Bean bean, int index);

    /**
     * 活动结束
     * @param bean
     */
    void activeEnd(Cfg_Daily_Bean bean);

    /**
     * 活动期间获取活动结束时间
     * @return 结束时间戳，未在活动期间返回0
     */
    long getDailyNearlyEndTime(int dailyId);
}
