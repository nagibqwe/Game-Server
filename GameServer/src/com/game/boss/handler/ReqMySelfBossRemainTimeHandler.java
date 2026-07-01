package com.game.boss.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.BossMessage.ReqMySelfBossRemainTime;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求个人BOSS刷新剩余时间
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqMySelfBossRemainTime.MsgID.eMsgID_VALUE, clazz = ReqMySelfBossRemainTime.class)

public class ReqMySelfBossRemainTimeHandler extends Handler<ReqMySelfBossRemainTime> {

    static final Logger log = LogManager.getLogger(ReqMySelfBossRemainTimeHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqMySelfBossRemainTime message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            Manager.bossManager.personalBossScript().call(player, "onReqMySelfBossRemainTime");

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqMySelfBossRemainTimeHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
