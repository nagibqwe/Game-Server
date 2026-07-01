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
public class ChangeGuildRankCommand implements ICommand {
    private static final Logger logger = LogManager.getLogger(ChangeGuildRankCommand.class);

    private final Player player;
    private final long roleId;
    private final int rank;


    public ChangeGuildRankCommand(Player player, long roleId, int rank) {
        this.player = player;
        this.roleId = roleId;
        this.rank = rank;
    }

    @Override
    public void action() {
        try {
            Manager.guildsManager.manager().reqChangeGuildRank(player, roleId, rank);
        } catch (Exception e) {
            logger.error(e, e);
        }
    }
}
