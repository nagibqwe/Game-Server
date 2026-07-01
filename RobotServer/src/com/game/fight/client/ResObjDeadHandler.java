package com.game.fight.client;
import com.game.player.structs.Player;
import com.game.structs.SessionAttribute;
import game.core.command.Handler;
import game.core.message.RMessage;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger; 
import game.core.util.TimeUtils;
import game.message.FightMessage.ResObjDead;
import game.message.MapMessage.ReqRelive;
import org.apache.mina.core.session.IoSession;


/**
* makehandler  v1.5
*	对象死亡消息
*/
public class ResObjDeadHandler extends Handler{

    private final Logger log = LogManager.getLogger(ResObjDeadHandler.class);
    private final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess) {
        try {
            long start = TimeUtils.Time();
            IoSession iosession = mess.getSession();
            ResObjDead messInfo = (ResObjDead) mess.getData();
            if (!iosession.containsAttribute(SessionAttribute.PLAYER.getValue())) {
                return;
            }
            Player player = (Player) iosession.getAttribute(SessionAttribute.PLAYER.getValue());
            player.removeNpc(messInfo.getDeaderId());
            if(player.getId() == messInfo.getDeaderId()){
                //玩家自己死亡进行回城复活
                ReqRelive.Builder msg = ReqRelive.newBuilder();
                msg.setType(1);
                msg.setIsUseGold(false);
                player.sendMsg(ReqRelive.MsgID.eMsgID_VALUE, msg.build().toByteArray());
            } else {//怪物死亡检查
                if(player.getMainTask().getMapId() == 1027){//位面副本特殊处理
                    int result = player.checkPlaneCloneRefresh(messInfo.getDeaderId());
                    if(result == 1){//周围怪物死亡，请求刷新下一波怪
                        player.waitDoTime(1000);
                        player.reqFlashMonster();
                        player.setPlaneLoop(player.getPlaneLoop()+1);
                    }else if(result == 2){//最后一波，最后一只怪死亡,请求离开副本
                        player.waitDoTime(1000);
                        player.reqCopyMapOut();
                        player.setPlaneLoop(1);
                    }
                }
            }
            log.debug("ResObjDead>" + player.getInfo() + "对象死亡,id=" + messInfo.getDeaderId());
            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ResObjDeadHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.info(e,e);
        }
    }

}