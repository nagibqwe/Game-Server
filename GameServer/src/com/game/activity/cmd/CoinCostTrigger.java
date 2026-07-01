package com.game.activity.cmd;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.ICommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @Desc TODO
 * @Date 2020/10/21 11:01
 * @Auth ZUncle
 */
public class CoinCostTrigger implements ICommand {

    final Logger logger = LogManager.getLogger(CoinCostTrigger.class);
    final Player player;
    final int type;
    final int value;

    public CoinCostTrigger(Player player, int type, int value) {
        this.player = player;
        this.type = type;
        this.value = value;
    }

    /**
     * 执行命令.
     */
    @Override
    public void action() {
        try {
            Manager.activityManager.deal().consumeDeal(player, type, value);
        } catch (Exception e) {
            logger.error(e);
        }
    }
}
