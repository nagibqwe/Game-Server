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

/**
 * @Desc TODO
 * @Date 2021/6/29 18:03
 * @Auth ZUncle
 */
public class IntimacyRankScript implements IRankScript {
    /**
     * 排行榜类型
     */
    @Override
    public int getRankType() {
        return RankType.Intimacy_Rank;
    }

    /**
     * 特定类型的排行榜网络消息
     */
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
            Manager.rankListManager.deal().buildRankInfo(entry.getKey(), rankPlayer, isOnLine, String.valueOf(getCompareValue(rankPlayer)), rankInfoList,-1);
        }
        return rankInfoList;
    }

    /**
     * 检查是否可以进入排行榜
     *
     * @param rankPlayer
     */
    @Override
    public boolean canRank(RankPlayer rankPlayer) {
        return getCompareValue(rankPlayer) > 0;
    }

    /**
     * 排序
     *
     * @param p1
     * @param p2
     */
    @Override
    public int compareRankPlayer(RankPlayer p1, RankPlayer p2) {
        if (getCompareValue(p2) != getCompareValue(p1)) {
            return getCompareValue(p2) > getCompareValue(p1) ? 1 : -1;
        }
        return 0;
    }

    /**
     * 该排行榜对比的值
     *
     * @param rankPlayer
     */
    @Override
    public long getCompareValue(RankPlayer rankPlayer) {
        return rankPlayer.getIntimacy();
    }

    /**
     * 获取scriptId
     *
     * @return
     */
    @Override
    public int getId() {
        return ScriptEnum.IntimacyRankScript;
    }

    /**
     * 调用脚本
     *
     * @param args 参数
     * @return
     */
    @Override
    public Object call(Object... args) {
        return null;
    }
}
