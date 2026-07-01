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
public class PurInvitNumCommand implements ICommand {
    private static final Logger logger = LogManager.getLogger(PurInvitNumCommand.class);

    private final Player player;

    public PurInvitNumCommand(Player player) {
        this.player = player;
    }

    @Override
    public void action() {
        try {
            Manager.marriageManager.manager().reqPurInvitNum(player);
        } catch (Exception e) {
            logger.error(e, e);
        }
    }
}
