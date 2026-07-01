package com.game.home.script;

import com.game.player.structs.GlobalPlayerWorldInfo;
import game.core.script.IScript;
import game.message.HomeMessage;

/**
 * @Desc TODO
 * @Date 2021/7/12 18:05
 * @Auth ZUncle
 */
public interface IHomeScript extends IScript {


    /**
     * 初始化家装大赛排名
     */
    void initRank();

    /**
     * 检测家装大赛结束
     */
    void checkRankOver();

    /**
     * 家装大赛排序方法
     * @return
     */
    int compareTo(GlobalPlayerWorldInfo p1, GlobalPlayerWorldInfo p2);

    /**
     * 加入家装大赛
     * @param player
     */
    boolean joinDecorateMatch(GlobalPlayerWorldInfo player);

    /**
     * 统计总装饰度
     * @param player
     */
    void doCalcHouseDecorate(GlobalPlayerWorldInfo player);

    /**
     * 刷新家装大赛
     */
    void doHouseDecorateRank();


    /** 设置房屋访问权限
     * @param player
     * @param messInfo
     */
    void G2SAuthHomePem(GlobalPlayerWorldInfo player, HomeMessage.G2SAuthHomePem messInfo);

    /**
     *请求进入房屋
     * @param player
     * @param messInfo
     */
    void G2SEnterHome(GlobalPlayerWorldInfo player, HomeMessage.G2SEnterHome messInfo);

    /**
     *请求房屋信息
     * @param player
     * @param messInfo
     * @param isMapInfoSyn
     */
    void G2SHomeInfo(GlobalPlayerWorldInfo player, HomeMessage.G2SHomeInfo messInfo, boolean isMapInfoSyn);

    /**
     *获取家装大赛投票数据
     * @param player
     * @param messInfo
     */
    void G2SHomeTrimMatchScore(GlobalPlayerWorldInfo player, HomeMessage.G2SHomeTrimMatchScore messInfo);

    /**
     *获取家装大赛排名
     * @param player
     * @param messInfo
     */
    void G2SHomeTrimRank(GlobalPlayerWorldInfo player, HomeMessage.G2SHomeTrimRank messInfo);

    /**
     *投票家园
     * @param player
     * @param messInfo
     */
    void G2SHomeTrimVote(GlobalPlayerWorldInfo player, HomeMessage.G2SHomeTrimVote messInfo);

    /**
     *获取访客送礼清单
     * @param player
     * @param messInfo
     */
    void G2SHomeVisitorGiftList(GlobalPlayerWorldInfo player, HomeMessage.G2SHomeVisitorGiftList messInfo);

    /**
     *获取访客记录
     * @param player
     * @param messInfo
     */
    void G2SHomeVisitorNote(GlobalPlayerWorldInfo player, HomeMessage.G2SHomeVisitorNote messInfo);

    /**
     *获取投票对象
     * @param player
     * @param messInfo
     */
    void G2SRandomHomeTrimTarget(GlobalPlayerWorldInfo player, HomeMessage.G2SRandomHomeTrimTarget messInfo);

    /**
     *获取访客送礼
     * @param player
     * @param messInfo
     */
    void G2SSendVisitorGift(GlobalPlayerWorldInfo player, HomeMessage.G2SSendVisitorGift messInfo);

    /**
     * 房屋装饰
     * @param player
     * @param messInfo
     */
    void G2SHomeDecorate(GlobalPlayerWorldInfo player, HomeMessage.G2SHomeDecorate messInfo);

    /**
     * 房屋升级
     * @param player
     * @param messInfo
     */
    void G2SHomeLevelUp(GlobalPlayerWorldInfo player, HomeMessage.G2SHomeLevelUp messInfo);

    /**
     * 家园场景同步玩家数据到家园
     * @param messInfo
     */
    void F2SHomePlayerInfo(HomeMessage.F2SHomePlayerInfo messInfo);

    /**
     * 添加家具
     * @param player
     * @param modelId
     * @param count
     * @param changeReason
     */
    void addFurniture(GlobalPlayerWorldInfo player, int modelId, int count, int changeReason);

    /**
     * 更新玩家数据
     * @param player
     * @param messInfo
     */
    void G2SUpdatePlayerInfo(GlobalPlayerWorldInfo player, HomeMessage.G2SUpdatePlayerInfo messInfo);
}
