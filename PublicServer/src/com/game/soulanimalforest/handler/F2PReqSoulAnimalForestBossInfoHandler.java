package com.game.soulanimalforest.handler;

import com.game.soulanimalforest.manager.SoulAnimalForestManager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.SoulAnimalForestMessage.F2PReqSoulAnimalForestBossInfo;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求跨服更新BOSS的刷新信息
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = F2PReqSoulAnimalForestBossInfo.MsgID.eMsgID_VALUE, clazz = F2PReqSoulAnimalForestBossInfo.class)

public class F2PReqSoulAnimalForestBossInfoHandler extends Handler<F2PReqSoulAnimalForestBossInfo> {

    static final Logger log = LogManager.getLogger(F2PReqSoulAnimalForestBossInfoHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, F2PReqSoulAnimalForestBossInfo messInfo) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = mess.getContext();
            SoulAnimalForestManager.getInstance().manager().onF2PReqSoulAnimalForestBossInfo(context, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("F2PReqSoulAnimalForestBossInfoHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
