package com.game.ranklist.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.RankListMessage.ReqRankInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //Client->Game 请求排行榜数据
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqRankInfo.MsgID.eMsgID_VALUE, clazz = ReqRankInfo.class)

public class ReqRankInfoHandler extends Handler<ReqRankInfo> {

    static final Logger log = LogManager.getLogger(ReqRankInfoHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqRankInfo messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.rankListManager.deal().OnReqRankInfo(player, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqRankInfoHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
