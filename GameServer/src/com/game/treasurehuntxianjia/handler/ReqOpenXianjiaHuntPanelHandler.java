package com.game.treasurehuntxianjia.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.TreasureHuntXianjiaMessage.ReqOpenXianjiaHuntPanel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求打开仙甲寻宝 界面
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqOpenXianjiaHuntPanel.MsgID.eMsgID_VALUE, clazz = ReqOpenXianjiaHuntPanel.class)

public class ReqOpenXianjiaHuntPanelHandler extends Handler<ReqOpenXianjiaHuntPanel> {

    static final Logger log = LogManager.getLogger(ReqOpenXianjiaHuntPanelHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqOpenXianjiaHuntPanel messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.treasureHuntXianjiaManager.deal().onReqOpenXianjiaHuntPanel(player,messInfo.getType());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqOpenXianjiaHuntPanelHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
