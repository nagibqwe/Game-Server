package com.game.task.command;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.ICommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @Desc TODO
 * @Date 2021/7/21 14:43
 * @Auth ZUncle
 */
public class TaskFinishCommand implements ICommand {

    final Logger logger = LogManager.getLogger(TaskFinishCommand.class);

    Player player;
    int taskType;
    int modelId;
    int taskId;
    int finishPer;
    int subType;

    public TaskFinishCommand(Player player, int taskType, int modelId, int taskId, int finishPer, int subType) {
        this.player = player;
        this.taskType = taskType;
        this.modelId = modelId;
        this.taskId = taskId;
        this.finishPer = finishPer;
        this.subType = subType;
    }

    /**
     * 执行命令.
     */
    @Override
    public void action() {
        try {
            Manager.taskManager.deal().onFinishTask(player, taskType, modelId, taskId, finishPer, subType);
        } catch (Exception e) {
            logger.error(e, e);
        }
    }
}
