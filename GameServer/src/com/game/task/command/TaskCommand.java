package com.game.task.command;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.ICommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @Desc TODO
 * @Date 2021/7/21 14:35
 * @Auth ZUncle
 */
public class TaskCommand implements ICommand {

    final Logger logger = LogManager.getLogger(TaskCommand.class);

    Player player;
    int type;
    int changeNum;

    public TaskCommand(Player player, int type, int changeNum) {
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
            Manager.taskManager.deal().functionVariableChange(player, type, changeNum);
        } catch (Exception e) {
            logger.error(e, e);
        }
    }
}
