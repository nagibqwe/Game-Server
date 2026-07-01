package com.game.task.structs;

import com.data.CfgManager;
import com.data.bean.Cfg_Task_branch_Bean;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.task.script.ITaskScript;
import game.core.script.IScript;
import game.message.taskMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * 支线任务
 *
 * @author soko <xuchangming@haowan123.com>
 */
public class BranchTask extends Task {

    @Override
    public boolean finishTask(Player player, boolean isGM) {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.BranchTaskBaseScript);
        if (is instanceof ITaskScript) {
            return ((ITaskScript) is).onFinishTask(player, modelId, (int) getId(), getTaskRewardPer(), isGM, subType);
        }
        return false;
    }

    @Override
    public void changeTask(Player player) {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.BranchTaskBaseScript);
        if (is instanceof ITaskScript) {
            ((ITaskScript) is).changeTask(player, this, false);
        }
    }

    @Override
    public byte acqType() {
        return BRANCH_TASK;
    }

    public taskMessage.branchTaskInfo buildTaskInfo(Player player) {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.BranchTaskBaseScript);
        if (is instanceof ITaskScript) {
            return (taskMessage.branchTaskInfo) ((ITaskScript) is).buildTaskInfo(player, this);
        } else {
            return null;
        }
    }

    @Override
    public List<Integer> targetModels() {
        List<Integer> result = new ArrayList<>(2);
        Cfg_Task_branch_Bean model = CfgManager.getCfg_Task_branch_Container().getValueByKey(getModelId());
        if (model == null) {
            return result;
        }
        result.add(Manager.taskManager.deal().getTargetModelByType(getTargetType() , model.getDemand_value().get(0)));
        return result;
    }

    @Override
    public int targetNum() {
        Cfg_Task_branch_Bean model = CfgManager.getCfg_Task_branch_Container().getValueByKey(getModelId());
        if (model == null) {
            logger.error(getModelId() + "支线任务模型找不着 type =" + getTargetType());
            return 0;
        }
        return Manager.taskManager.deal().getTargetNumByType(getTargetType(),model.getDemand_value().get(0));
    }

    @Override
    public void release() {

    }
}
