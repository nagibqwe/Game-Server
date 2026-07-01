package com.game.task.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.task.script.ITaskScript;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.script.IScript;
import game.core.util.TimeUtils;
import game.message.taskMessage.ReqChangeJob;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc 
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqChangeJob.MsgID.eMsgID_VALUE, clazz = ReqChangeJob.class)

public class ReqChangeJobHandler extends Handler<ReqChangeJob> {

    static final Logger log = LogManager.getLogger(ReqChangeJobHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqChangeJob messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            IScript script = Manager.scriptManager.GetScriptClass(ScriptEnum.GenderTaskBaseScript);
            ((ITaskScript)script).onFinishTask(player, player.getCurGenderTask().getModelId(), player.getCurGenderTask().getModelId(), 0, false, 0);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqChangeJobHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
