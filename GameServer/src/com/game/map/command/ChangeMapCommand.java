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
public class ChangeMapCommand implements ICommand {
    private static final Logger logger = LogManager.getLogger(ChangeMapCommand.class);

    private final Player player;

    private final long mapId;

    private final int modelId;

    private final int line;

    private final Position pos;

    private final int type;

    private final boolean isLogin;

    public ChangeMapCommand(Player player, long mapId, int modelId, int line, Position pos, int type, boolean isLogin) {
        this.player = player;
        this.mapId = mapId;
        this.modelId = modelId;
        this.line = line;
        this.pos = pos;
        this.type = type;
        this.isLogin = isLogin;
    }

    @Override
    public void action(){
        try {
            Manager.mapManager.manager().changeMap(player, mapId, modelId, line, pos, type, isLogin);
        } catch (Exception e) {
            logger.error(e, e);
        }
    }
}
