package com.game.nature.script;

import com.game.nature.structs.NatureBase;
import com.game.player.structs.Player;
import game.core.script.IScript;

public interface INatureScript extends IScript {
    /**
     * 初始化玩家的造化中的某个功能
     * */
    void initPlayerAllNatureFunction(Player player);

    void initPlayerWing(Player player);

    /**
     * 初始化坐骑
     * */
    void initPlayerHorse(Player player);

    /**
     * 初始化灵压法宝
     * */
    void initStifleFabao(Player player);

    /**
     * 初始法宝御魂
     * */
    void initFabaoDrug(Player player);

    /**
     * 初始宠物御魂
     * */
    void initPetDrug(Player player);

    /**
     * 打开造化界面
     * */
    void onReqNatureInfo(Player player, int type);

    /**
     * 造化培养
     * */
    void onReqNatureDrug(Player player, int type, int itemId);

    /**
     * 设置显示模型
     * */
    void onReqNatureModelSet(Player player, int type, int modelId);

    /**
     * 使用物品升级造化
     * */
    void onReqNatureUpLevel(Player player, int type, int itemId, boolean isOneKeyUp);

    /**
     * 请求化形激活,升级
     * */
    void onReqNatureFashionUpLevel(Player player, int type, int id);

    /**
     * 化形激活，升级
     * @param auto 自动激活
     */
    void handleHuaxinStarUp(NatureBase base, Player player, int id, int type, boolean auto);

    /**
     * 初始化神兵
     * @param player 玩家
     */
    void initPlayerWeapon(Player player);
}
