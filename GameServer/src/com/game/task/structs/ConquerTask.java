package com.game.task.structs;

import com.data.CfgManager;
import com.data.bean.Cfg_Task_conquer_Bean;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.task.script.IConquerTask;
import com.game.task.script.ITaskScript;
import com.game.utils.RandomUtils;
import game.core.script.IScript;
import game.core.util.TimeUtils;
import game.message.taskMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * 帮会日常、周常任务的数据及部分逻辑实现
 *
 * @author soko
 */
public class ConquerTask extends Task {

    private int finishType = FINISH_TYPE_COMMON;

    @Override
    public byte acqType() {
        return GUILD_TASK;
    }

    @Override
    public int getFinishType() {
        return finishType;
    }

    @Override
    public void setFinishType(int finishType) {
        this.finishType = finishType;
    }

    /**
     * 初始化帮会日常、周常任务
     *
     * @param player 当前玩家 scroll_type 当前任务的颜色值0白色，1蓝色，2紫色
     * @return 是否接任务成功
     */
    public boolean initTask(Player player) {
        if (logger.isDebugEnabled()) {
            logger.debug("conquerTask init() - start");
        }

        List<Integer> taskIds = CanReceiveTaskHelper.getTaskIds(player, GUILD_TASK);
        if (taskIds.size() < 1) {
            logger.error("没有找到合适的帮会日常、周常任务，请策划同学注意一下： 玩家等级 + " + player.getLevel());
            return false;
        }
        int id = taskIds.get(RandomUtils.random(taskIds.size()));
        Cfg_Task_conquer_Bean model = CfgManager.getCfg_Task_conquer_Container().getValueByKey(id);
        if (model == null) {
            logger.error("没有找到合适的帮会日常、周常任务，请策划同学注意一下： task_id =  + " + id);
            return false;
        }
        setOwnerId(player.getId());
        modelId = model.getId();
        if (logger.isDebugEnabled()) {
            logger.debug("init() - end");
        }
        player.getConquerTaskCount().put(model.getConquer_subtype(), player.getConquerTaskCount().get(model.getConquer_subtype()) + 1);
        setIsReceive(true);
        setFinish(false);
        setTaskRewardPer(1);

        player.getConquerTaskTime().put(model.getConquer_subtype(), TimeUtils.Time());
        player.getCurConquerTasks().put(model.getConquer_subtype(), this);
        return true;
    }

    @Override
    public boolean finishTask(Player player, boolean isGM) {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.ConquerTaskBaseScript);
        if (is instanceof ITaskScript) {
            return ((ITaskScript) is).onFinishTask(player, modelId, (int) getId(), getTaskRewardPer(), isGM, subType);
        }
        return false;
    }

    public void noResumeFinishTask(Player player, int finishType) {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.ConquerTaskBaseScript);
        if (is instanceof IConquerTask) {
            ((IConquerTask) is).noResumeFinishTask(player, this, finishType);
        }
    }

    @Override
    public void changeTask(Player player) {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.ConquerTaskBaseScript);
        if (is instanceof ITaskScript) {
            ((ITaskScript) is).changeTask(player, this, false);
        }
    }

    public taskMessage.conquerTaskInfo buildTaskInfo(Player player) {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.ConquerTaskBaseScript);
        if (is instanceof ITaskScript) {
            return (taskMessage.conquerTaskInfo) ((ITaskScript) is).buildTaskInfo(player, this);
        } else {
            return null;
        }
    }

    @Override
    public List<Integer> targetModels() {
        List<Integer> result = new ArrayList<>(2);
        Cfg_Task_conquer_Bean model = CfgManager.getCfg_Task_conquer_Container().getValueByKey(modelId);
        if (model == null) {
            logger.info(getModelId() + "帮会日常、周常任务模型找不着 type =" + getTargetType());
            return result;
        }

        result.add(Manager.taskManager.deal().getTargetModelByType(getTargetType(), model.getGoal_npc()));
        return result;
    }

    @Override
    public int targetNum() {
        Cfg_Task_conquer_Bean model = CfgManager.getCfg_Task_conquer_Container().getValueByKey(modelId);
        if (model == null) {
            logger.error(getModelId() + "帮会日常、周常任务模型找不着 type =" + getTargetType());
            return 0;
        }
        return Manager.taskManager.deal().getTargetNumByType(getTargetType(), model.getGoal_npc());
    }

    @Override
    public void release() {

    }
}
