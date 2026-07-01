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
public class DealApplyGuildCommand implements ICommand {
    private static final Logger logger = LogManager.getLogger(DealApplyGuildCommand.class);

    private final Player player;
    private final List<Long> playerIds;
    private final boolean flag;

    public DealApplyGuildCommand(Player player, List<Long> playerIds, boolean flag) {
        this.player = player;
        this.playerIds = playerIds;
        this.flag = flag;
    }

    @Override
    public void action() {
        try {
            Manager.guildsManager.manager().reqApplyDelGuild(player, playerIds, flag);
        } catch (Exception e) {
            logger.error(e, e);
        }
    }
}
