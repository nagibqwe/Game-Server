package com.game.guild.command;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.ICommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @Description
 * @auther lw
 * @create 2020-1-15 9:32
 */
public class CreateGuildCommand implements ICommand {
    private static final Logger logger = LogManager.getLogger(CreateGuildCommand.class);

    private final Player player;
    private final String name;
    private final int icon;
    private final String notice;

    public CreateGuildCommand(Player player, String name, int icon, String notice) {
        this.player = player;
        this.name = name;
        this.icon = icon;
        this.notice = notice;
    }

    @Override
    public void action() {
        try {
            Manager.guildsManager.manager().createGuild(player, name, icon, notice);
        } catch (Exception e) {
            logger.error(e, e);
        }
    }
}
