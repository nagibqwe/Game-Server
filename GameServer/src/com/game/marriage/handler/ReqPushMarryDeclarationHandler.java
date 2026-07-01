package com.game.marriage.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.MarriageMessage.ReqPushMarryDeclaration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //Client -> Server 发布爱情宣言
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqPushMarryDeclaration.MsgID.eMsgID_VALUE, clazz = ReqPushMarryDeclaration.class)

public class ReqPushMarryDeclarationHandler extends Handler<ReqPushMarryDeclaration> {

    static final Logger log = LogManager.getLogger(ReqPushMarryDeclarationHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqPushMarryDeclaration messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.marriageManager.wall().reqPushMarryDeclaration(mess.getExecutor(), messInfo.getDeclarationId());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqPushMarryDeclarationHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
