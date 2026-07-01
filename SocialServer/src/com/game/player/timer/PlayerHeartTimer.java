package com.game.player.timer;

import com.game.manager.Manager;
import game.core.command.ICommand;

/**
 * @Desc TODO
 * @Date 2021/8/9 15:58
 * @Auth ZUncle
 */
public class PlayerHeartTimer implements ICommand {
    /**
     * 执行命令.
     */
    @Override
    public void action() {

        Manager.globalPlayerManager.deal().tick();

    }
}
