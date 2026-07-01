package com.game.openserverac.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.OpenServerAcMessage.ReqGetLuckyTaskReawrd;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //领取幸运翻牌任务奖励请求
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqGetLuckyTaskReawrd.MsgID.eMsgID_VALUE, clazz = ReqGetLuckyTaskReawrd.class)

public class ReqGetLuckyTaskReawrdHandler extends Handler<ReqGetLuckyTaskReawrd> {

    static final Logger log = LogManager.getLogger(ReqGetLuckyTaskReawrdHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqGetLuckyTaskReawrd messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.openServerAcManager.deal().getLuckyCardWish(player, messInfo.getTaskId());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetLuckyTaskReawrdHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
