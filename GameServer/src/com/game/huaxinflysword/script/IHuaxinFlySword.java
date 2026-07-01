package com.game.huaxinflysword.script;

import com.game.player.structs.Player;

/**
 * Created by cxl on 2020/5/21.
 */
public interface IHuaxinFlySword {

    /**
     * 上线初始化
     * @param player
     */
    void onlineInit(Player player);


    /**
     * 化形使用
     * @param player
     * @param id
     */
    void onReqUseHuxin(Player player,int id,int type,boolean isClientActive);


    /**
     * FlyswardActivated;激活剑灵X，激活剑灵：ID_进度(对应HuaxingFlySword表中的ID）
     */
    int getFlySwordActivateState(Player player,int id);


    /**
     * 新增变量，主线任务使用，取HuaxingFlySword_levelup表中的等级
     * @param player
     * @param type
     * @return
     */
    int getFlySwordLevel(Player player,int type);


    /**
     * 上线初始化剑灵技能
     * @param player
     */
    void  onlieInitflySwordSkill(Player player);


}
