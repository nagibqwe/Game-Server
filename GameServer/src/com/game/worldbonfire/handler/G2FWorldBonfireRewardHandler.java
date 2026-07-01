package com.game.worldbonfire.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.WorldBonfireMessage.G2FWorldBonfireReward;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //游戏请求战斗服领奖
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = G2FWorldBonfireReward.MsgID.eMsgID_VALUE, clazz = G2FWorldBonfireReward.class)

public class G2FWorldBonfireRewardHandler extends Handler<G2FWorldBonfireReward> {

    static final Logger log = LogManager.getLogger(G2FWorldBonfireRewardHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, G2FWorldBonfireReward messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = Manager.playerManager.getPlayerOnline(messInfo.getRoleId());
            if (player == null) {
                return;
            }
            Manager.worldBonfireManager.manager().onBonfireFingerCrossReward(player);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2FWorldBonfireRewardHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
