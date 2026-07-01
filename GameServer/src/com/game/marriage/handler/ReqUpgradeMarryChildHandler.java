package com.game.marriage.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.MarriageMessage.ReqUpgradeMarryChild;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //Client -> Server 升级仙娃
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqUpgradeMarryChild.MsgID.eMsgID_VALUE, clazz = ReqUpgradeMarryChild.class)

public class ReqUpgradeMarryChildHandler extends Handler<ReqUpgradeMarryChild> {

    static final Logger log = LogManager.getLogger(ReqUpgradeMarryChildHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqUpgradeMarryChild messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.marriageManager.manager().reqUpgradeMarryChild(mess.getExecutor(), messInfo.getChildId(), messInfo.getItemId(), messInfo.getOneKey() == 1);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqUpgradeMarryChildHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
