package com.game.welfare.handler;

import com.game.player.structs.Player;
import com.game.welfare.manager.WelfareManager;
import com.game.welfare.script.IWelfareScript;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.WelfareMessage;
import game.message.WelfareMessage.ReqWelfareData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc // 请求福利数据
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqWelfareData.MsgID.eMsgID_VALUE, clazz = ReqWelfareData.class)

public class ReqWelfareDataHandler extends Handler<ReqWelfareData> {

    static final Logger log = LogManager.getLogger(ReqWelfareDataHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqWelfareData messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();

            IWelfareScript script = (IWelfareScript) WelfareManager.getInstance().getScript(WelfareMessage.WelfareType.valueOf(messInfo.getTyp()));
            if (script != null)
                script.freshDataNtf(player);
            else
                log.error("未定义福利脚本：" + messInfo.getTyp());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqWelfareDataHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
