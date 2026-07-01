package com.game.marriage.command;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.ICommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * @Description
 * @auther admin
 * @create 2020-06-04 9:32
 */
public class AgreeInvitCommand implements ICommand {
    private static final Logger logger = LogManager.getLogger(AgreeInvitCommand.class);

    private final Player player;
    private final long roleId;
    private final boolean isAgree;

    public AgreeInvitCommand(Player player, long roleId, boolean isAgree) {
        this.player = player;
        this.roleId = roleId;
        this.isAgree = isAgree;
    }

    @Override
    public void action() {
        try {
            Manager.marriageManager.manager().reqAgreeInvit(player, roleId, isAgree);
        } catch (Exception e) {
            logger.error(e, e);
        }
    }
}
