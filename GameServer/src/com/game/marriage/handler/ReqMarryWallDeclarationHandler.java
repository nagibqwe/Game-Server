package com.game.marriage.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.MarriageMessage.ReqMarryWallDeclaration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //Client -> Server 请求相亲墙 爱情宣言
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqMarryWallDeclaration.MsgID.eMsgID_VALUE, clazz = ReqMarryWallDeclaration.class)

public class ReqMarryWallDeclarationHandler extends Handler<ReqMarryWallDeclaration> {

    static final Logger log = LogManager.getLogger(ReqMarryWallDeclarationHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqMarryWallDeclaration messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.marriageManager.wall().reqMarryWallDeclaration(mess.getExecutor());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqMarryWallDeclarationHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
