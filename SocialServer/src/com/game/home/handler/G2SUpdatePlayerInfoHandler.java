package com.game.home.handler;

import com.game.manager.Manager;
import com.game.player.structs.GlobalPlayerWorldInfo;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.HomeMessage.G2SUpdatePlayerInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //房屋升级
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = G2SUpdatePlayerInfo.MsgID.eMsgID_VALUE, clazz = G2SUpdatePlayerInfo.class)

public class G2SUpdatePlayerInfoHandler extends Handler<G2SUpdatePlayerInfo> {

    static final Logger log = LogManager.getLogger(G2SUpdatePlayerInfoHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, G2SUpdatePlayerInfo messInfo) {
        try {
            long start = TimeUtils.Time();

            GlobalPlayerWorldInfo player = mess.getExecutor();
            Manager.homeManager.deal().G2SUpdatePlayerInfo(player, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2SUpdatePlayerInfoHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
