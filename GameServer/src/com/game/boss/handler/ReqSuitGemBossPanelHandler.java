package com.game.boss.handler;

import com.game.dailyactive.script.IDailyScript;
import com.game.manager.Manager;
import com.game.map.structs.MapObject;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.BossMessage.ReqSuitGemBossPanel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * @Desc //打开套装宝石boss界面
 * @Desc TODO Auto Create, Do not Edit
 * @Auth Tool
 */

@Message(id = ReqSuitGemBossPanel.MsgID.eMsgID_VALUE, clazz = ReqSuitGemBossPanel.class)

public class ReqSuitGemBossPanelHandler extends Handler<ReqSuitGemBossPanel> {

    static final Logger log = LogManager.getLogger(ReqSuitGemBossPanelHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqSuitGemBossPanel message) {
        try {
            long start = TimeUtils.Time();

            Player player =  session.getExecutor();
            MapObject map = Manager.mapManager.getMap(player.gainMapId());
            //套装boss
            int scriptId = 0;
            if (message.getType() == 0) {
                scriptId = ScriptEnum.SuitBossScript;
            }
            if (message.getType() == 1) {
                scriptId = ScriptEnum.GemBossScript;
            }
            IDailyScript iScript = (IDailyScript) Manager.scriptManager.GetScriptClass(scriptId);
            iScript.sendBossPanel(player, map);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqSuitGemBossPanelHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
