package com.game.ranklist.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.RankListMessage.ReqRankPlayerImageInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //Client->Game 请求排行榜玩家的外观形象数据
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqRankPlayerImageInfo.MsgID.eMsgID_VALUE, clazz = ReqRankPlayerImageInfo.class)

public class ReqRankPlayerImageInfoHandler extends Handler<ReqRankPlayerImageInfo> {

    static final Logger log = LogManager.getLogger(ReqRankPlayerImageInfoHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqRankPlayerImageInfo messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.rankListManager.deal().OnReqRankPlayerImageInfo(player, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqRankPlayerImageInfoHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
