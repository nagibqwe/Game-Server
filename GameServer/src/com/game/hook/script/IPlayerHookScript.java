/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.hook.script;

import com.game.map.structs.MapObject;
import com.game.player.structs.Player;
import game.core.script.IScript;

/**
 * 挂机功能
 * @author gsj
 */
public interface IPlayerHookScript extends IScript {

    /**
     * 请求挂机设置信息
     */
    void onReqHookSetInfoHandler(Player player);

    /**
     * 开始打坐
     */
    void startSitDownHandler(Player player);

    /**
     * 请求结束打坐
     */
    void endSitDownHandler(Player player);

    /**
     * 结束打坐处理
     */
    void afterEndSitDown(Player player);

    /**
     * 开始离线挂机
     */
    void enterOfflineHook(Player player);

    /**
     * 停止离线挂机
     */
    void stopOfflineHook(Player player);

    /**
     * 发送地图玩家打坐收益，每10秒发送一次
     */
    void sitDownEarning(Player player);

    /**
     * 特殊地图每秒给玩家经验加成
     * 等级经验*（1 + 战力放大倍率）*（1 + 药剂倍率+ VIP倍率 + 世界等级倍率 + 其他倍率）
     */
    void sendSpecialMapPlayerExpUp(MapObject map);

    /**
     * 获取挂机效率
     * 为排行榜数据提供接口
     */
    long getHookRate(long playerId);

    /**
     * 获取药剂经验加成
     */
    int getCurrentItemRate(Player player);

    /**
     * 定时扣除在线玩家经验倍率时间接口
     */
    void decExpItemRateTime(Player player);

    /**
     * 世界等级变化处理
     */
    void worldLvChange();

    /**
     * 检查玩家经验倍率变化
     */
    void checkPlayerRateChange(Player player, int type);


    /**
     * 上线发送挂机找回时间
     * @param player
     */
    void onlineSendHookFindTime(Player player);

    /**
     * 请求找回
     * @param player
     * @param isfind
     */
    void onReqOfflineHookFindTime(Player player,boolean isfind);

}
