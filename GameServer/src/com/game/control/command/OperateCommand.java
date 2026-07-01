package com.game.control.command;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.ICommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @Desc TODO
 * @Date 2021/7/13 15:53
 * @Auth ZUncle
 */
public class OperateCommand implements ICommand {

    static final Logger logger = LogManager.getLogger(OperateCommand.class);

    Player player;
    int type;
    int changeNum;

    public OperateCommand(Player player, int type, int changeNum) {
        this.player = player;
        this.type = type;
        this.changeNum = changeNum;
    }

    /**
     * 执行命令.
     */
    @Override
    public void action() {

        try {
            Manager.controlManager.deal().operate(player, type, changeNum);
        } catch (Exception e) {
            logger.error(e, e);
        }

    }
}
