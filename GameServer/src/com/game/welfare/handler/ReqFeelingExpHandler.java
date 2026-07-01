package com.game.welfare.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.welfare.script.IFeelingExpScript;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.WelfareMessage;
import game.message.WelfareMessage.ReqFeelingExp;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc // 请求感悟经验
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqFeelingExp.MsgID.eMsgID_VALUE, clazz = ReqFeelingExp.class)

public class ReqFeelingExpHandler extends Handler<ReqFeelingExp> {

    static final Logger log = LogManager.getLogger(ReqFeelingExpHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqFeelingExp messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();

            IFeelingExpScript script = (IFeelingExpScript) Manager.welfareManager.getScript(WelfareMessage.WelfareType.FeelingExp);
            if (script != null)
                script.onReqFeelingExp(player, messInfo.getTyp(), messInfo.getTimes());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqFeelingExpHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
