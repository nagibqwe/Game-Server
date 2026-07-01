package com.game.mail.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.mailMessage.ReqOneClickReceiveMailAttach;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //Client->Game 请求一键领取邮件的附件物品
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqOneClickReceiveMailAttach.MsgID.eMsgID_VALUE, clazz = ReqOneClickReceiveMailAttach.class)

public class ReqOneClickReceiveMailAttachHandler extends Handler<ReqOneClickReceiveMailAttach> {

    static final Logger log = LogManager.getLogger(ReqOneClickReceiveMailAttachHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqOneClickReceiveMailAttach message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player)session.getExecutor();
            if (player != null) {
                Manager.mailManager.reqOneClickReceiveMailAttach(player, message.getMailIdListList());
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
