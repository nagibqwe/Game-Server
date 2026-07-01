package com.game.crazyweek.script;

import com.game.player.structs.Player;
import game.core.script.IRunScript;
import game.core.script.IScript;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 运营活动接口类
 */
public interface ICrazyWeekScript extends IRunScript {
    
    /**
     * 充值后的处理
     * @param player
     */
    void rechargeDeal(Player player, int rechargeNum);

    /**
     * 数据初始化
     */
    void initializeData();

    /**
     * 0点处理活动数据
     */
    void endActivity();

    /**
     * 5点处理活动数据
     */
    void beginActivity();

    /**
     * 功能是否开启,提供给ContorlScript使用
     * @param funcID
     */
    boolean funcIsOpen(int funcID);
}

