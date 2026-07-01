package com.game.crossserver.handler;

import com.game.server.MainServer;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CrossServerMessage.G2PSynPlayerName;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //玩家改名成功同步到公共服  --游戏服没用
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = G2PSynPlayerName.MsgID.eMsgID_VALUE, clazz = G2PSynPlayerName.class)

public class G2PSynPlayerNameHandler extends Handler<G2PSynPlayerName> {

    static final Logger log = LogManager.getLogger(G2PSynPlayerNameHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, G2PSynPlayerName messInfo) {
        try {
            long start = TimeUtils.Time();

            MainServer.getRoleName().put(messInfo.getRoleId(), messInfo.getName());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2PSynPlayerNameHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
