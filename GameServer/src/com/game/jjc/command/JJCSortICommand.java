package com.game.jjc.command;

import com.game.jjc.structs.JJC;
import com.game.manager.Manager;
import game.core.command.ICommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @Desc TODO
 * @Date 2020/9/21 20:49
 * @Auth ZUncle
 */
public class JJCSortICommand implements ICommand {

    final Logger logger = LogManager.getLogger(JJCChangeCareerCommand.class);

    final int newsort;
    final JJC rank;

    public JJCSortICommand(int newsort, JJC rank) {
        this.newsort = newsort;
        this.rank = rank;
    }

    /**
     * 执行命令.
     */
    @Override
    public void action() {
        try {
            Manager.jjcManager.deal().sort(newsort, rank);
        } catch (Exception e) {
            logger.error(e, e);
        }
    }
}
