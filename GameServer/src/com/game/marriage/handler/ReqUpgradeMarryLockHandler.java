package com.game.marriage.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.MarriageMessage.ReqUpgradeMarryLock;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //Client -> Server 心锁升级
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqUpgradeMarryLock.MsgID.eMsgID_VALUE, clazz = ReqUpgradeMarryLock.class)

public class ReqUpgradeMarryLockHandler extends Handler<ReqUpgradeMarryLock> {

    static final Logger log = LogManager.getLogger(ReqUpgradeMarryLockHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqUpgradeMarryLock messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.marriageManager.manager().reqUpGradeMarryLock(mess.getExecutor(), messInfo.getItemId(), messInfo.getOneKey() == 1);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqUpgradeMarryLockHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
