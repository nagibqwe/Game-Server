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
public class ChangeGuildNameCommand implements ICommand {
    private static final Logger logger = LogManager.getLogger(ChangeGuildNameCommand.class);

    private final Player player;
    private final String name;

    public ChangeGuildNameCommand(Player player, String name) {
        this.player = player;
        this.name = name;
    }

    @Override
    public void action() {
        try {
            Manager.guildsManager.manager().reqChangeGuildName(player, name);
        } catch (Exception e) {
            logger.error(e, e);
        }
    }
}
