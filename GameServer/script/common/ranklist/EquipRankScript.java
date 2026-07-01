package common.ranklist;

import com.game.db.bean.RankPlayer;
import com.game.manager.Manager;
import com.game.ranklist.manager.RankListManager;
import com.game.ranklist.script.IRankScript;
import com.game.ranklist.structs.RankType;
import com.game.script.structs.ScriptEnum;
import game.core.script.IScript;
import game.message.RankListMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EquipRankScript implements IScript, IRankScript {

    @Override
    public int getId() {
        return ScriptEnum.EquipRankScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public int getRankType() {
        return RankType.EQUIP_RANK;
    }

    @Override
    public List<RankListMessage.RankInfo.Builder> getRankInfo() {
        List<RankListMessage.RankInfo.Builder> rankInfoList = new ArrayList<>();
        ConcurrentHashMap<Integer, Long> rankMap = RankListManager.getTempRankMap().get(getRankType());
        for (Map.Entry<Integer, Long> entry : rankMap.entrySet()) {
            long roleId = entry.getValue();
            RankPlayer rankPlayer = RankListManager.getRankPlayerMap().get(roleId);
            if (rankPlayer == null) {
                return null;
            }
            Manager.rankListManager.deal().buildRankInfo(entry.getKey(), rankPlayer,
                    Manager.playerManager.isOnline(rankPlayer.getRoleId()), String.valueOf(rankPlayer.getEquipFightPower()), rankInfoList,rankPlayer.getEquipFightPower());
        }
        return rankInfoList;
    }

    @Override
    public boolean canRank(RankPlayer rankPlayer) {
        return getCompareValue(rankPlayer) > 0;
    }

    @Override
    public int compareRankPlayer(RankPlayer p1, RankPlayer p2) {
        if (getCompareValue(p2) != getCompareValue(p1)) {
            return getCompareValue(p2) > getCompareValue(p1) ? 1 : -1;
        }
        return 0;
    }

    @Override
    public long getCompareValue(RankPlayer rankPlayer) {
        return rankPlayer.getEquipFightPower();
    }
}
