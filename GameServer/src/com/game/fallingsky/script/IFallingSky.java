package com.game.fallingsky.script;

import com.game.player.structs.Player;
import game.core.script.IScript;

/**
 * Created by cxl on 2020/11/9.
 */
public interface IFallingSky  extends IScript {


    /**
     * 数据加载
     */
    void  loadData();

    /**
     * 上线初始化
     * @param player
     */
    void online(Player player);


    /**
     * 计时器
     */
    void tick();


    /**
     * 进度刷新
     * @param player
     */
    void onRefreshUpProgress(Player player,int type,int num);


    /**
     * 领取等级奖励
     */
    void onReqGetFallSkyLevelReward(Player player,int levelID,boolean isFree);

    /**
     * 领取任务奖励
     * @param player
     * @param taskID
     */
    void onReqGetFallSkyTaskReward(Player player,int taskID);


    /**
     * 充值回调
     * @param player
     * @param goodsID
     */
    void onResRefreshRechargeState(Player player,int goodsID);

    /**
     * 每日刷新
     * @param player
     */
    void onDailyRefreshTask(Player player);


    /**
     * 一键领取等级奖励
     * @param player
     */
    void onReqOnekeyGetFallSkyLevelReward(Player player);


    /**
     * 一键领取任务奖励
     * @param player
     */
    void onReqOnekeyGetFallSkyTaskReward(Player player);


    /**
     * 重设开服时间后，天禁令默认改为第一轮
     */
    void onOpenTimeChange();


}
