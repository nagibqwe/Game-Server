package com.game.marriage.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.MarriageMessage.ReqMarryActivityIntimacy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //完美情缘获取 亲密度
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqMarryActivityIntimacy.MsgID.eMsgID_VALUE, clazz = ReqMarryActivityIntimacy.class)

public class ReqMarryActivityIntimacyHandler extends Handler<ReqMarryActivityIntimacy> {

    static final Logger log = LogManager.getLogger(ReqMarryActivityIntimacyHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqMarryActivityIntimacy messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.marriageManager.activity().reqMarryActivityIntimacy(player);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqMarryActivityIntimacyHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
