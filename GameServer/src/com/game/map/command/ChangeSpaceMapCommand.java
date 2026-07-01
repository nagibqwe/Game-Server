package com.game.map.command;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.ICommand;
import game.core.map.Position;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * @Description
 * @auther lw
 * @create 2019-12-06 11:57
 */
public class ChangeSpaceMapCommand implements ICommand {
    private static final Logger logger = LogManager.getLogger(ChangeSpaceMapCommand.class);

    private final Player player;

    public ChangeSpaceMapCommand(Player player) {
        this.player = player;
    }

    @Override
    public void action(){
        try {
            Manager.mapManager.manager().changeSpaceMap(player);
        } catch (Exception e) {
            logger.error(e, e);
        }
    }
}
