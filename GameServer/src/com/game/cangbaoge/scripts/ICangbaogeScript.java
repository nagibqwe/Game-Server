package com.game.cangbaoge.scripts;


import com.game.player.structs.Player;
import game.core.script.IScript;

/**
 * Created by cxl on 2020/9/2.
 */
public interface ICangbaogeScript extends IScript {


    /**
     * 起服 加载数据
     */
    void loadData();

    /**
     * 周期检测
     */
    void tick();

    /**
     * 玩家上线
     * @param player
     */
    void playerOnline(Player player);

    /**
     * 打开藏宝阁
     * @param player
     */
     void ReqOpenCangbaogePanel(Player player);


    /**
     * 记录请求
     * @param player
     */
     void ReqOpenRecordPanel(Player player);


    /**
     * 抽检
     * @param player
     */
     void ReqCangbaogeLottery(Player player);


    /**
     * 领奖
     * @param player
     */
     void ReqCangbaogeReward(Player player,int id);

    /**
     * 兑换
     * @param player
     * @param exchangeId
     */
     void ReqCangbaogeExchange(Player player,int exchangeId);


    /**
     * 打开兑换界面
     * @param player
     */
    void ReqOpenCangbaogeExchange(Player player);


    /**
     * GM设置抽检次数
     * @param player
     */
    void gmSetLotteryTimes(Player player);


}
