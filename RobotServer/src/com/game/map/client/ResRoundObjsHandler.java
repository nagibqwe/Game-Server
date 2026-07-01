package com.game.map.client;
import com.game.player.structs.Player;
import com.game.structs.SessionAttribute;
import com.game.utils.Utils;
import game.core.command.Handler;
import game.core.message.RMessage;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger; 
import game.core.util.TimeUtils;
import game.message.MapMessage.ResRoundObjs;
import org.apache.mina.core.session.IoSession;
import game.message.CommonMessage;

/**
* makehandler  v1.5
*地图周围数据
*/
public class ResRoundObjsHandler extends Handler{

    private final Logger log = LogManager.getLogger(ResRoundObjsHandler.class);
    private final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess) {
        try {
            long start = TimeUtils.Time();
            IoSession iosession = mess.getSession();
            ResRoundObjs messInfo = (ResRoundObjs) mess.getData();
            if (!iosession.containsAttribute(SessionAttribute.PLAYER.getValue())) {
                return;
            }
            Player player = (Player) iosession.getAttribute(SessionAttribute.PLAYER.getValue());
            for(Long id : messInfo.getRemoveIdsList()){
                player.removeNpc(id);
            }
            for(CommonMessage.PlayerInfo info : messInfo.getPlayersList()){
                if(player.getId() == info.getPlayerId()){
                    player.getCurPos().setX(info.getX());
                    player.getCurPos().setY(info.getY());
                }
                player.addMapPeople(Utils.makeMapPeople(info));
            }
            for(CommonMessage.MonsterInfo info : messInfo.getMonstersList()){
                player.addMapMonster(Utils.makeMapMonster(info));
            }
            for(CommonMessage.GatherInfo info : messInfo.getGathersList()){
                player.addMapGather(Utils.makeMapGather(info));
            }
            for(CommonMessage.NpcInfo info : messInfo.getNpcsList()){
                player.addMapNPC(Utils.makeMapNPC(info));
            }
            for(CommonMessage.PetInfo info : messInfo.getPetsList()){
                if(player.getPet().getId() == info.getId()){
                    player.getPet().getCurPos().setX(info.getX());
                    player.getPet().getCurPos().setY(info.getY());
                }
                player.addMapPet(Utils.makeMapPet(info));
            }
            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ResRoundObjsHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.info(e,e);
        }
    }

}