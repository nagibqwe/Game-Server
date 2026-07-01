package com.game.player.handler;

import com.game.manager.Manager;
import com.game.player.structs.GlobalPlayerWorldInfo;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.PlayerMessage.G2SReqPlayerSummaryInfo;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //跨服  玩家点击头像 获取跨服基础信息请求
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = G2SReqPlayerSummaryInfo.MsgID.eMsgID_VALUE, clazz = G2SReqPlayerSummaryInfo.class)

public class G2SReqPlayerSummaryInfoHandler extends Handler<G2SReqPlayerSummaryInfo> {

    static final Logger log = LogManager.getLogger(G2SReqPlayerSummaryInfoHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, G2SReqPlayerSummaryInfo messInfo) {
        try {
            long start = TimeUtils.Time();
            ChannelHandlerContext channel = mess.getContext();
            Manager.globalPlayerManager.deal().G2SReqPlayerSummaryInfoHandler(channel, messInfo);
            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2SReqPlayerSummaryInfoHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
