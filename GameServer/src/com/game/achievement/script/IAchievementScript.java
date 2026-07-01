package com.game.achievement.script;

import com.game.player.structs.Player;
import game.core.script.IScript;

public interface IAchievementScript extends IScript {

    /**
     * 上线发送成就信息
     */
    void sendAchievementInfo(Player player);

    /**
     * 领取成就
     */
    void getAchievement(Player player, int id);

    /**
     * 成就进度检查
     */
    void checkAchievement(Player player, int type);

}
