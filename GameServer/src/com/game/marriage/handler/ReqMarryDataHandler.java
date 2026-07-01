package com.game.marriage.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.MarriageMessage.ReqMarryData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求婚姻信息
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqMarryData.MsgID.eMsgID_VALUE, clazz = ReqMarryData.class)

public class ReqMarryDataHandler extends Handler<ReqMarryData> {

    static final Logger log = LogManager.getLogger(ReqMarryDataHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqMarryData messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.marriageManager.manager().reqMarriageData(player);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqMarryDataHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
