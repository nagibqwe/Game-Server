package com.game.ranklist.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerAttributeType;
import game.core.command.ICommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CalTitleFightPowerHandler implements ICommand {

    private static final Logger log = LogManager.getLogger(CalTitleFightPowerHandler.class);

    private final long roleId;

    public CalTitleFightPowerHandler(long roleId) {
        this.roleId = roleId;
    }

    @Override
    public void action() {
        try {
            Player player = Manager.playerManager.getPlayer(roleId);
            Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.TITLE);
        } catch (Exception ex) {
            log.error(ex, ex);
        }
    }

}
