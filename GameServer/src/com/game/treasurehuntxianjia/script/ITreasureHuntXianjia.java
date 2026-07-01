package com.game.treasurehuntxianjia.script;

import com.game.player.structs.Player;
import game.core.script.IScript;

/**
 * Created by 542 on 2020/2/25.
 */
public interface ITreasureHuntXianjia extends IScript {



    void  loadData();
    /**
     * 周期检测
     */
    void treasureTick();

    /**
     * 上线初始化
     * @param player
     */
    void onLineInit(Player player);

    /**
     * 打开仙甲界面
     * @param player
     * @param type
     */
    void onReqOpenXianjiaHuntPanel(Player player,int type);

    /**
     * 仙甲寻宝
     * @param player
     * @param type
     * @param times
     */
    void onReqTreasureHuntXijia(Player player,int type,int times);

    /**
     * 购买钥匙
     * @param player
     * @param type
     * @param times
     * @param
     */
    void onReqBuyCount(Player player,int type,int num,int times);

    /**
     * 秘宝
     * @param player
     * @param type
     * @param id
     */
    void onReqTreasureHuntMibao(Player player  ,int type,int id);

    /**
     * 仓库提取
     * @param player
     * @param type
     * @param uid
     */
    void onReqExtract(Player player,int type,long uid);


    /**
     * 清数据
     * @param player
     */
    void gmClearXianjiaData(Player player);


    void gmSetMibao(Player player);

}
