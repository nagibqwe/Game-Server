package com.game.map.command;

import com.game.manager.Manager;
import com.game.map.structs.MapObject;
import game.core.command.ICommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * @Description
 * @auther lw
 * @create 2019-12-06 11:57
 */
public class CreateMapCommand implements ICommand {
    private static final Logger logger = LogManager.getLogger(CreateMapCommand.class);

    private final MapObject mapObject;

    private final Object[] objects;

    public CreateMapCommand(MapObject mapObject, Object[] objects) {
        this.mapObject = mapObject;
        this.objects = objects;
    }

    @Override
    public void action(){
        try {
            Manager.mapManager.manager().createMap(mapObject, objects);
        } catch (Exception e) {
            logger.error(e, e);
        }
    }
}
