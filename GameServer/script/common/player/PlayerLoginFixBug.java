package common.player;


import com.data.CfgManager;
import com.data.bean.Cfg_RankAwardType_Bean;
import com.game.db.bean.RankPlayer;
import com.game.db.dao.GoldChangeDao;
import com.game.manager.Manager;
import com.game.player.script.IPlayerLoginFixBug;
import com.game.player.structs.Player;
import com.game.ranklist.handler.SyncRankPlayerHandler;
import com.game.ranklist.manager.RankListManager;
import com.game.ranklist.structs.RankType;
import com.game.script.structs.ScriptEnum;
import com.game.structs.GlobalType;
import com.game.task.structs.MainTask;
import com.game.task.structs.Task;
import game.core.script.IScript;
import game.core.util.TimeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.ParseException;

/**
 * 登录修复BUG
 * @author admin
 */
public class PlayerLoginFixBug implements IScript, IPlayerLoginFixBug {

    private static final Logger logger = LogManager.getLogger(PlayerLoginFixBug.class);

    @Override
    public int getId() {
        return ScriptEnum.PlayerLoginFixBugScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public void fixBug(Player player) {
        //修补 修仙 宝剑 消费排行
        repairConsumptionRank(player);

        fixMainTask(player);
    }

    private void fixMainTask(Player player) {
        //2021.9.11策划修改配置之后，修复老任务
        long updateTime = 0;
        try {
            updateTime = TimeUtils.getDateByString("2021-09-10 18:30:00").getTime();
        } catch (ParseException e) {
            logger.error(e,e);
            return;
        }

        //检查是否满足修复条件
        if(player.getLastLoginTime()<updateTime){
            return;
        }

        int curTaskId = player.getCurMainTaskId();
        if(curTaskId == 99144){
            reAcceptMainTask(player, curTaskId);
        }else if(curTaskId == 99158){
            reAcceptMainTask(player, curTaskId);
        }
    }

    private void reAcceptMainTask(Player player, int curTaskId) {
        //移除玩家任务
        player.getOverMainTaskIDs().remove((Integer)curTaskId);
        player.getCurMainTasks().clear();
        player.setCurMainTaskId(0);

        //重新接受任务
        Manager.taskManager.deal().acceptTask(player, Task.MAIN_TASK, curTaskId, 0, false);
    }

    private void repairConsumptionRank(Player player) {
        Cfg_RankAwardType_Bean cfg = CfgManager.getCfg_RankAwardType_Container().getValueByKey(11);
        int openDay = TimeUtils.getOpenServerDay();
        if (openDay < cfg.getStart_day() || openDay > cfg.getEnd_day()) {
            return;
        }
        long openTime = TimeUtils.getBeginTime(TimeUtils.getOpenServerTime());
        long startTime = openTime + (cfg.getStart_day() -1) * GlobalType.MILLIS_PER_DAY;
        long endTime = openTime + (cfg.getEnd_day() -1) * GlobalType.MILLIS_PER_DAY;

        RankPlayer rankPlayer = RankListManager.getRankPlayerMap().get(player.getId());
        if (null == rankPlayer) {
            return;
        }
        GoldChangeDao changeDao = new GoldChangeDao();
        int cnsuption = changeDao.getRoleTimeConsumption(player.getId(), startTime, endTime);
        if (rankPlayer.getConsumeGold() < cnsuption) {
            rankPlayer.setConsumeGold(cnsuption);
            logger.info("修复 修仙宝剑 消耗统计 oldConsumeGold   {}  newConsumeGold ", rankPlayer.getConsumeGold(), cnsuption);
            RankListManager.getInstance().addCommand(new SyncRankPlayerHandler(rankPlayer, RankType.COMSUME_GOLD_RANK));
        }
    }
}
