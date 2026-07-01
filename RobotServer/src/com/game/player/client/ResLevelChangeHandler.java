package com.game.player.client;

import com.game.manager.Manager;
import com.game.nature.struct.Nature;
import com.game.nature.struct.NatureType;
import com.game.player.structs.Player;
import com.game.structs.SessionAttribute;
import game.core.command.Handler;
import game.core.message.RMessage;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger; 
import game.core.util.TimeUtils;
import game.message.PlayerMessage.ResLevelChange;
import org.apache.mina.core.session.IoSession;

/**
 * makehandler v1.5 等级变化
 */
public class ResLevelChangeHandler extends Handler {

    private final Logger log = LogManager.getLogger(ResLevelChangeHandler.class);
    private final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess) {
        try {
            long start = TimeUtils.Time();
            IoSession iosession = mess.getSession();
            ResLevelChange messInfo = (ResLevelChange) mess.getData();
            if (!iosession.containsAttribute(SessionAttribute.PLAYER.getValue())) {
                return;
            }
            Player player = (Player) iosession.getAttribute(SessionAttribute.PLAYER.getValue());
            if (messInfo.getPlayerId() != player.getId()) {
                return;
            }
            int oldLevel = player.getLevel();
            player.setLevel(messInfo.getLevel());
//            if(player.getLevel()==20){
//                player.setLastEventType(player.getEventType());
//                player.setEventType(EventDefine.Event_OneKeyAtt);
//            }

            if (player.getLevel() % 10==0){
                player.chatGM("&additem 3 500000");
                player.chatGM("&addatt 1 10000");
                player.chatGM("&addatt 2 50000");
                player.chatGM("&addatt 3 10000");
                player.chatGM("&addatt 4 50000");
                //同步升级技能
                Manager.skillManager.deal().sendReqOneKeyUpSkill(player);
            }
            //达到指定等级，主动请求开启法宝功能
            if(player.getLevel()>=10){
                Nature nature =player.getNatures().get(NatureType.STIFLEFFABAO);
                if(nature!=null&&nature.getCurModelId()!=0){
                    //GM升级法宝
                    player.chatGM("&setstiflelevel " + player.getLevel() / 10 +" "+ player.getLevel() % 10);
                }else{
                    player.reqNatureInfo(NatureType.STIFLEFFABAO);
                }
            }
            log.info("ResLevelChange>" + player.getInfo() + "等级改变"+oldLevel+">"+player.getLevel());
            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ResLevelChangeHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.info(e, e);
        }
    }

}
