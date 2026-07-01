package com.game.eightdiagrams.handler;

import com.game.eightdiagrams.manager.EightDiagramsManager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.EightDiagramsMessage.G2PReqTickMapInfo;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc 
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = G2PReqTickMapInfo.MsgID.eMsgID_VALUE, clazz = G2PReqTickMapInfo.class)

public class G2PReqTickMapInfoHandler extends Handler<G2PReqTickMapInfo> {

    static final Logger log = LogManager.getLogger(G2PReqTickMapInfoHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, G2PReqTickMapInfo messInfo) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = mess.getContext();
            EightDiagramsManager.getInstance().deal().G2PReqTickMapInfo(context,messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2PReqTickMapInfoHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
