package com.game.boss.script;

import com.data.struct.ReadArray;
import com.data.struct.ReadIntegerArrayEs;
import com.game.boss.struct.Boss;
import com.game.dailyactive.structs.DailyActiveDefine;
import com.game.map.structs.MapObject;
import com.game.monster.structs.Monster;
import com.game.player.structs.Player;
import game.message.BossMessage;

import java.util.Collection;

/**
 * 世界boss接口
 */
public interface IWorldBossScript extends IBossCoin{
    /**
     * gm命令重新加载世界BOSS的缓存，根据配置表来，不重置复活时间
     */
    void reloadSpecialMonsterConfig();

    /**
     * 启服加载世界boss和boss之家数据
     */
    void loadSpecialMonsterConfig();

    /**
     * 加载个人世界boss数据
     */
    void loadPersonalWorldBoss(Player player);

    /**
     * 计算个人世界boss的复活时间
     */
    void calcPersonWorldBossBirth(Player player);

    /**
     * 检查Boss出生
     */
    void calcBossBirth(int type);

    /**
     * 添加boss击杀记录数据
     */
    void addBossKilledRecord(MapObject map, Monster monster, Player killer);

    /**
     * 0点计算幻境boss重生基数时间(按开服天数提升一定时间)
     */
    void calcBossRebornBaseTime();

    /**
     * 刷新boss
     * @param boss
     */
    void calcRefreshTime(Boss boss);

    /**
     * 世界boss刷新或死亡后的同步
     * @param type boss类型
     */
    void syncWorldBossInfo(MapObject mapObject, int bossId, int type);

    /**
     * 世界boss刷新规则根据开服天数获取对应的刷新时间上下限
     * @param openDay 开服时间
     */
    ReadArray<Integer> getLimitTime(int openDay, ReadIntegerArrayEs arrlist);

    /**
     * 通过boss的类型来获取掉落类型
     * @param type boss类型
     */
    int getSpecialType(int type);

    /**
     * 购买排名奖励次数
     */
    void buyRankCount(Player player, int type);

    /**
     * 处理日常次数增加
     * @param player
     * @param dailyId
     */
    void dealBossDailyCountAdd(Player player, int dailyId);

    /**
     * 同步掉落次数
     * @param type boss类型
     */
    void synDropDataFromFightToGame(Player player, int type);


    /**
     * 请求打开世界boss界面
     */
    void reqOpenDreamBoss(Player player, BossMessage.ReqOpenDreamBoss messInfo);

    /**
     * 请求boss击杀记录信息
     */
    void reqBossKilledInfo(Player player, int bossId, int bossType);

    /**
     * 请求关注boss
     * @param type 1：关注， 2：取消关注
     * @param bossType boss类型
     */
    void reqFollowBoss(Player player, int bossId, int type, int bossType);

    /**
     * 升级自动关注boss
     * 玩家升级自动关注离自己等级最近且小于自身等级的世界boss
     */
    void autoFollowBoss(Player player);

    /**
     *
     * @param bossId
     * @param type
     */
    void sendBossRefreshTip(int bossId, int type);

    /**
     * 同步boss掉落归属排名
     */
    void syncBossDamageRank(Monster monster);

    /**
     * 发送boss列表
     * @param player
     * @param bossList
     * @param cloneId
     * @param type
     */
    void sendBossInfo(Player player, Collection<Boss> bossList, int cloneId, int type);

    void sendBossInfo(Collection<Player> players, Collection<Boss> bossList, int cloneId, int type);


    /**
     * 通知boss 刷新
     * @param player
     * @param bossId
     * @param type
     */
    void sendBossRefresh(Player player, int bossId, int type);

    /**
     * 发送首领 daily 次数
     * @param player
     * @param daily
     */
    void sendDailyCount(Player player, DailyActiveDefine daily);

    /**
     * 获取新手层 boss 多人副本
     * @param modelId
     * @return
     */
    MapObject getNoodBoss(int modelId);

}
