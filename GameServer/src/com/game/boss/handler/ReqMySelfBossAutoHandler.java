package com.game.boss.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.BossMessage.ReqMySelfBossAuto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //设置自动扣钱后，时间到了主动推送更新消息
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqMySelfBossAuto.MsgID.eMsgID_VALUE, clazz = ReqMySelfBossAuto.class)

public class ReqMySelfBossAutoHandler extends Handler<ReqMySelfBossAuto> {

    static final Logger log = LogManager.getLogger(ReqMySelfBossAutoHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqMySelfBossAuto message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            Manager.bossManager.personalBossScript().call(player, "onReqMySelfBossAuto", message.getAuto());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqMySelfBossAutoHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
