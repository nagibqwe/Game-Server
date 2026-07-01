package com.game.welfare.handler;

import com.game.player.structs.Player;
import com.game.welfare.manager.WelfareManager;
import com.game.welfare.script.IUpdateNoticeScript;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.WelfareMessage;
import game.message.WelfareMessage.ReqGetUpdateNoticeAward;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc // 请求领取更新公告奖励
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqGetUpdateNoticeAward.MsgID.eMsgID_VALUE, clazz = ReqGetUpdateNoticeAward.class)

public class ReqGetUpdateNoticeAwardHandler extends Handler<ReqGetUpdateNoticeAward> {

    static final Logger log = LogManager.getLogger(ReqGetUpdateNoticeAwardHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqGetUpdateNoticeAward messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();

            IUpdateNoticeScript script = (IUpdateNoticeScript) WelfareManager.getInstance().getScript(WelfareMessage.WelfareType.UpdateNotice);
            if (script != null)
                script.receiveAward(player);
            else
                log.error("未定义更新公告脚本：ReqGetUpdateNoticeAward");

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetUpdateNoticeAwardHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
