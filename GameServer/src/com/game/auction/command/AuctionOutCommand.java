package com.game.auction.command;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.ICommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @Description
 * @auther lw
 * @create 2019-10-09 14:31
 */
public class AuctionOutCommand implements ICommand {
    private static final Logger logger = LogManager.getLogger(AuctionOutCommand.class);

    private final Player player;
    private final long auctionId;

    public AuctionOutCommand(Player player, long auctionId) {
        this.player = player;
        this.auctionId = auctionId;
    }

    @Override
    public void action() {
        try {
            Manager.auctionManager.manager().auctionOut(player, auctionId);
        } catch (Exception e) {
            logger.error(e, e);
        }
    }
}
