package com.game.universe.handler;

import com.game.count.structs.BaseCountType;
import com.game.dailyactive.structs.DailyActiveDefine;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.utils.MessageUtils;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.MSG_UniverseMessage;
import game.message.MSG_UniverseMessage.ReqUniverseWarPanel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //打开太虚战场面板
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqUniverseWarPanel.MsgID.eMsgID_VALUE, clazz = ReqUniverseWarPanel.class)

public class ReqUniverseWarPanelHandler extends Handler<ReqUniverseWarPanel> {

    static final Logger log = LogManager.getLogger(ReqUniverseWarPanelHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqUniverseWarPanel messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            if (player == null) {
                return;
            }
            MSG_UniverseMessage.G2PReqUniverseWarPanel.Builder msg = MSG_UniverseMessage.G2PReqUniverseWarPanel.newBuilder();
            msg.setRoleId(player.getId());
            msg.setAnger((int) Manager.countManager.getCount(player, BaseCountType.UniverseAnger, DailyActiveDefine.ACTIVITY_CROSS_UNIVERSEWAR.getValue()));
            MessageUtils.send_to_public(MSG_UniverseMessage.G2PReqUniverseWarPanel.MsgID.eMsgID_VALUE, msg.build().toByteArray());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqUniverseWarPanelHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
