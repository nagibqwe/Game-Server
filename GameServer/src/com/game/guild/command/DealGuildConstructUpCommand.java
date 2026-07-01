package com.game.guild.command;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.ICommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * @Description
 * @auther lw
 * @create 2020-1-15 9:32
 */
public class DealGuildConstructUpCommand implements ICommand {
    private static final Logger logger = LogManager.getLogger(DealGuildConstructUpCommand.class);

    private final Player player;
    private final int type;

    public DealGuildConstructUpCommand(Player player, int type) {
        this.player = player;
        this.type = type;
    }

    @Override
    public void action() {
        try {
            Manager.guildsManager.manager().reqGuildConstructUp(player, type);
        } catch (Exception e) {
            logger.error(e, e);
        }
    }
}
