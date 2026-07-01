package com.game.marriage.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.MarriageMessage.ReqGetMarryActivityTaskReward;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //情缘任务领奖
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqGetMarryActivityTaskReward.MsgID.eMsgID_VALUE, clazz = ReqGetMarryActivityTaskReward.class)

public class ReqGetMarryActivityTaskRewardHandler extends Handler<ReqGetMarryActivityTaskReward> {

    static final Logger log = LogManager.getLogger(ReqGetMarryActivityTaskRewardHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqGetMarryActivityTaskReward messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.marriageManager.activity().onReqGetMarryActivityTaskReward(player,messInfo.getTaskID());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetMarryActivityTaskRewardHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
