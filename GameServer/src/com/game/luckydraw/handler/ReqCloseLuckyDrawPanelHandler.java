package com.game.luckydraw.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.LuckyDrawMessage.ReqCloseLuckyDrawPanel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * @Desc //6.关闭抽奖面板的消息
 * @Desc TODO Auto Create, Do not Edit
 * @Auth Tool
 */

@Message(id = ReqCloseLuckyDrawPanel.MsgID.eMsgID_VALUE, clazz = ReqCloseLuckyDrawPanel.class)

public class ReqCloseLuckyDrawPanelHandler extends Handler<ReqCloseLuckyDrawPanel> {

    static final Logger log = LogManager.getLogger(ReqCloseLuckyDrawPanelHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqCloseLuckyDrawPanel message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            if (player != null) {
                Manager.luckyDrawManager.deal().onReqCloseLuckyDrawPanelHandler(player, message);
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
