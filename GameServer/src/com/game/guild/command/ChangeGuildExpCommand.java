package com.game.guild.command;

import com.game.guild.structs.Guild;
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
public class ChangeGuildExpCommand implements ICommand {
    private static final Logger logger = LogManager.getLogger(ChangeGuildExpCommand.class);

    private final Guild guild;
    private final long exp;
    private final int res;
    private final long actionId;

    public ChangeGuildExpCommand(Guild guild, long exp, int res, long actionId) {
        this.guild = guild;
        this.exp = exp;
        this.res = res;
        this.actionId = actionId;
    }

    @Override
    public void action() {
        try {
            Manager.guildsManager.manager().changeGuildExp(guild, exp, res, actionId);
        } catch (Exception e) {
            logger.error(e, e);
        }
    }
}
