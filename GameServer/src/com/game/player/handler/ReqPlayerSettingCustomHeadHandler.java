package com.game.player.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.PlayerMessage.ReqPlayerSettingCustomHead;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //玩家 设置自定义头像 请求
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqPlayerSettingCustomHead.MsgID.eMsgID_VALUE, clazz = ReqPlayerSettingCustomHead.class)

public class ReqPlayerSettingCustomHeadHandler extends Handler<ReqPlayerSettingCustomHead> {

    static final Logger log = LogManager.getLogger(ReqPlayerSettingCustomHeadHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqPlayerSettingCustomHead messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            if (player != null) {
                Manager.playerManager.deal(ScriptEnum.PlayerCustomHeadScript).playerSettingCustomHead(player, messInfo.getCustomHeadPath(),messInfo.getUseCustomHead());
            } else {
                log.error("未获取到玩家数据！");
            }

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqPlayerSettingCustomHeadHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
