package com.game.home.handler;

import com.game.home.manager.HomeManager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.HomeMessage.ReqHomeVisitorNote;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //获取访客记录
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqHomeVisitorNote.MsgID.eMsgID_VALUE, clazz = ReqHomeVisitorNote.class)

public class ReqHomeVisitorNoteHandler extends Handler<ReqHomeVisitorNote> {

    static final Logger log = LogManager.getLogger(ReqHomeVisitorNoteHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqHomeVisitorNote messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = mess.getExecutor();
            HomeManager.getInstance().deal().reqHomeVisitorNoteHandler(player, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqHomeVisitorNoteHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
