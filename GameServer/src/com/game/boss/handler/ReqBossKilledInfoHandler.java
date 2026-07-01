package com.game.boss.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.BossMessage.ReqBossKilledInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求boss击杀记录信息
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqBossKilledInfo.MsgID.eMsgID_VALUE, clazz = ReqBossKilledInfo.class)

public class ReqBossKilledInfoHandler extends Handler<ReqBossKilledInfo> {

    static final Logger log = LogManager.getLogger(ReqBossKilledInfoHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqBossKilledInfo message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            Manager.bossManager.manager().reqBossKilledInfo(player, message.getBossId(), message.getBossType());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqBossKilledInfoHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
