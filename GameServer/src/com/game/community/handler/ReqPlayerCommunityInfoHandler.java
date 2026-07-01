package com.game.community.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CommunityMessage.ReqPlayerCommunityInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求查看玩家社区信息
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqPlayerCommunityInfo.MsgID.eMsgID_VALUE, clazz = ReqPlayerCommunityInfo.class)

public class ReqPlayerCommunityInfoHandler extends Handler<ReqPlayerCommunityInfo> {

    static final Logger log = LogManager.getLogger(ReqPlayerCommunityInfoHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqPlayerCommunityInfo messInfo) {
        try {
            long start = TimeUtils.Time();
            Player player = (Player)mess.getExecutor();
            Manager.communityManager.deal().reqPlayerCommunityInfo(player,messInfo);
            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqPlayerCommunityInfoHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
