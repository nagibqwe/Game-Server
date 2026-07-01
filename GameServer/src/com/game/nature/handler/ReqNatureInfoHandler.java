package com.game.nature.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.NatureMessage.ReqNatureInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求造化面板信息
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqNatureInfo.MsgID.eMsgID_VALUE, clazz = ReqNatureInfo.class)

public class ReqNatureInfoHandler extends Handler<ReqNatureInfo> {

    static final Logger log = LogManager.getLogger(ReqNatureInfoHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqNatureInfo messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            if (player != null) {
                Manager.natureManager.deal().onReqNatureInfo(player, messInfo.getNatureType());
            } else {
                log.error("未获取到玩家数据！");
            }

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqNatureInfoHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
