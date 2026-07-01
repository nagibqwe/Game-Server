package com.game.couplefight.script;

import com.game.couplefight.structs.CoupleData;
import com.game.couplefight.structs.CoupleFightRoom;
import com.game.couplefight.structs.CoupleTeam;
import game.core.script.IScript;
import game.message.CouplefightMessage;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

/**
 * @Auther: gouzhongliang
 * @Date: 2021/7/1 10:57
 */
public interface ICouplefightScript extends IScript {

    /**
     * 报名
     * @param context
     * @param data
     * @param messInfo
     */
    void apply(ChannelHandlerContext context, CouplefightMessage.G2PReqApply messInfo);

    /**
     * 开始匹配
     * @param context
     * @param mId
     * @param wId
     * @param captain 队长
     */
    void matchStart(ChannelHandlerContext context, long mId, long mpower, long wId, long wpower, long captain);

    /**
     * 刷新活动状态
     */
    void refresh();

    /**
     * 状态改变
     * @param status
     * @return
     */
    public int statusChange(int status);

    /**
     * 小组赛下一轮
     */
    public void onGroupsNextRound();

    /**
     * 海选赛匹配
     */
    void match();

    /**
     * 定时执行
     * @param now
     */
    void OnTick(long now);

    /**
     * 活动开始
     */
    void start();

    /**
     * 活动关闭
     */
    void close();

    /**
     * 匹配确认
     * @param context
     * @param uid
     * @param confirm
     */
    void matchConfirm(ChannelHandlerContext context, long uid, boolean confirm);


    void matchConfirmOver(CoupleData data, CoupleFightRoom room, CoupleTeam t, CoupleTeam o);

    /**
     * 结束匹配
     * @param context
     * @param uid
     */
    void matchStop(ChannelHandlerContext context, long uid);

    /**
     * 海选战斗结果
     * @param type 1海选赛 2小组赛 3冠军赛
     * @param fid 房间id
     * @param winList
     * @param loseList
     */
    void fightResult(int type, long fid, List<Long> winList, List<Long> loseList);

    /**
     * 玩家获取信息
     * @param typeList
     * @param rid
     */
    void reqInfo(ChannelHandlerContext context, int type, long rid, List<Long> params);

    /**小组赛队伍
     * 初始化
     * @param coupleData
     * @param teams
     */
    void initGroupInfo(CoupleData coupleData, List<CoupleTeam> teams);

    /**
     * 请求进入小组赛准备地图
     * @param context
     * @param rid
     */
    void reqGroupPrepareMapEnter(ChannelHandlerContext context, long rid);

    /**
     * 初始化冠军赛信息
     * @param coupleData
     */
    void initChampionInfo(CoupleData coupleData, List<CoupleTeam> dis, List<CoupleTeam> tians);

    /**
     * 请求进行竞猜
     * @param context
     * @param messInfo
     */
    void reqChampionGuess(ChannelHandlerContext context, CouplefightMessage.G2PReqChampionGuess messInfo);

    /**
     * 冠军赛下一轮
     */
    public void onChampionNextRound();

    /**
     * 获取当前的活动ID
     * @return
     */
    int getActivityId();

    /**
     * 更新玩家数据
     * @param context
     * @param messInfo
     */
    void updatePlayerInfo(ChannelHandlerContext context, CouplefightMessage.G2PSendPlayerInfo messInfo);

    /**
     * 初始化
     */
    void doInit();

    /**
     * 发送状态信息给游戏服
     * @param context
     */
    void sendStatus(ChannelHandlerContext context);

    /**
     * 停止
     */
    void stop();
}
