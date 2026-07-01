package com.game.marriage.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.MarriageMessage.ReqSelectMarryCloneImg;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //Client -> Server 默契大考验 选择图片
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqSelectMarryCloneImg.MsgID.eMsgID_VALUE, clazz = ReqSelectMarryCloneImg.class)

public class ReqSelectMarryCloneImgHandler extends Handler<ReqSelectMarryCloneImg> {

    static final Logger log = LogManager.getLogger(ReqSelectMarryCloneImgHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqSelectMarryCloneImg messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.marriageManager.clone().reqSelectMarryClone(mess.getExecutor(), messInfo.getImgId());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqSelectMarryCloneImgHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
