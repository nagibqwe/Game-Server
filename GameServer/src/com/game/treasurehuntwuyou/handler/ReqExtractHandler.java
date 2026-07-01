package com.game.treasurehuntwuyou.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.TreasureHuntWuyouMessage.ReqExtract;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求提取
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqExtract.MsgID.eMsgID_VALUE, clazz = ReqExtract.class)

public class ReqExtractHandler extends Handler<ReqExtract> {

    static final Logger log = LogManager.getLogger(ReqExtractHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqExtract messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.treasureHuntWuyouManager.getScript().reqExtract((Player) mess.getExecutor(), messInfo.getUid());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqExtractHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
