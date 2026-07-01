package com.game.community.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CommunityMessage.ReqPlayerCommunityInfoSetting;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //设置社区个人信息请求
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqPlayerCommunityInfoSetting.MsgID.eMsgID_VALUE, clazz = ReqPlayerCommunityInfoSetting.class)

public class ReqPlayerCommunityInfoSettingHandler extends Handler<ReqPlayerCommunityInfoSetting> {

    static final Logger log = LogManager.getLogger(ReqPlayerCommunityInfoSettingHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqPlayerCommunityInfoSetting messInfo) {
        try {
            long start = TimeUtils.Time();
            Player player = (Player)mess.getExecutor();
            Manager.communityManager.deal().reqPlayerCommunityInfoSetting(player,messInfo);
            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqPlayerCommunityInfoSettingHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
