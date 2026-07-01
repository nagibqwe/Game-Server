package com.game.skill.script;

import com.data.struct.ReadIntegerArrayEs;
import com.game.player.structs.Player;
import game.message.SkillMessage;

public interface ISkillScript {


    int getSkillAllLevel(Player player);

    /**
     * 添加被动技能
     * @param player
     * @param skillId
     */
    void addSkill(Player player, int skillId);

    /**
     * 移除技能
     * @param player
     * @param skillId
     */
    void removeSkill(Player player, int skillId);


    /**
     *上线初始化
     */
    void online(Player player);


    /**
     * 技能激活
     * @param player
     * @param skillIDArrayEs
     */
    void addActivateNewSkill(Player player,ReadIntegerArrayEs skillIDArrayEs);
    /**
     * 技能升星
     * @param player
     * @param skillID
     */
    void onReqUpSkillStar(Player player,int skillID);

    /**
     * 格子升级
     * @param player
     * @param cellId
     */
     void onResUpCell(Player player);


    /**
     * 获得技能总星级
     * @param player
     * @return
     */
     int getAllStar(Player player);

    /**
     * 获取所有等级
     * @param player
     * @return
     */
     int getAllLevel(Player player);


    /**
     *激活经脉
     * @param player
     */
     void onReqActivateMeridian(Player player,int meridianID);


    /**
     * 保存出站技能
     * @param player
     * @param playedSkillStr
     */
     void onReqSaveFightSkill(Player player,String playedSkillStr);


    /**
     * 重置经脉
     * @param player
     */
    void onReqResetMeridianSkill(Player player);


    /**
     * 选择心法
     * @param player
     * @param type
     */
    void onSelectMental(Player player,int type);

    /**
     * 重置心法
     * @param player
     */
    void  onReqResetSelectMental(Player player);


}
