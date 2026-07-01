package com.game.devilseries.handler;

import com.game.devilseries.manager.DevilSeriesManager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.DevilSeriesMessage.ReqDevilHuntPanel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //打开魔魂抽奖面板请求
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqDevilHuntPanel.MsgID.eMsgID_VALUE, clazz = ReqDevilHuntPanel.class)

public class ReqDevilHuntPanelHandler extends Handler<ReqDevilHuntPanel> {

    static final Logger log = LogManager.getLogger(ReqDevilHuntPanelHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqDevilHuntPanel messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            DevilSeriesManager.instance.getScript().onReqDevilHuntPanelHandler(player);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqDevilHuntPanelHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
