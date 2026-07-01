package com.game.map.command;

import com.game.manager.Manager;
import com.game.map.structs.MapObject;
import com.game.player.structs.Player;
import game.core.command.ICommand;
import game.message.EquipMessage.ReqEquipSyn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * @Description
 * @auther lw
 * @create 2019-12-04 11:57
 */
public class DeleteMapCommand implements ICommand {
    private static final Logger logger = LogManager.getLogger(DeleteMapCommand.class);

    private final MapObject mapObject;

    public DeleteMapCommand(MapObject mapObject) {
        this.mapObject = mapObject;
    }

    @Override
    public void action(){
        try {
            Manager.mapManager.manager().deleteMap(mapObject);
        } catch (Exception e) {
            logger.error(e, e);
        }
    }
}
