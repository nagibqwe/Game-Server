package com.game.crossserver.handler;

import com.game.crossserver.scripts.ICrossServerScript;
import com.game.manager.Manager;
import com.game.script.structs.ScriptEnum;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.script.IScript;
import game.core.util.TimeUtils;
import game.message.CrossServerMessage.P2GNoticeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //公共服通知游戏服的特殊事件处理
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = P2GNoticeEvent.MsgID.eMsgID_VALUE, clazz = P2GNoticeEvent.class)

public class P2GNoticeEventHandler extends Handler<P2GNoticeEvent> {

    static final Logger log = LogManager.getLogger(P2GNoticeEventHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, P2GNoticeEvent message) {
        try {
            long start = TimeUtils.Time();

            IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.CrossServerBaseScript);
            if (is instanceof ICrossServerScript) {
                ICrossServerScript icss = (ICrossServerScript) is;
                icss.OnP2GNoticeEvent(message);
            } else {
                log.error("公共服发送事件消息来时， 没有得到脚本类！");
            }

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
