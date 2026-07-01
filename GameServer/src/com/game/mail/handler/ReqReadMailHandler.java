package com.game.mail.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.mailMessage.ReqReadMail;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //Client->Game 请求读取邮件内容
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqReadMail.MsgID.eMsgID_VALUE, clazz = ReqReadMail.class)

public class ReqReadMailHandler extends Handler<ReqReadMail> {

    static final Logger log = LogManager.getLogger(ReqReadMailHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqReadMail message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player)session.getExecutor();
            if (player != null) {
                Manager.mailManager.reqReadMail(player, message.getMailId());
            } else {
                log.error("未获取到玩家数据！");
            }

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
