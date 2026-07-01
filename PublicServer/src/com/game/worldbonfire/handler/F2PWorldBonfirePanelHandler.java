package com.game.worldbonfire.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.WorldBonfireMessage.F2PWorldBonfirePanel;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //进入地图通知公共服获取数据
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = F2PWorldBonfirePanel.MsgID.eMsgID_VALUE, clazz = F2PWorldBonfirePanel.class)

public class F2PWorldBonfirePanelHandler extends Handler<F2PWorldBonfirePanel> {

    static final Logger log = LogManager.getLogger(F2PWorldBonfirePanelHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, F2PWorldBonfirePanel messInfo) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = mess.getContext();
            Manager.worldBonfireManager.manager().onWorldBonfirePanel(context, messInfo.getRoleId(),messInfo.getGatherCount());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("F2PWorldBonfirePanelHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
