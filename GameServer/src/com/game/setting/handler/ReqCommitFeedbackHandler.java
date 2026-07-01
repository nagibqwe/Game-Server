package com.game.setting.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.SettingMessage.ReqCommitFeedback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //提交反馈
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqCommitFeedback.MsgID.eMsgID_VALUE, clazz = ReqCommitFeedback.class)

public class ReqCommitFeedbackHandler extends Handler<ReqCommitFeedback> {

    static final Logger log = LogManager.getLogger(ReqCommitFeedbackHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqCommitFeedback messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.settingManager.deal().onReqCommitFeedback(player, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqCommitFeedbackHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
