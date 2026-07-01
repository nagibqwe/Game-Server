package com.game.fight.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.FightMessage.ReqPlayLockTrajectory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //播放弹道效果
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqPlayLockTrajectory.MsgID.eMsgID_VALUE, clazz = ReqPlayLockTrajectory.class)

public class ReqPlayLockTrajectoryHandler extends Handler<ReqPlayLockTrajectory> {

    static final Logger log = LogManager.getLogger(ReqPlayLockTrajectoryHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqPlayLockTrajectory message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            Manager.fightManager.deal().onReqPlayLockTrajectory(player, message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
