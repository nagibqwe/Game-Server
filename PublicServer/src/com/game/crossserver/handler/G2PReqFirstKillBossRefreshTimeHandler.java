package com.game.crossserver.handler;

import com.game.soulanimalforest.manager.SoulAnimalForestManager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CrossServerMessage.G2PReqFirstKillBossRefreshTime;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求跨服首杀boss刷新数据
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = G2PReqFirstKillBossRefreshTime.MsgID.eMsgID_VALUE, clazz = G2PReqFirstKillBossRefreshTime.class)

public class G2PReqFirstKillBossRefreshTimeHandler extends Handler<G2PReqFirstKillBossRefreshTime> {

    static final Logger log = LogManager.getLogger(G2PReqFirstKillBossRefreshTimeHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, G2PReqFirstKillBossRefreshTime messInfo) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = mess.getContext();
            SoulAnimalForestManager.getInstance().manager().onG2PFirstKillBossRefreshTime(context, messInfo.getRoleId());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2PReqFirstKillBossRefreshTimeHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
