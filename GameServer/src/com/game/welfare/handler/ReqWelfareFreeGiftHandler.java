package com.game.welfare.handler;

import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.welfare.manager.WelfareManager;
import com.game.welfare.script.IWelfareFreeGiftScript;
import com.game.welfare.script.IWelfareScript;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.script.IScript;
import game.core.script.ScriptManager;
import game.core.util.TimeUtils;
import game.message.WelfareMessage;
import game.message.WelfareMessage.ReqWelfareFreeGift;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc // 请求领取免费礼包奖励
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqWelfareFreeGift.MsgID.eMsgID_VALUE, clazz = ReqWelfareFreeGift.class)

public class ReqWelfareFreeGiftHandler extends Handler<ReqWelfareFreeGift> {

    static final Logger log = LogManager.getLogger(ReqWelfareFreeGiftHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqWelfareFreeGift messInfo) {
        try {
            long start = TimeUtils.Time();
            Player player = (Player) mess.getExecutor();

            IScript script = ScriptManager.getInstance().GetScriptClass(ScriptEnum.WelfareFreeGiftScript);
            if (script == null) {
                log.error("没有找到免费礼包脚本：");
                return;
            }
            if(script instanceof IWelfareFreeGiftScript){
                IWelfareFreeGiftScript welfareFreeGiftScript = (IWelfareFreeGiftScript)script;
                welfareFreeGiftScript.ReqWelfareFreeGift(player);
            }else {
                log.error("没有找到免费礼包脚本：");
            }

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqWelfareFreeGiftHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
