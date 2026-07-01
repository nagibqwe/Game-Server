package com.game.treasurehunt.script;

import com.data.bean.Cfg_Treasure_Pop_Bean;
import com.game.player.structs.Player;
import com.game.treasurehunt.struct.HuntReward;
import com.game.treasurehunt.struct.TreasureHuntRecord;
import game.core.script.IScript;

import java.util.Collection;
import java.util.List;

/**
 * Created by 瞿冰冰
 * 2019/7/9
 */
public interface ITreasureHuntScript extends IScript {

    /**
     * 抽奖
     * @param player
     * @param times
     * @param type
     */
    void treasureHunt(Player player, int times,int type);

    void treasureHuntBaseInfo(Player player);

    void onekeyExtract(Player player, int type);

    void buy(Player player, int type, int times, int messInfoTimes);

    /**
     * 获取玩家的免费次数
     *
     * @param type 寻宝类型
     * @param allCount 总的次数
     */
     int getLeftFreeTimes(Player player, int type, int allCount);

    /**
     * 暂时只设计一种类型 需要的时候这里需要跟着修改
     */
     int getLeftMustTypeTimes(Player player, int type, Cfg_Treasure_Pop_Bean pop_bean);

    int getTodayLeftTimes(Player player, int type);

    int getTodayUseTimes(Player player, int type);

    void writeTreasureHurtLog(Player player, Collection<TreasureHuntRecord> rewards, int times, int type);

    void writeBuyTimesLog(Player player, int type, int times, int beforeTimes, int afterTimes, int modelId);

    HuntReward createHuntReward(Player player, int mustType, int type,boolean isFree);

    void treasureHuntRecord(Player player, List<TreasureHuntRecord> records, int type);


    /**
     * 灵魄抽奖
     * @param player
     * @param times
     * @param isCopyMap
     */
    void treasureSoulHunt(Player player,int times,boolean isCopyMap);


    /**
     * 一键回收
     * @param player
     */
    void onReqOnekeyRecovery(Player player);


    /**
     * 选择部分回收
     * @param player
     * @param itemId
     * @param num
     */
    void onReqChooseRecovery(Player player,int itemId,int num);
}
