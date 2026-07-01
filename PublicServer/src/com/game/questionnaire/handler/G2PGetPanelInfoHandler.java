package com.game.questionnaire.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.QuestionnaireMessage.G2PGetPanelInfo;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //游戏服-->公共服  请求角色面板信息
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = G2PGetPanelInfo.MsgID.eMsgID_VALUE, clazz = G2PGetPanelInfo.class)

public class G2PGetPanelInfoHandler extends Handler<G2PGetPanelInfo> {

    static final Logger log = LogManager.getLogger(G2PGetPanelInfoHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, G2PGetPanelInfo messInfo) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = mess.getContext();
            Manager.questionnaireManager.deal().onG2PGetPanelInfo(context, messInfo.getUserId(), messInfo.getRoleId());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2PGetPanelInfoHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
