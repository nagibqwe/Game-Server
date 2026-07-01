package com.game.marriage.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.MarriageMessage.ReqMarryWallReward;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //Client -> Server 领取缘定三生奖励
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqMarryWallReward.MsgID.eMsgID_VALUE, clazz = ReqMarryWallReward.class)

public class ReqMarryWallRewardHandler extends Handler<ReqMarryWallReward> {

    static final Logger log = LogManager.getLogger(ReqMarryWallRewardHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqMarryWallReward messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.marriageManager.wall().reqMarryWallReward(mess.getExecutor());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqMarryWallRewardHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
