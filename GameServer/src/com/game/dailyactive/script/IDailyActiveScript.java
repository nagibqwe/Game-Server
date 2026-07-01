package com.game.dailyactive.script;

import com.data.bean.Cfg_Daily_Bean;
import com.game.dailyactive.structs.DailyActiveDefine;
import com.game.player.structs.Player;
import game.core.script.IScript;
import game.message.DailyactiveMessage;

public interface IDailyActiveScript extends IScript {

    /**
     * 日常重置
     * @param player
     */
    void checkDailyReset(Player player);

    /**
     * 活动开启
     */
    void activeBegin(Cfg_Daily_Bean bean);

    /**
     * 活动结束
     */
    void activeEnd(Cfg_Daily_Bean bean);

    /**
     * 日常心跳检查
     */
    void timerCheck(long now, long lastCheckTime);

    /**
     * 发送日常面板信息
     */
    void sendDailyActivePanelInfo(Player player);

    /**
     * 增加日常进度
     */
    void addDailyProgress(Player player, DailyActiveDefine dailyId, int num);

    /**
     * 增加活跃次数
     * @param player
     * @param dailyId
     * @param num
     */
    void addCountByItem(Player player, int dailyId, int num);

    /**
     * 请求领取活跃奖励
     */
    void OnReqGetActiveReward(Player player, DailyactiveMessage.ReqGetActiveReward messInfo);

    /**
     * 检查是否能参加活动
     */
    boolean checkCanEnterActive(Player player, Cfg_Daily_Bean bean);

    /**
     * 参加日常活动
     */
    void joinDailyActive(Player player, int dailyId, int param, boolean isGm);

    /**
     * 请求/设置推送列表
     */
    void onReqDailyPushIds(Player player, DailyactiveMessage.ReqDailyPushIds mess);

    /**
     * 进入日常活动地图日志
     */
    void writeEnterDailyActivityLog(int dailyId,int modelId,long roleId,int level);

    /**
     * 公共服通知战斗服活动状态
     */
    void P2FSendDailyState(DailyactiveMessage.P2FSendDailyState  messInfo);

    /**
     * 活动期间获取活动结束时间
     * @return 结束时间戳，未在活动期间返回0
     */
    long getDailyNearlyEndTime(int dailyId);

    /**
     * 获取日常活动玩家可购买次数
     */
    int getDailyCanBuyCount(Player player, int dailyId);

    /**
     * 获取日常活动购买所需元宝数
     * @param buyCount 购买第几次
     */
    int getDailyBuyNeedGold(int dailyId, int buyCount);

    /**
     * 获取日常剩余次数
     */
    int getDailyRemainCount(Player player, int dailyId);

    /**
     * 获取日常最大次数
     */
    int getDailyMaxCount(Player player, int dailyId);

    /**
     * 活动是否开启
     * @param dailyId
     * @return
     */
    boolean isOpen(int dailyId);

    /**
     * 是否有公会活动
     * @return
     */
    boolean isInGuildDaily();

    /**
     * 检查是否在日常boss地图
     */
    boolean checkInBossMap(Player player, int dailyId);


    int getVipPowerTypeByDailyId(int dailyId);


}
