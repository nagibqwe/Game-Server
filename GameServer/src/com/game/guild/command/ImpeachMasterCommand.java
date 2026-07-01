package com.game.guild.command;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.ICommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @Description
 * @auther lw
 * @create 2020-1-15 9:32
 */
public class ImpeachMasterCommand implements ICommand {
    private static final Logger logger = LogManager.getLogger(ImpeachMasterCommand.class);

    private final Player player;

    public ImpeachMasterCommand(Player player) {
        this.player = player;
    }

    @Override
    public void action() {
        try {
            Manager.guildsManager.manager().reqImpeachMaster(player);
        } catch (Exception e) {
            logger.error(e, e);
        }
    }
}
