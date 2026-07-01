package com.game.huaxinflysword.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.HuaxinFlySwordMessage.ReqSwordSoulPannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求剑灵阁面板
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqSwordSoulPannel.MsgID.eMsgID_VALUE, clazz = ReqSwordSoulPannel.class)

public class ReqSwordSoulPannelHandler extends Handler<ReqSwordSoulPannel> {

    static final Logger log = LogManager.getLogger(ReqSwordSoulPannelHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqSwordSoulPannel message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            Manager.huaxinFlySwordManager.swordSoulScript().onReqSwordSoulPanel(player);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
