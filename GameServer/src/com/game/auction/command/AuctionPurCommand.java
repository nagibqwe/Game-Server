package com.game.auction.command;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.ICommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @Description
 * @auther lw
 * @create 2019-10-09 14:57
 */
public class AuctionPurCommand implements ICommand {
    private static final Logger logger = LogManager.getLogger(AuctionPurCommand.class);

    private final Player player;

    private final long auctionId;

    private final String password;

    public AuctionPurCommand(Player player, long auctionId, String password) {
        this.player = player;
        this.auctionId = auctionId;
        this.password = password;
    }

    @Override
    public void action() {
        try {
            Manager.auctionManager.manager().auctionPur(player, auctionId, password);
        } catch (Exception e) {
            logger.error(e, e);
        }
    }
}
