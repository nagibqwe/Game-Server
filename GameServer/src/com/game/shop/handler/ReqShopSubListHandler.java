package com.game.shop.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.shopMessage.ReqShopSubList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc // 请求所有标签列表
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqShopSubList.MsgID.eMsgID_VALUE, clazz = ReqShopSubList.class)

public class ReqShopSubListHandler extends Handler<ReqShopSubList> {

    static final Logger log = LogManager.getLogger(ReqShopSubListHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqShopSubList messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.shopManager.deal().onReqShopSubList(player, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqShopSubListHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
