package com.game.guildactivity.script;

import com.game.map.script.IMapBaseScript;
import com.game.map.structs.MapObject;
import com.game.player.structs.Player;
import game.core.map.Position;

/**
 * @Desc TODO
 * @Date 2021/1/20 17:22
 * @Auth ZUncle
 */
public interface IGuildLastBattle extends IMapBaseScript {

    /**
     *  进入副本
     * @param player
     */
    void enterMap(Player player);

    /**
     * 结束活动
     */
    void endActive();

    /**
     * 获取个人连胜击杀
     * @param player
     */
    void reqGuildLastBattleRoleKill(Player player);

    /**
     * 活动开启邮件
     */
    void notifyMail();

}
