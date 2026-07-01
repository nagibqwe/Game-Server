package com.game.player.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.PlayerMessage.ReqChangeJobPanel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求当前转职界面
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqChangeJobPanel.MsgID.eMsgID_VALUE, clazz = ReqChangeJobPanel.class)

public class ReqChangeJobPanelHandler extends Handler<ReqChangeJobPanel> {

    static final Logger log = LogManager.getLogger(ReqChangeJobPanelHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqChangeJobPanel messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.playerManager.deal(ScriptEnum.ChangeJobBaseScript).sendPlayerChangeFateStar(player,messInfo.getGender());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqChangeJobPanelHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
