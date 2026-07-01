package common.goddevilwar;

import com.data.Global;
import com.data.MessageString;
import com.data.bean.Cfg_Clone_map_Bean;
import com.game.dailyactive.manager.DailyActiveManager;
import com.game.dailyactive.structs.DailyActiveDefine;
import com.game.fightroom.manager.FightManager;
import com.game.fightroom.structs.FightRoom;
import com.game.fightroom.structs.FightRoomState;
import com.game.goddevilwar.script.IGodDevilWarScript;
import com.game.manager.Manager;
import com.game.script.ScriptEnum;
import com.game.servermatch.manager.ServerMatchManager;
import com.game.utils.MessageUtils;
import com.game.zone.structs.TeamPlayerInfo;
import com.game.zone.structs.ZoneTeam;
import game.core.util.TimeUtils;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.List;

public class GodDevilWarScript implements IGodDevilWarScript {

    private static final Logger log = LogManager.getLogger(GodDevilWarScript.class);

    @Override
    public int getId() {
        return ScriptEnum.GodDevilWarScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public boolean enterFightRoom(ChannelHandlerContext context,ZoneTeam zoneTeam, Cfg_Clone_map_Bean bean) {
        if (zoneTeam.getPlist().size() > 1) {
            return false;
        }
        TeamPlayerInfo playerInfo = zoneTeam.leader();
        if (playerInfo == null) {
            return false;
        }
        String groupKey = zoneTeam.getPlat() + "_" + zoneTeam.getsId();
        int groupId =  ServerMatchManager.deal().getGroupIDForCurOpenDay(groupKey, DailyActiveDefine.SJZC);
        if (groupId<0){
            for (Long roleID : zoneTeam.getPlist().keySet()){
                MessageUtils.notify_player(context,roleID, MessageString.ServerMachtFail);
            }
            return false;
        }
        List<FightRoom> rooms = FightManager.getInstance().getBravePeakRoom(bean.getId(), groupId);
        FightRoom room = null;

        long copyMapTime =   DailyActiveManager.getInstance().deal().getDailyNearlyEndTime(DailyActiveDefine.SJZC);
        long endTime = (copyMapTime - TimeUtils.Time())/1000;
        if (endTime <=0){
            for (Long roleID : zoneTeam.getPlist().keySet()){
                MessageUtils.notify_player(context,roleID, MessageString.WorldAnswerAppError);
            }
            return false;
        }
        if (endTime <= DailyActiveManager.DailyLastTime){
            for (Long roleID : zoneTeam.getPlist().keySet()){
                MessageUtils.notify_player(context,roleID, MessageString.DailyActiveTimeNotEngouh);
            }
            return false;
        }

        //查找上次去的房间和人数不足的房间
        boolean hasLastRoom = false;
        for (FightRoom fightRoom : rooms) {
            if (fightRoom.getRstate() >= FightRoomState.FIGHTEND) {
                continue;
            }
            if (fightRoom.hasRoleId(playerInfo.getRoleId())) {
                hasLastRoom = true;
                room = fightRoom;
                break;
            }
            int roomNum = 0;
            for (ZoneTeam team : fightRoom.getTeam()) {
                roomNum += team.getPlist().size();
            }
            if (roomNum < Global.SMZC_max_limit) {
                room = fightRoom;
            }
        }

        //未找到，则创建房间
        if (room == null) {
            room = Manager.fightManager.deal().createFightRoom(bean, Collections.singletonList(zoneTeam));
            room.setServerGroupId(groupId);
        }
        if (!hasLastRoom) {
            room.getTeam().add(zoneTeam);
        }
        Manager.fightManager.deal().fightStart(room, Collections.singletonList(zoneTeam));
        return true;
    }
}
