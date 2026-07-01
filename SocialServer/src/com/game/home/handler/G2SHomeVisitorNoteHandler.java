package com.game.home.handler;

import com.game.manager.Manager;
import com.game.player.structs.GlobalPlayerWorldInfo;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.HomeMessage.G2SHomeVisitorNote;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //获取访客记录
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = G2SHomeVisitorNote.MsgID.eMsgID_VALUE, clazz = G2SHomeVisitorNote.class)

public class G2SHomeVisitorNoteHandler extends Handler<G2SHomeVisitorNote> {

    static final Logger log = LogManager.getLogger(G2SHomeVisitorNoteHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, G2SHomeVisitorNote messInfo) {
        try {
            long start = TimeUtils.Time();

            GlobalPlayerWorldInfo player = mess.getExecutor();
            Manager.homeManager.deal().G2SHomeVisitorNote(player, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2SHomeVisitorNoteHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
