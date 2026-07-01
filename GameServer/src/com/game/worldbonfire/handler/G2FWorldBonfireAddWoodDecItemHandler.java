package com.game.worldbonfire.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.WorldBonfireMessage.G2FWorldBonfireAddWoodDecItem;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求跨服篝火升级扣取道具
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = G2FWorldBonfireAddWoodDecItem.MsgID.eMsgID_VALUE, clazz = G2FWorldBonfireAddWoodDecItem.class)

public class G2FWorldBonfireAddWoodDecItemHandler extends Handler<G2FWorldBonfireAddWoodDecItem> {

    static final Logger log = LogManager.getLogger(G2FWorldBonfireAddWoodDecItemHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, G2FWorldBonfireAddWoodDecItem messInfo) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = mess.getContext();
            Player player = Manager.playerManager.getPlayerOnline(messInfo.getRoleId());
            if (player == null) {
                return;
            }
            Manager.worldBonfireManager.manager().onBonfireCrossDecItem(context, player, messInfo.getLv());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2FWorldBonfireAddWoodDecItemHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
