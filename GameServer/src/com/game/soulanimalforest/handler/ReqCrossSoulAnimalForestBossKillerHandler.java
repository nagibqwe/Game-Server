package com.game.soulanimalforest.handler;

import com.game.player.structs.Player;
import com.game.utils.MessageUtils;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.SoulAnimalForestMessage;
import game.message.SoulAnimalForestMessage.ReqCrossSoulAnimalForestBossKiller;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求跨服BOSS的击杀人
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqCrossSoulAnimalForestBossKiller.MsgID.eMsgID_VALUE, clazz = ReqCrossSoulAnimalForestBossKiller.class)

public class ReqCrossSoulAnimalForestBossKillerHandler extends Handler<ReqCrossSoulAnimalForestBossKiller> {

    static final Logger log = LogManager.getLogger(ReqCrossSoulAnimalForestBossKillerHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqCrossSoulAnimalForestBossKiller messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player)mess.getExecutor();
            SoulAnimalForestMessage.G2PReqCrossSoulAnimalForestBossKiller.Builder msg = SoulAnimalForestMessage.G2PReqCrossSoulAnimalForestBossKiller.newBuilder();
            msg.setBossConfigId(messInfo.getBossConfigId());
            msg.setRoleId(player.getId());
            MessageUtils.send_to_public(SoulAnimalForestMessage.G2PReqCrossSoulAnimalForestBossKiller.MsgID.eMsgID_VALUE, msg.build().toByteArray());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqCrossSoulAnimalForestBossKillerHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
