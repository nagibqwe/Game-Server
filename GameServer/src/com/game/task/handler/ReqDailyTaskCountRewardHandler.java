package com.game.task.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.task.script.IDailyTask;
import com.game.task.script.ITaskScript;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.script.IScript;
import game.core.util.TimeUtils;
import game.message.taskMessage.ReqDailyTaskCountReward;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //日常任务 赏金之道 领取环数奖励请求
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqDailyTaskCountReward.MsgID.eMsgID_VALUE, clazz = ReqDailyTaskCountReward.class)

public class ReqDailyTaskCountRewardHandler extends Handler<ReqDailyTaskCountReward> {

    static final Logger log = LogManager.getLogger(ReqDailyTaskCountRewardHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqDailyTaskCountReward messInfo) {
        try {
            long start = TimeUtils.Time();
            Player player = (Player) mess.getExecutor();
            IScript script = Manager.scriptManager.GetScriptClass(ScriptEnum.DailyTaskBaseScript);
            ((IDailyTask)script).reqDailyTaskCountReward(player, messInfo);
            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqDailyTaskCountRewardHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
