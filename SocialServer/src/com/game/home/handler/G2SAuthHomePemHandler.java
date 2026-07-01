package com.game.home.handler;

import com.game.manager.Manager;
import com.game.player.structs.GlobalPlayerWorldInfo;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.HomeMessage.G2SAuthHomePem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * @Desc //设置房屋访问权限
 * @Desc TODO Auto Create
 * @Auth Tool
 */

@Message(id = G2SAuthHomePem.MsgID.eMsgID_VALUE, clazz = G2SAuthHomePem.class)

public class G2SAuthHomePemHandler extends Handler<G2SAuthHomePem> {

    static final Logger log = LogManager.getLogger(G2SAuthHomePemHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, G2SAuthHomePem messInfo) {
        try {
            long start = TimeUtils.Time();

            GlobalPlayerWorldInfo player = mess.getExecutor();
            Manager.homeManager.deal().G2SAuthHomePem(player, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2SAuthHomePemHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
