package com.game.marriage.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.MarriageMessage.ReqRewardTitle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //Client -> Server 领取仙缘称号
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqRewardTitle.MsgID.eMsgID_VALUE, clazz = ReqRewardTitle.class)

public class ReqRewardTitleHandler extends Handler<ReqRewardTitle> {

    static final Logger log = LogManager.getLogger(ReqRewardTitleHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqRewardTitle messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.marriageManager.manager().reqRewardTitle(mess.getExecutor(), messInfo.getId());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqRewardTitleHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
