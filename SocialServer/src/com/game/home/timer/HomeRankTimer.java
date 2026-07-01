package com.game.home.timer;

import com.game.manager.Manager;
import game.core.command.ICommand;

/**
 * @Desc TODO
 * @Date 2021/8/2 17:11
 * @Auth ZUncle
 */
public class HomeRankTimer implements ICommand {

    /**
     * 执行命令.
     */
    @Override
    public void action() {

        Manager.homeManager.deal().checkRankOver();

    }
}
