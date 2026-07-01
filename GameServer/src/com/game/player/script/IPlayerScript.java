package com.game.player.script;

import com.game.player.structs.Player;
import com.game.player.structs.ReliveType;
import com.game.structs.Fighter;
import game.core.map.Position;
import game.message.CrossServerMessage;

/**
 *
 * @author lw
 */
public interface IPlayerScript {

    /**
     * 转职
     * @param player
     */
    void changeJob(Player player);

    /**
     * 角色改名
     * @param player
     * @param newName
     */
    void changeName(Player player, String newName);

    /**
     * 玩家等级提升
     * @param player
     * @param oldLevel
     */
    void levelUp(Player player, int oldLevel);

    /**
     * 玩家复活
     * @param player
     * @param type
     * @param glod
     * @param pos
     */
    void OnPlayerRelive(Player player, ReliveType type, boolean glod, Position pos);

    /**
     * 玩家复活
     * @param handler
     */
    void G2FReliveHandler(CrossServerMessage.G2FRelive handler);

    /**
     * 跨服复活
     * @param messInfo
     */
    void F2GReliveResHandler(CrossServerMessage.F2GReliveRes messInfo);

    /**
     * 检查玩家是否到转职等级了
     * @param player
     */
    void checkUpLevelLimit(Player player);
    
    void sendPlayerFateStar(Player player);
    
    void sendPlayerChangeFateStar(Player player,int gender);
    
    void activeFateStar(Player player,int starId);

    /**
     * 升级血脉
     * @param player
     */
    void upgradeBlood(Player player);
    
    void openBloodPannel(Player player);

    /**
     * 请求设置自定义头像
     * @param player
     * @param customHeadPath
     * @param useCustomHead
     */
    void playerSettingCustomHead(Player player,String customHeadPath,boolean useCustomHead);

    /**
     * 回城复活
     * @param player
     */
    void doHomeRelive(Player player);
}
