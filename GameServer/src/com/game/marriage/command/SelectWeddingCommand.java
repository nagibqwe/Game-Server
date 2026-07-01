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
public class SelectWeddingCommand implements ICommand {
    private static final Logger logger = LogManager.getLogger(SelectWeddingCommand.class);

    private final Player player;
    private final int id;

    public SelectWeddingCommand(Player player, int id) {
        this.player = player;
        this.id = id;
    }

    @Override
    public void action() {
        try {
            Manager.marriageManager.manager().reqSelectMarriage(player, id);
        } catch (Exception e) {
            logger.error(e, e);
        }
    }
}
