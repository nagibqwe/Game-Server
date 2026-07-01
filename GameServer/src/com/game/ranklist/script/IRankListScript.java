/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.ranklist.script;

import com.game.db.bean.RankPlayer;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerWorldInfo;
import com.game.ranklist.manager.RankListManager;
import game.core.script.IScript;
import game.message.RankListMessage;
import game.message.RankListMessage.ReqRankInfo;
import game.message.RankListMessage.ReqRankPlayerImageInfo;
import game.message.RankListMessage.ReqWorship;

import java.util.List;

/**
 * 排行榜的脚本接口
 */
public interface IRankListScript extends IScript {

    /**
     * 处理定时任务
     */
    void tick();

    /**
     * 上线红点消息
     */
    void online(Player player);

    /**
     * 请求排行榜数据
     */
    void OnReqRankInfo(Player player, ReqRankInfo messInfo);

    /**
     * 请求排行榜玩家镜像数据
     */
    void OnReqRankPlayerImageInfo(Player player, ReqRankPlayerImageInfo messInfo);

    /**
     * 请求崇拜
     */
    void OnReqWorship(Player player, ReqWorship messInfo);

    /**
     * 请求战力属性对比
     */
    void onReqCompareAttr(Player player, RankListMessage.ReqCompareAttr message);

    /**
     * 同步玩家的数据
     * @param rankType 类型
     */
    void OnSyncRankPlayer(RankPlayer rankPlayer, int rankType);

    /**
     * 刷新所有排行榜
     */
    void sortAllRank();

    /**
     * 刷新单个类型的排行榜
     * @param type 类型
     */
    void sortRank(int type);

    /**
     * 0点清理玩家的排行榜数据
     */
    void onZeroClearRank();

    /**
     * 删除角色处理
     * @param deleteRoleId 角色id
     */
    void deleteRankRole(long deleteRoleId);

    /**
     * 请求所有排行榜状态
     * @param player
     */
    void onReqGetAllRankListState(Player player);

    /**
     * 设置装备洗练等级
     */
    void changeEquipWashData(Player player);


    /**
     * 排行榜玩家同步
     */
    void syncRankPlayer(Player player);


    /**
     * 设置强化战力
     */
     void setStrengthrenPower(Player player, int power);


    /**
     * 设置坐骑战力、坐骑御魂等级、坐骑等级
     */
    void setHorseRankData(Player player, int power);


    /**
     * 设置宠物战力排行、宠物御魂等级排行、宠物等级排行
     */
     void setPetRankData(Player player, int power);


    /**
     * 、宠物御魂战力、
     */
     void setPetSoulPower(Player player, int power);


    /**
     * 、坐骑御魂战力、
     */
     void setHorseSoulPower(Player player,int power);

    /**
     * 灵体战力排行
     */
     void setSpiritPower(Player player, int power);


    /**
     * 仙甲战力排行
     */
     void setImmEuiqpPower(Player player, int power);


    /**
     * 圣装战力排行
     */
     void setHolyEuiqpPower(Player player, int power);


    /**
     * 神兽战力排行
     */
     void setMonstorPower(Player player, int power);


    /**
     * 设置翅膀战力
     */
    void setWingPower(Player player, int power);


    /**
     * 设置装备战力
     */
     void setEquipPower(Player player, int power);


    /**
     * 设置装备总星级
     */
     void updateEquipStarNum(Player player);

    /**
     * 设置装备总星级(包含灵体)
     */
    void updateEquipAllStar(Player player);


    /**
     * 设置装备强化等级
     */
     void changeEquipStrengthen(Player player);

    /**
     * 设置法器战力
     */
     void setTalismanPower(Player player, int power);

    /**
     * 设置阵法战力
     */
     void setMagicPower(Player player, int power);

    /**
     * 设置灵压法宝等级
     */
     void setMagicWeaponDamage(Player player, int damage);


    /**
     * 设置神兵战力
     */
     void setWeaponPower(Player player, int power);

    /**
     * 设置宝石总等级
     */
     void changeGemLv(Player player);

    /**
     * 设置宝石战力
     */
     void setGemPower(Player player, int power);

    /**
     * 设置石海层数
     */
     void setShihaiLayer(Player player, int layer);

    /**
     * 设置竞技场排名
     */
     void setArenaRank(Player player, int rank);


    /**
     * 增加金元宝消耗
     */
     void addConsumeGoldRank(Player player, int consumeAdd);


    /**
     * 魂甲战力排行
     * @param player
     * @param soulFight
     */
    void setSoulFightRank(Player player,int soulFight);


    /**
     * 八卦战力排行
     */
     void setBaguaPower(Player player, int power);

    /**
     * 灵魂战力排行
     */
     void setImmortalsoulPower(Player player, int power);


    /**
     * 魔魂战力排行
     */
     void setDevilSoulPower(Player player, int power);


    /**
     * 设置亲密度
     */
     void setIntimacy(Player player, int intimacy);


    /**
     * 坐骑脉轮战力排行
     */
     void setHorseEquipPower(Player player, int power);

    /**
     * 剑灵战力
     */
     void setFlySwordPower(Player player, int power);

    /**
     * 仙娃战力
     */
     void setMarryChildPower(Player player, int power);


    void  setFightPower(Player player, long fighting);

    /**
     * 设置玩家等级
     */
     void setLevel(Player player, int level);

    /**
     * 设置坐骑id
     */
     void setHorseId(Player player, int horseId);

    /**
     * 设置翅膀id
     */
     void setWingId(Player player, int wingId);

    /**
     * 设置名字
     */
     void setName(long id, String name);


    void initGuild(long guildId, PlayerWorldInfo pwi, String name);

    void buildRankInfo(int index, RankPlayer rankPlayer, boolean isOnline, String data, List<RankListMessage.RankInfo.Builder> rankInfoList, int needShowFightPower);


}
