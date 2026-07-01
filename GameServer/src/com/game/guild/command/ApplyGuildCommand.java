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
public class ApplyGuildCommand implements ICommand {
    private static final Logger logger = LogManager.getLogger(ApplyGuildCommand.class);

    private final Player player;
    private final List<Long> guildIds;

    public ApplyGuildCommand(Player player, List<Long> guildIds) {
        this.player = player;
        this.guildIds = guildIds;
    }

    @Override
    public void action() {
        try {
            Manager.guildsManager.manager().reqApplyGuild(player, guildIds);
        } catch (Exception e) {
            logger.error(e, e);
        }
    }
}
