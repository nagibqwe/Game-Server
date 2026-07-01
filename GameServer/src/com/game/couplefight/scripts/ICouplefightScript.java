package com.game.couplefight.scripts;

import com.game.player.structs.Player;
import game.core.script.IScript;
import game.message.CouplefightMessage;

import java.util.List;

public interface ICouplefightScript extends IScript {

    /**
     * 活动初始化
     */
    public void init();
    /**
     * 玩家上线
     * @param player
     */
    public void online(Player player);
    /**
     * 玩家报名
     * @param player
     */
    public void apply(Player player, String name);

    /**
     * 报名确认
     * @param player
     */
    public void applyConfirm(Player player, boolean confirm);

    /**
     * 开始匹配
     * @param player
     */
    void matchStart(Player player);

    /**
     * 停止匹配
     * @param player
     */
    void matchStop(Player player);

    /**匹配确认*/
    void matchConfirm(Player player, boolean confirm);

    /**
     * 战斗结果
     * @param messInfo
     */
    void p2GResFightResult(CouplefightMessage.P2GResFightResult messInfo);

    /**
     * 获取仙侣对决信息
     * @param player
     * @param type
     */
    void reqCouplefightInfo(Player player, int type, long... params);

    /**
     * 请求进入小组赛准备地图
     * @param p
     */
    void reqGroupPrepareMapEnter(Player p);

    /**
     * 玩家请求参与竞猜
     * @param player
     * @param messInfo
     */
    void reqChampionGuess(Player player, CouplefightMessage.ReqChampionGuess messInfo);

    /**
     * 玩家竞猜成功处理
     * @param rid
     */
    void p2GResChampionGuess(long rid);

    /**
     * 返回预选赛信息
     * @param messInfo
     */
    void p2GResTrialsInfo(CouplefightMessage.P2GResTrialsInfo messInfo);

    /**
     * 发送排行奖励
     * @param messInfo
     */
    void p2GResRankAward(CouplefightMessage.P2GResRankAward messInfo);

    /**
     * 请求进入战斗
     * @param messInfo
     */
    void reqGoToFight(CouplefightMessage.P2FReqGoToFight messInfo);

    /**
     * 竞猜结果 发放奖励
     * @param messInfo
     */
    void p2GResGuessResult(CouplefightMessage.P2GResGuessResult messInfo);

    /**
     * 向公共服同步玩家数据
     * @param player
     */
    public void updatePlayerInfo(Player player);

    /**
     * 玩家领取海选赛奖励
     * @param messInfo
     */
    void p2GGetTrialsAward(CouplefightMessage.P2GGetTrialsAward messInfo);

    /**
     * 活动状态改变
     * @param status
     */
    void p2GChangeStatus(int status);

    /**
     * 判断是否开启
     * @param funcId
     * @return
     */
    boolean funcIsOpen(int funcId);

    /**
     * 晋级消息
     * @param type
     * @param idList
     */
    void p2GPromotion(int type, List<Long> idList);

    /**
     * 发送预选赛奖励
     * @param awardList
     */
    void p2GTrialsAward(List<CouplefightMessage.TrialsAward> awardList);

    /**
     * 定时任务
     */
    void tick();

    /**
     * 请求进入冠军赛准备地图
     * @param player
     */
    void reqChampionEnter(Player player);

    /**
     * 玩家下线
     * @param player
     */
    void playerOffline(Player player);
}
