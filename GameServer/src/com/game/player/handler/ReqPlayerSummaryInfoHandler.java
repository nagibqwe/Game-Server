package com.game.player.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.PlayerMessage.ReqPlayerSummaryInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求查看玩家简略信息
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqPlayerSummaryInfo.MsgID.eMsgID_VALUE, clazz = ReqPlayerSummaryInfo.class)

public class ReqPlayerSummaryInfoHandler extends Handler<ReqPlayerSummaryInfo> {

    static final Logger log = LogManager.getLogger(ReqPlayerSummaryInfoHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqPlayerSummaryInfo messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.playerManager.managerExt().onReqPlayerSummaryInfo(player, messInfo.getRoleId());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqPlayerSummaryInfoHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
