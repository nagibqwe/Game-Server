package common.ranklist;

import com.game.db.bean.RankPlayer;
import com.game.manager.Manager;
import com.game.ranklist.manager.RankListManager;
import com.game.ranklist.script.IRankScript;
import com.game.ranklist.structs.RankType;
import com.game.script.structs.ScriptEnum;
import game.message.RankListMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LevelRankScript implements IRankScript {

    @Override
    public int getId() {
        return ScriptEnum.LevelRankScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public int getRankType() {
        return RankType.LEVEL_RANK;
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
            boolean isOnLine = Manager.playerManager.isOnline(rankPlayer.getRoleId());
            Manager.rankListManager.deal().buildRankInfo(entry.getKey(), rankPlayer, isOnLine, rankPlayer.getLevel() + "", rankInfoList,-1);
        }
        return rankInfoList;
    }

    @Override
    public boolean canRank(RankPlayer rankPlayer) {
        return getCompareValue(rankPlayer) > 0;
    }

    @Override
    public int compareRankPlayer(RankPlayer p1, RankPlayer p2) {
        if (p2.getLevel() != p1.getLevel()) {
            return p2.getLevel() > p1.getLevel() ? 1 : -1;
        } else if (p2.getExp() != p1.getExp()) {
            return p2.getExp() > p1.getExp() ? 1 : -1;
        } else if (p2.getLevelUpTime() != p1.getLevelUpTime()) {
            return p1.getLevelUpTime() > p2.getLevelUpTime() ? 1 : -1;
        }
        return 0;
    }

    @Override
    public long getCompareValue(RankPlayer rankPlayer) {
        return rankPlayer.getLevel();
    }
}
