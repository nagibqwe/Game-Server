package com.game.marriage.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.MarriageMessage.ReqMarrySendItem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //赠送道具
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqMarrySendItem.MsgID.eMsgID_VALUE, clazz = ReqMarrySendItem.class)

public class ReqMarrySendItemHandler extends Handler<ReqMarrySendItem> {

    static final Logger log = LogManager.getLogger(ReqMarrySendItemHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqMarrySendItem messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.marriageManager.manager().reqMarrySendItem(player,messInfo.getIndex(),messInfo.getRoleId());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqMarrySendItemHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
