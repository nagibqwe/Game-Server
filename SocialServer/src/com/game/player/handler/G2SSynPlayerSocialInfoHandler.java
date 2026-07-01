package com.game.player.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.PlayerMessage.G2SSynPlayerSocialInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //跨服 玩家同步数据到社交服务器
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = G2SSynPlayerSocialInfo.MsgID.eMsgID_VALUE, clazz = G2SSynPlayerSocialInfo.class)

public class G2SSynPlayerSocialInfoHandler extends Handler<G2SSynPlayerSocialInfo> {

    static final Logger log = LogManager.getLogger(G2SSynPlayerSocialInfoHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, G2SSynPlayerSocialInfo messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.globalPlayerManager.deal().G2SSynPlayerSocialInfo(mess.getContext(), messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2SSynPlayerSocialInfoHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
