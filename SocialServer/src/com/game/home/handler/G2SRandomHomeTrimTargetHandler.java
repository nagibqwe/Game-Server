package com.game.home.handler;

import com.game.manager.Manager;
import com.game.player.structs.GlobalPlayerWorldInfo;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.HomeMessage.G2SRandomHomeTrimTarget;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //获取投票对象
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = G2SRandomHomeTrimTarget.MsgID.eMsgID_VALUE, clazz = G2SRandomHomeTrimTarget.class)

public class G2SRandomHomeTrimTargetHandler extends Handler<G2SRandomHomeTrimTarget> {

    static final Logger log = LogManager.getLogger(G2SRandomHomeTrimTargetHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, G2SRandomHomeTrimTarget messInfo) {
        try {
            long start = TimeUtils.Time();

            GlobalPlayerWorldInfo player = mess.getExecutor();
            Manager.homeManager.deal().G2SRandomHomeTrimTarget(player, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2SRandomHomeTrimTargetHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
