package com.game.eightdiagrams.handler;

import com.game.eightdiagrams.manager.EightDiagramsManager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.EightDiagramsMessage.G2PReqEnterEightCityMap;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //向公共服请求
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = G2PReqEnterEightCityMap.MsgID.eMsgID_VALUE, clazz = G2PReqEnterEightCityMap.class)

public class G2PReqEnterEightCityMapHandler extends Handler<G2PReqEnterEightCityMap> {

    static final Logger log = LogManager.getLogger(G2PReqEnterEightCityMapHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, G2PReqEnterEightCityMap messInfo) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = mess.getContext();
            EightDiagramsManager.getInstance().deal().G2PReqEnterEightCityMap(context,messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2PReqEnterEightCityMapHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
