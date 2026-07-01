package com.game.couplefight.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CouplefightMessage.G2PSendPlayerInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //游戏服到公共服-同步玩家信息
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = G2PSendPlayerInfo.MsgID.eMsgID_VALUE, clazz = G2PSendPlayerInfo.class)

public class G2PSendPlayerInfoHandler extends Handler<G2PSendPlayerInfo> {

    static final Logger log = LogManager.getLogger(G2PSendPlayerInfoHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, G2PSendPlayerInfo messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.couplefightManager.getScript().updatePlayerInfo(mess.getContext(), messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2PSendPlayerInfoHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
