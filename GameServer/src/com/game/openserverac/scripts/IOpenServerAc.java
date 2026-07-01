package com.game.openserverac.scripts;

import com.game.player.structs.Player;
import game.core.script.IScript;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public interface IOpenServerAc extends IScript {
    /**
     * 初始化开服活动
     * @param player
     */
    void initRevel(Player player);

    /**
     * 排行榜结算
     * @param rankScriptID  排行榜脚本ID
     * @param roleIds 排行榜角色
     */
    void onOperateBalance(int rankScriptID, ConcurrentHashMap<Integer, Long> roleIds);

    /**
     * 增加消耗
     * @param player
     * @param num
     */
    void onOperateRechargeGold(Player player, int num);

    /**
     * 请求个人领奖开服活动
     * @param player
     * @param id
     */
    void onReqPersonReward(Player player, int id);



    /**
     * 初始化成长之路
     * @param player
     */
    void initGrowUp(Player player);

    /**
     * 领取成长之路积分
     * @param player
     * @param id
     */
    void onReqGrowUpPoint(Player player, int id);

    /**
     * 领取成长之路奖励
     * @param player
     * @param id
     */
    void onReqGrowUpReward(Player player, int id);

    /**
     * 成长之路道具购买(注意只能购买一次，不然每次都会优惠购买)
     * @param player
     */
    void onReqGrowUpPur(Player player);

    /**
     * 推送成长之路进度
     */
    void onReqGrowUpProgress(Player player, int type);

    /**
     * 推送下一天成长之路
     */
    void onReqGrowUpNextProgress(Player player);




    /**
     * 初始化开服活动红点
     * @param player
     */
    void initOpenServerSpec(Player player);

    /**
     * 推送红点提示
     * @param player
     * @param type
     */
    void onReqOpenServerSpecProgress(Player player, int type);

    /**
     * 请求开服活动列表
     * @param player
     */
    void onReqOpenServerSpecAc(Player player);

    /**
     * 请求领奖
     * @param player
     * @param id
     */
    void onReqOpenServerSpecReward(Player player, int id);

    /**
     * 请求领取红包
     * @param player
     */
    void onReqOpenServerSpecRed(Player player);

    /**
     * 请求兑换
     * @param player
     * @param type
     */
    void onReqOpenServerSpeceExchange(Player player, int type);

    /**
     * 消耗存钱
     * @param player
     * @param num
     */
    void onReqOpenServerSpecSaveRed(Player player, int num);

    /**
     * 领取开服预告每日免费奖励
     */
    void onReqFreeDailyReward(Player player);

    void loginFreeDailyReward(Player player);

    void resetFreeDailyReward(Player player);

    /**
     * 请求打开首杀boss面板
     */
    void onReqFirstKillPanel(Player player, boolean online);

    /**
     * 请求领取击杀首杀boss奖励
     */
    void onReqGetKillReward(Player player, int id);

    /**
     * 请求领取红包
     */
    void onReqHongBaoReward(Player player, int id);

    /**
     * 伤害最高击杀boss
     */
    void onKillMonster(int modelId, List<Long> roleId, List<String> name);

    /**
     * 检查首杀活动结束未领取奖励
     */
    void checkTimeOver(Player player);

    /**
     * 检查新服活动的完成情况
     */
    void checkCompleteActTask(Player player);

    /**
     * 检查新服活动是否结束
     */
    void checkActEnd(long lastCheckTime, long now);

    /**
     * 新服活动面板
     */
    void sendNewServerActInfo(Player player);

    /**
     * 领取新服活动奖励
     *
     * @param type  活动类型
     * @param id    配置表id
     */
    void onReqGetActReward(Player player, int type, int id);

    /**
     * 玩家上线，推送数据
     * */
    void playerOnline(Player player);

    /**
     * 领取幸运翻牌幸运值
     * */
    void getLuckyCardWish(Player player, int taskId);

    /**
     * 幸运翻牌活动翻牌一次
     * */
    void luckyOnce(Player player, int cellId);

    /**
     * 完成任务条件后，幸运翻牌活动任务的分子增加
     * */
    void onAddluckyTaskFenzi(Player player);

    /**
     * 查看幸运翻牌记录
     * */
    void getLuckyHistroy(Player player);

    /**
     * 消耗金元宝，增加幸运值
     * */
    void onCostGold(Player player, int cost);


    /**
     * 广播排名第一的玩家信息
     */
    void onNoticeFirstRank(int rankScriptID, ConcurrentHashMap<Integer, Long> roleIds);

    /**
     * 玩家领取返利宝箱
     * @param player
     * @param day
     */
    void getRebateBox(Player player, int day);

    /**
     * 0点处理
     * @param player
     */
    void zeroClockDeal(Player player);

    /**
     * 刷新仙盟争霸任务
     * @param player
     * @param type
     * @param subType
     * @param changeNum
     */
    void onRefreshGuildBattle(Player player, int changeNum, int... types);

    /**
     * 请求获取仙盟争霸奖励
     * @param player
     * @param id
     */
    void onReqXmzbReward(Player player, int id);
}
