package com.game.ranklist.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.RankListMessage.ReqCompareAttr;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //Client->Game 请求对比属性
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqCompareAttr.MsgID.eMsgID_VALUE, clazz = ReqCompareAttr.class)

public class ReqCompareAttrHandler extends Handler<ReqCompareAttr> {

    static final Logger log = LogManager.getLogger(ReqCompareAttrHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqCompareAttr messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.rankListManager.deal().onReqCompareAttr(player, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqCompareAttrHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
