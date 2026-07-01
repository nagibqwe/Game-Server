/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.robot.script;

import com.game.pet.structs.Pet;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerAttributeType;
import com.game.robot.struct.Robot;

import java.util.List;

/**
 *
 * @author zenghai
 */
public interface IRobotScript {

    //中国制造
    public Robot OnMake(Player player);

    public Robot OnMake(long roleId);

    public Robot OnMakeByJJCConfig(int configId);

    public Robot OnMakeByRobotConfig(int configId);

    /**
     * 随机一个机器人
     * @param player 玩家
     * @param attributePercent 能力百分比
     * @param rename 是否随机名字
     * @return
     */
    public Robot OnMake(Player player,float attributePercent, boolean rename);
    //心跳
    public void tick(Robot robot);

    //计算机器人的属性
    public void OnCalcBuffAttribute(Robot robot, Pet pet, PlayerAttributeType type);


    /**
     * 召唤机器人助战
     * @param player
     * @param roleIDs
     * @param liveTime
     * @param attributePercent
     * @param buffId
     */
    void OnRobotHelpBattle(Player player, List<Long> roleIDs,Long liveTime,float attributePercent, int buffId);
}
