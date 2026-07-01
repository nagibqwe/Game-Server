package com.game.copymap.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CopyMapMessage.ReqCopySetting;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求副本设置
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqCopySetting.MsgID.eMsgID_VALUE, clazz = ReqCopySetting.class)

public class ReqCopySettingHandler extends Handler<ReqCopySetting> {

    static final Logger log = LogManager.getLogger(ReqCopySettingHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqCopySetting message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            Manager.copyMapManager.logic().changeCloneSetting(player, message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqCopySettingHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
