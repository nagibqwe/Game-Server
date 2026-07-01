package com.game.couplefight.timer;

import com.game.manager.Manager;
import game.core.command.ICommand;

/**
 * @Auther: gouzhongliang
 * @Date: 2021/7/5 10:23
 */
public class CouplefightRefushCommand implements ICommand {

    @Override
    public void action() {
        Manager.couplefightManager.getScript().refresh();
    }
}
