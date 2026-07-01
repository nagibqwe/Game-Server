package com.game.auction.command;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.ICommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @Description
 * @auther lw
 * @create 2019-10-09 14:32
 */
public class AuctionCommand implements ICommand {
    private static final Logger logger = LogManager.getLogger(AuctionCommand.class);

    private final Player player;
    private final long auctionId;
    private final int price;

    public AuctionCommand(Player player, long auctionId, int price) {
        this.player = player;
        this.auctionId = auctionId;
        this.price = price;
    }

    @Override
    public void action() {
        try {
            Manager.auctionManager.manager().auction(player, auctionId, price);
        } catch (Exception e) {
            logger.error(e, e);
        }
    }
}
