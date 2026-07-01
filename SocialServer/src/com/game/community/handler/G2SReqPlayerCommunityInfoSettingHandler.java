package com.game.community.handler;

import com.game.manager.Manager;
import com.game.player.structs.GlobalPlayerWorldInfo;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CommunityMessage.G2SReqPlayerCommunityInfoSetting;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //社交服设置
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = G2SReqPlayerCommunityInfoSetting.MsgID.eMsgID_VALUE, clazz = G2SReqPlayerCommunityInfoSetting.class)

public class G2SReqPlayerCommunityInfoSettingHandler extends Handler<G2SReqPlayerCommunityInfoSetting> {

    static final Logger log = LogManager.getLogger(G2SReqPlayerCommunityInfoSettingHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, G2SReqPlayerCommunityInfoSetting messInfo) {
        try {
            long start = TimeUtils.Time();
            GlobalPlayerWorldInfo player = mess.getExecutor();
            Manager.communityManager.deal().G2SReqPlayerCommunityInfoSetting(player, messInfo);
            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2SReqPlayerCommunityInfoSettingHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
