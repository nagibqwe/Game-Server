package com.game.activity.cmd;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.ICommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @Desc TODO
 * @Date 2020/10/20 15:00
 * @Auth ZUncle
 */
public class CloneSuccessTrigger implements ICommand {

    final Logger logger = LogManager.getLogger(CloneSuccessTrigger.class);
    final Player player;
    final int cloneId;

    public CloneSuccessTrigger(Player player, int cloneId) {
        this.player = player;
        this.cloneId = cloneId;
    }

    /**
     * 执行命令.
     */
    @Override
    public void action() {
        try {
            Manager.activityManager.deal().cloneDrop(player, cloneId);
        } catch (Exception e) {
            logger.error(e);
        }
    }
}
