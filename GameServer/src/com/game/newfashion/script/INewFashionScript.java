package com.game.newfashion.script;

import com.game.newfashion.structs.FashionData;
import com.game.player.structs.Player;
import game.core.script.IScript;
import game.message.NewFashionMessage;

import java.util.List;

/**
 * Created by cxl on 2020/3/23.
 */
public interface INewFashionScript extends IScript {


    /**
     * 序列号时装
     * @param fashionData
     * @return
     */
    NewFashionMessage.NewFashion buildFashionData(FashionData fashionData);

    /**
     * 上线初始化时装
     * @param player
     */
    void onlineInit(Player player);


    /**
     * 玩家激活时装
     * @param player
     * @param id
     */
    void ReqActiveFashion(Player player,int id);

    /**
     *玩家激活时装图鉴
     */
    void ReqActiveTj(Player player,int id);
    /**
     * 时装升星
     * @param player
     * @param id
     */
    void ReqFashionStar(Player player,int id);
    /**
     * 图鉴升星
     * @param player
     * @param id
     */
    void ReqTjStar(Player player,int id);

    /**
     * 穿戴时装
     * @param player
     * @param fashionList
     */
    void ReqSaveFashion(Player player,List<Integer> fashionList);

    /**
     * 激活时装
     * @param player
     * @param type
     * @param condition
     */
    void activeNewFashion(Player player,int type,int condition);

    /**
     * GM激活 时装 ID
     * @param player
     * @param id
     */
    void gmSetFashionID(Player player ,int id);
    /**
     * GM清除所有 时装
     * @param player
     */
    void clearAllFshionID(Player player);


    void addFashionID(Player player,int id);


    /**
     * 化形激活时装
     * @param player
     * @param type
     * @param huaxingID
     */
    void huaxingActivateFashion(Player player,int type , int huaxingID);


    /**
     * 化形穿戴时装
     * @param player
     * @param type
     * @param huaxingID
     */
     void huaxingWearFashion(Player player,int type , int huaxingID);


    /**
     * 获取头像
     * @param player
     */
     int getHead(Player player);
    /**
     * 获取头像框
     * @param player
     */
    int getHeadBox(Player player);

    /**
     * 获取气泡
     * @param player
     */
    int getQiPao(Player player);




}
