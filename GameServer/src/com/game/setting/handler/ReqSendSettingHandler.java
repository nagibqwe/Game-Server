package com.game.setting.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.SettingMessage.ReqSendSetting;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //发送设置信息
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqSendSetting.MsgID.eMsgID_VALUE, clazz = ReqSendSetting.class)

public class ReqSendSettingHandler extends Handler<ReqSendSetting> {

    static final Logger log = LogManager.getLogger(ReqSendSettingHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqSendSetting messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.settingManager.deal().onReqSendSetting(player, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqSendSettingHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
