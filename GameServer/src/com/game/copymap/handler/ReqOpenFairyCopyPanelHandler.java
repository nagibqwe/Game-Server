package com.game.copymap.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CopyMapMessage.ReqOpenFairyCopyPanel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求面板信息
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqOpenFairyCopyPanel.MsgID.eMsgID_VALUE, clazz = ReqOpenFairyCopyPanel.class)

public class ReqOpenFairyCopyPanelHandler extends Handler<ReqOpenFairyCopyPanel> {

    static final Logger log = LogManager.getLogger(ReqOpenFairyCopyPanelHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqOpenFairyCopyPanel message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            Manager.scriptManager.GetScriptClass(ScriptEnum.FairyLandActivityScript).call(player, "openPanel", message.getCopyId());


            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqOpenFairyCopyPanelHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
