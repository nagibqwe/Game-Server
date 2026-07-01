package com.game.luckydraw.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.LuckyDrawMessage.ReqGetLuckyDrawVolume;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * @Desc //3.请求获取抽奖卷
 * @Desc TODO Auto Create, Do not Edit
 * @Auth Tool
 */

@Message(id = ReqGetLuckyDrawVolume.MsgID.eMsgID_VALUE, clazz = ReqGetLuckyDrawVolume.class)

public class ReqGetLuckyDrawVolumeHandler extends Handler<ReqGetLuckyDrawVolume> {

    static final Logger log = LogManager.getLogger(ReqGetLuckyDrawVolumeHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqGetLuckyDrawVolume message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            if (player != null) {
                Manager.luckyDrawManager.deal().onReqGetLuckyDrawVolumeHandler(player, message);
            } else {
                log.error("未获取到玩家数据！");
            }

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
