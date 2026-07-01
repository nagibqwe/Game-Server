package com.game.bi.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.BIMessage.ReqBiDevice;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc // 选择角色进入游戏时发送设备相关信息
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqBiDevice.MsgID.eMsgID_VALUE, clazz = ReqBiDevice.class)

public class ReqBiDeviceHandler extends Handler<ReqBiDevice> {

    static final Logger log = LogManager.getLogger(ReqBiDeviceHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqBiDevice message) {
        try {
            long start = TimeUtils.Time();

            Manager.biManager.getScript().onReqBiDevice((Player) session.getExecutor(), message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqBiDeviceHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
