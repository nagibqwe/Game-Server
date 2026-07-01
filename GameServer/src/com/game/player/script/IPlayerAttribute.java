package com.game.player.script;

import com.game.attribute.BaseIntAttribute;
import com.game.attribute.BaseLongAttribute;
import com.game.attribute.BaseSystemIntAttribute;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerAttributeType;
import com.game.robot.struct.Robot;
import game.core.script.IScript;

/**
 * @Desc TODO
 * @Date 2020/8/12 19:58
 * @Auth ZUncle
 */
public interface IPlayerAttribute  extends IScript {

    /**
     * 初始化属性
     * @param player
     * @param sycRank
     */
    void initPlayerAttribute(Player player, boolean sycRank);


    /**
     * 发送玩家属性到客户端
     * @param player
     */
    void sendAttributeValueToPlayer(Player player);

    /**
     * 攻速改变
     * @param player
     */
    void sendAttackSpeedMessge(Player player);

    /**
     * 广播移速变化
     * @param player
     */
    void sendMoveSpeedMessage(Player player);

    /**
     * 写属性日志
     * @param player
     */
    void writeAttlog(Player player);

    /**
     * 机器人
     * @param robot
     * @param petFightPower
     */
    void CountBaseRobot(Robot robot, long petFightPower);

    /**
     * 计算战力
     * @param att
     * @return
     */
    int calcFightPower(BaseIntAttribute att);
    /**
     * 计算战力
     * @param att
     * @return
     */
    long calcFightPower(BaseLongAttribute att);

    /**
     * 计算玩家属性
     * @param player
     * @param types
     */
    void calcAttribute(Player player, PlayerAttributeType... types);

    /**
     * 计算总属性
     * @param player
     * @param fightBaseAttribute
     * @param ext
     */
    void sumAttribute(Player player, BaseLongAttribute fightBaseAttribute, PlayerAttributeType... ext);

    /**
     * 获取单个系统属性
     * @param player
     * @param type
     * @return
     */
    BaseIntAttribute getAttribute(Player player, PlayerAttributeType type);

    BaseSystemIntAttribute getSystemAttribute(Player player, PlayerAttributeType type);


}
