package com.game.player.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.PlayerMessage.ReqLookOtherPlayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //查看其他玩家信息
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqLookOtherPlayer.MsgID.eMsgID_VALUE, clazz = ReqLookOtherPlayer.class)

public class ReqLookOtherPlayerHandler extends Handler<ReqLookOtherPlayer> {

    static final Logger log = LogManager.getLogger(ReqLookOtherPlayerHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqLookOtherPlayer messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.playerManager.managerExt().lookOtherPlayer(player, messInfo.getOtherPlayerId());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqLookOtherPlayerHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
