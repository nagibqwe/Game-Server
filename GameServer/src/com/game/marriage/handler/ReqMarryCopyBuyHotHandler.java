package com.game.marriage.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.MarriageMessage.ReqMarryCopyBuyHot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //婚宴副本购买热度请求
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqMarryCopyBuyHot.MsgID.eMsgID_VALUE, clazz = ReqMarryCopyBuyHot.class)

public class ReqMarryCopyBuyHotHandler extends Handler<ReqMarryCopyBuyHot> {

    static final Logger log = LogManager.getLogger(ReqMarryCopyBuyHotHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqMarryCopyBuyHot messInfo) {
        try {
            long start = TimeUtils.Time();
            Player player = (Player) mess.getExecutor();
            Manager.marriageManager.manager().reqMarryCopyBuyHot(player);
            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqMarryCopyBuyHotHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
