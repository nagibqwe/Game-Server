package com.game.auction.command;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.ICommand;
import game.message.EquipMessage.ReqEquipSyn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * @Description
 * @auther lw
 * @create 2019-10-09 14:57
 */
public class AuctionFastPurCommand implements ICommand {
    private static final Logger logger = LogManager.getLogger(AuctionFastPurCommand.class);

    private final Player player;

    private final ReqEquipSyn mess;

    public AuctionFastPurCommand(Player player, ReqEquipSyn mess) {
        this.player = player;
        this.mess = mess;
    }

    @Override
    public void action() {
        try {
            Manager.auctionManager.manager().auctionFastPur(player, mess);
        } catch (Exception e) {
            logger.error(e, e);
        }
    }
}
