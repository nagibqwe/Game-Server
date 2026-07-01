package com.game.jjc.command;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.ICommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @Desc TODO
 * @Date 2020/9/21 20:54
 * @Auth ZUncle
 */
public class JJCChangeCareerCommand implements ICommand {

    final Logger logger = LogManager.getLogger(JJCChangeCareerCommand.class);

    final int oldCareer;
    final Player player;

    public JJCChangeCareerCommand(int oldCareer, Player player) {
        this.oldCareer = oldCareer;
        this.player = player;
    }

    /**
     * 执行命令.
     */
    @Override
    public void action() {
        try {
            Manager.jjcManager.deal().onDelete(player, oldCareer);
        } catch (Exception e) {
            logger.error(e, e);
        }
    }
}
