package com.game.copymap.handler;

import com.game.manager.Manager;
import com.game.map.structs.MapObject;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CopyMapMessage.ReqCallMonster;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * @Desc //请求召唤小怪
 * @Desc TODO Auto Create, Do not Edit
 * @Auth Tool
 */

@Message(id = ReqCallMonster.MsgID.eMsgID_VALUE, clazz = ReqCallMonster.class)

public class ReqCallMonsterHandler extends Handler<ReqCallMonster> {

    static final Logger log = LogManager.getLogger(ReqCallMonsterHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqCallMonster message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            MapObject mapObject = Manager.mapManager.getMap(player.gainMapId());
            Manager.bossManager.stateBoss().doRefreshBoss(player, mapObject);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqCallMonsterHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
