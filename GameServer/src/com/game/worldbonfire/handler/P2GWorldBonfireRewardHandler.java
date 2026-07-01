package com.game.worldbonfire.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.WorldBonfireMessage.P2GWorldBonfireReward;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //跨服返回游戏服领奖
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = P2GWorldBonfireReward.MsgID.eMsgID_VALUE, clazz = P2GWorldBonfireReward.class)

public class P2GWorldBonfireRewardHandler extends Handler<P2GWorldBonfireReward> {

    static final Logger log = LogManager.getLogger(P2GWorldBonfireRewardHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, P2GWorldBonfireReward messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = Manager.playerManager.getPlayerOnline(messInfo.getRoleId());
            if (player == null) {
                return;
            }
            Manager.worldBonfireManager.manager().onBonfireFingerLocalReward(player);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("P2GWorldBonfireRewardHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
