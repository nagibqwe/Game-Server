package com.game.ranklist.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.RankListMessage.ReqGetAllRankListState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求所有排行榜状态
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqGetAllRankListState.MsgID.eMsgID_VALUE, clazz = ReqGetAllRankListState.class)

public class ReqGetAllRankListStateHandler extends Handler<ReqGetAllRankListState> {

    static final Logger log = LogManager.getLogger(ReqGetAllRankListStateHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqGetAllRankListState messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.rankListManager.deal().onReqGetAllRankListState(player);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAllRankListStateHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
