package com.game.questionnaire.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.QuestionnaireMessage.ReqDownloadOver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //下载完后请求奖励
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqDownloadOver.MsgID.eMsgID_VALUE, clazz = ReqDownloadOver.class)

public class ReqDownloadOverHandler extends Handler<ReqDownloadOver> {

    static final Logger log = LogManager.getLogger(ReqDownloadOverHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqDownloadOver messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.questionnaireManager.deal().onDownLoadOver(player);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqDownloadOverHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
