package com.game.worldbonfire.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.WorldBonfireMessage.P2GWorldBonfireAddWoodCheckRes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //跨服返回篝火升级结果
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = P2GWorldBonfireAddWoodCheckRes.MsgID.eMsgID_VALUE, clazz = P2GWorldBonfireAddWoodCheckRes.class)

public class P2GWorldBonfireAddWoodCheckResHandler extends Handler<P2GWorldBonfireAddWoodCheckRes> {

    static final Logger log = LogManager.getLogger(P2GWorldBonfireAddWoodCheckResHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, P2GWorldBonfireAddWoodCheckRes messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = Manager.playerManager.getPlayerOnline(messInfo.getRoleId());
            if (player == null) {
                return;
            }
            Manager.worldBonfireManager.manager().onBonfireDecItem(player, messInfo.getLv());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("P2GWorldBonfireAddWoodCheckResHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
