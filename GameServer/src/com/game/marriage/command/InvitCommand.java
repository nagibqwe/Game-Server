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
public class InvitCommand implements ICommand {
    private static final Logger logger = LogManager.getLogger(InvitCommand.class);

    private final Player player;
    private final long roleId;
    private final int type;

    public InvitCommand(Player player, long roleId, int type) {
        this.player = player;
        this.roleId = roleId;
        this.type = type;
    }

    @Override
    public void action() {
        try {
            Manager.marriageManager.manager().reqInvit(player, roleId, type);
        } catch (Exception e) {
            logger.error(e, e);
        }
    }
}
