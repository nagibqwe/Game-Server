package com.game.task.structs;

import com.data.CfgManager;
import com.data.bean.Cfg_Task_daily_Bean;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.task.script.IDailyTask;
import com.game.task.script.ITaskScript;
import game.core.script.IScript;
import game.message.taskMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class DailyTask extends Task {

    //奖励物品 需要在接受任务时就将物品随机属性计算出
    private boolean fullStar = false;
    //日常任务的星级
    private int star = -1;

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public boolean isFullStar() {
        return fullStar;
    }

    public void setFullStar(boolean fullStar) {
        this.fullStar = fullStar;
    }

    public void noResumeFinishTask(Player player, int finishType) {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.DailyTaskBaseScript);
        if (is instanceof IDailyTask) {
            ((IDailyTask) is).finishTask(player, this, finishType);
        }
    }

    @Override
    public byte acqType() {
        return DAILY_TASK;
    }

    @Override
    public boolean finishTask(Player player, boolean isGM) {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.DailyTaskBaseScript);
        if (is instanceof ITaskScript) {
            return ((ITaskScript) is).onFinishTask(player, modelId, (int) getId(), getTaskRewardPer(), isGM, subType);
        }
        return false;
    }

    @Override
    public void changeTask(Player player) {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.DailyTaskBaseScript);
        if (is instanceof ITaskScript) {
            ((ITaskScript) is).changeTask(player, this, false);
        }
    }

    public taskMessage.dailyTaskInfo buildTaskInfo(Player player) {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.DailyTaskBaseScript);
        if (is instanceof ITaskScript) {
            return (taskMessage.dailyTaskInfo) ((ITaskScript) is).buildTaskInfo(player, this);
        }
        return null;
    }

    @Override
    public List<Integer> targetModels() {
        List<Integer> result = new ArrayList<>();
        Cfg_Task_daily_Bean model = CfgManager.getCfg_Task_daily_Container().getValueByKey(getModelId());
        if (model == null) {
            logger.error(getModelId() + "日常任务模型找不着");
            return result;
        }
        result.add(Manager.taskManager.deal().getTargetModelByType(getTargetType(), model.getGoal_npc()));
        return result;
    }

    @Override
    public int targetNum() {
        Cfg_Task_daily_Bean model = CfgManager.getCfg_Task_daily_Container().getValueByKey(getModelId());
        if (model == null) {
            logger.error(getModelId() + "日常任务模型找不着");
            return 0;
        }
        return Manager.taskManager.deal().getTargetNumByType(getTargetType(), model.getGoal_npc());
    }

    @Override
    public void release() {

    }
}
