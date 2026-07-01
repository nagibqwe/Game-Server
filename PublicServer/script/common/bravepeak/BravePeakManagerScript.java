package common.bravepeak;

import com.data.Global;
import com.data.MessageString;
import com.data.bean.Cfg_Clone_map_Bean;
import com.game.bravepeak.manager.BravePeakManager;
import com.game.bravepeak.script.IBravePeakScript;
import com.game.dailyactive.manager.DailyActiveManager;
import com.game.dailyactive.structs.DailyActiveDefine;
import com.game.fightroom.manager.FightManager;
import com.game.fightroom.structs.FightRoom;
import com.game.fightroom.structs.FightRoomState;
import com.game.gameserver.manager.GameServerManager;
import com.game.manager.Manager;
import com.game.script.ScriptEnum;
import com.game.servermatch.manager.ServerMatchManager;
import com.game.utils.MessageUtils;
import com.game.utils.RandomUtils;
import com.game.zone.structs.ZoneMapDefine;
import com.game.zone.structs.ZoneTeam;
import game.core.util.IDConfigUtil;
import game.core.util.TimeUtils;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class BravePeakManagerScript implements IBravePeakScript {

    private static final Logger logger = LogManager.getLogger(BravePeakManagerScript.class);
    @Override
    public int getId() {
        return ScriptEnum.BravePeakScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public boolean enterBravePeakRoom(ChannelHandlerContext context, ZoneTeam zt, int modelId, Cfg_Clone_map_Bean bean) {
        if (zt == null) {
            return false;
        }
        if (!Manager.dailyActiveManager.deal().isActiveOpen(DailyActiveDefine.YZZD)) {
            for (Long roleID : zt.getPlist().keySet()){
                MessageUtils.notify_player(context,roleID, MessageString.WorldAnswerAppError);
            }
            return false;
        }
        //获取组号
        String groupKey = zt.getPlat() + "_" + zt.getsId();
        Integer groupId = ServerMatchManager.deal().getGroupIDForCurOpenDay(groupKey,DailyActiveDefine.YZZD);
        if (groupId < 0) {
            for (Long roleID : zt.getPlist().keySet()){
                MessageUtils.notify_player(context,roleID, MessageString.ServerMachtFail);
            }
            return false;
        }
        //查找已经创建好的房间
        FightRoom mine = null;
        //如果组的队是没有满的
        List<FightRoom> list = FightManager.getInstance().getBravePeakRoom(modelId, groupId);
        if (list.size() > 0) {
            List<FightRoom> filter = checkActivityFilter(list, 1, bean, zt.getBirthGroup());
            if (filter.size() > 0) {
                mine = filter.get(RandomUtils.random(filter.size()));
            }
        }

        //如果没有分配到房间， 则创建一个新的房间
        List<ZoneTeam> zts = new ArrayList<>();
        zts.add(zt);
        long copyMapTime =   DailyActiveManager.getInstance().deal().getDailyNearlyEndTime(DailyActiveDefine.YZZD);
        long endTime = copyMapTime - TimeUtils.Time();
        if (endTime <=0){
            for (Long roleID : zt.getPlist().keySet()){
                MessageUtils.notify_player(context,roleID, MessageString.WorldAnswerAppError);
            }
            return false;
        }
        if (mine == null) {
            mine = createFightRoom( bean,  zts, endTime);
            mine.setServerGroupId(groupId);
        } else {
            mine.getTeam().add(zt);
            mine.setFightTime(endTime);
        }
        if (mine == null)
            return false;

        mine.setAllReadyStart(true);
        Manager.fightManager.deal().fightStart(mine, zts);
        return true;
    }

    private List<FightRoom> checkActivityFilter(List<FightRoom> list, int num, Cfg_Clone_map_Bean bean, int birthGroup) {
        int maxnum = Global.TZZD_max_limit;
        List<FightRoom> fl = new ArrayList<>();

        for (FightRoom fr : list) {
            if (fr.getRstate() >= FightRoomState.FIGHTEND) {
                continue;
            }
            if (fr.hasPeoples() >= maxnum) {
                continue;
            }
            int need = maxnum - fr.hasPeoples();
            //人数不满足
            if (need < num) {
                continue;
            }
            fl.add(fr);
        }
        return fl;
    }

    private FightRoom createFightRoom(Cfg_Clone_map_Bean bean, List<ZoneTeam> zt,long copyTime){
        FightRoom room = new FightRoom();
        room.setModelId(bean.getId());
        room.setCtime(TimeUtils.Time());
        room.setCrId(zt.get(0).leader().getRoleId());//取第一个了
        room.setCname(zt.get(0).leader().getName());
        room.setFid(IDConfigUtil.getLogId());
        room.setAllReadyStart(false);
        room.setType(bean.getType());
        room.setWaitTime(room.getCtime());
        room.setEndwait(room.getCtime() + bean.getEnter_time());
        room.setFightTime(copyTime);
        room.getTeam().addAll(zt);
        room.setAttackValue(0);
        room.setMinP(ZoneMapDefine.GM_ZONE_MAP_MIN_NUM);//最低要求的人数
        room.setRstate(FightRoomState.CREATEROOM);
        FightManager.getInstance().SaveRoomInfo(room, zt.get(0).getPlat(), zt.get(0).getsId());//保存并且写log
        return room;
    }
}
