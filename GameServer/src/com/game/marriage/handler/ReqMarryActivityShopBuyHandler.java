package com.game.marriage.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.MarriageMessage.ReqMarryActivityShopBuy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //情缘商店购买请求
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqMarryActivityShopBuy.MsgID.eMsgID_VALUE, clazz = ReqMarryActivityShopBuy.class)

public class ReqMarryActivityShopBuyHandler extends Handler<ReqMarryActivityShopBuy> {

    static final Logger log = LogManager.getLogger(ReqMarryActivityShopBuyHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqMarryActivityShopBuy messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.marriageManager.activity().reqMarryActivityShopBuy(player, messInfo.getShopId());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqMarryActivityShopBuyHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
