package com.game.guildcrossfud.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.GuildCrossFudMessage.ReqCrossFudUnLockScoreBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //解锁积分宝箱
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqCrossFudUnLockScoreBox.MsgID.eMsgID_VALUE, clazz = ReqCrossFudUnLockScoreBox.class)

public class ReqCrossFudUnLockScoreBoxHandler extends Handler<ReqCrossFudUnLockScoreBox> {

    static final Logger log = LogManager.getLogger(ReqCrossFudUnLockScoreBoxHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqCrossFudUnLockScoreBox message) {
        try {
            long start = TimeUtils.Time();

            Manager.crossFudManager.deal().reqCrossFudUnLockScoreBoxHandler(session.getExecutor(), message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
