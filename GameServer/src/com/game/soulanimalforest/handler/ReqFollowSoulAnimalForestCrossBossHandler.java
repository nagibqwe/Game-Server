package com.game.soulanimalforest.handler;

import com.game.player.structs.Player;
import com.game.utils.MessageUtils;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.net.Config.ServerConfig;
import game.core.util.TimeUtils;
import game.message.SoulAnimalForestMessage;
import game.message.SoulAnimalForestMessage.ReqFollowSoulAnimalForestCrossBoss;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求是否关注跨服BOSS的关注
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqFollowSoulAnimalForestCrossBoss.MsgID.eMsgID_VALUE, clazz = ReqFollowSoulAnimalForestCrossBoss.class)

public class ReqFollowSoulAnimalForestCrossBossHandler extends Handler<ReqFollowSoulAnimalForestCrossBoss> {

    static final Logger log = LogManager.getLogger(ReqFollowSoulAnimalForestCrossBossHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqFollowSoulAnimalForestCrossBoss messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            SoulAnimalForestMessage.G2PReqFollowSoulAnimalForestCrossBoss.Builder msg = SoulAnimalForestMessage.G2PReqFollowSoulAnimalForestCrossBoss.newBuilder();
            msg.setOs(player.getOs());
            msg.setBossId(messInfo.getBossId());
            msg.setFollowValue(messInfo.getFollowValue());
            msg.setPlat(ServerConfig.getServerPlatform());
            msg.setRoleId(player.getId());
            msg.setSid(ServerConfig.getServerId());
            MessageUtils.send_to_public(SoulAnimalForestMessage.G2PReqFollowSoulAnimalForestCrossBoss.MsgID.eMsgID_VALUE, msg.build().toByteArray());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqFollowSoulAnimalForestCrossBossHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
