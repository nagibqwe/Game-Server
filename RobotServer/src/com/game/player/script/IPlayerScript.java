package com.game.player.script;

import com.game.player.structs.Player;
import game.core.script.IScript;
import game.message.PlayerMessage;

public interface IPlayerScript extends IScript {

    void init();

    /**
     * 获取当前登录进游戏的角色数量
     */
    int getPlayerCount();

    Player getPlayerByRoleId(long roleId);

    void addPlayer(long roleId, Player p);

    void removePlayer(long roleId);

    /**
     * 随机机器人名字
     * @return
     */
    String getRandomName();

    String getRobotName(long beginId);

    /**
     * 进入游戏初始化角色信息
     * @param player
     * @param messInfo
     */
    void initPlayerBaseInfo(Player player, PlayerMessage.ResPlayerBaseInfo messInfo);

    /**
     * 事件心跳处理
     */
    void tickEvent();

    /**
     * 添加玩家
     * @param num
     */
    public void addPlayer(int num);

    /**
     * 获取一名未加入队伍的玩家
     * @return
     */
    Player getPlayerNoTeam();

    Player getPlayerNoTeam(int career);
}
