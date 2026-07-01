package com.game.eightdiagrams.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.EightDiagramsMessage.ReqEnterEightCityMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * @Desc //进入地图请求
 * @Desc TODO Auto Create, Do not Edit
 * @Auth Tool
 */

@Message(id = ReqEnterEightCityMap.MsgID.eMsgID_VALUE, clazz = ReqEnterEightCityMap.class)

public class ReqEnterEightCityMapHandler extends Handler<ReqEnterEightCityMap> {

    static final Logger log = LogManager.getLogger(ReqEnterEightCityMapHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqEnterEightCityMap message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            Manager.eightDiagramsManager.deal().ReqEnterEightCityMap(player, message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
