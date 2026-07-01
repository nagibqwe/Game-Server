package com.game.task.structs;

import com.data.CfgManager;
import com.data.bean.Cfg_Task_Bean;
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

public class MainTask extends Task {

    private void init() {
        if (logger.isDebugEnabled()) {
            logger.debug("init() - start" + getModelId());
        }
        Cfg_Task_Bean taskmodel = CfgManager.getCfg_Task_Container().getValueByKey(getModelId());
        if (taskmodel == null) {
            return;
        }
        setTargetType(taskmodel.getType());
        //设置任务需要的玩家等级
        int needlevel = 1;
        int needNum = taskmodel.getTarget().get(1);
        setTargetMap(taskmodel.getPathMap());
        if (taskmodel.getType() == Task.ACTION_TYPE_NEED_LEVEL || taskmodel.getType() == Task.ACTION_TYPE_VIP_STATE_BREAK) {
            needlevel = needNum;
        }
        int degree = 0;
        setNeedDegree(degree);
        setNeedLevel(needlevel);
        if (logger.isDebugEnabled()) {
            logger.debug("init() - end" + getModelId());
        }
    }

    @Override
    public byte acqType() {
        return MAIN_TASK;
    }

    /**
     * 返回任务的类型
     * @return
     */
    public int getTaskType() {
        Cfg_Task_Bean model = CfgManager.getCfg_Task_Container().getValueByKey(modelId);
        if (model == null) {
            logger.error(getModelId() + "主线任务模型找不着");//, new NullPointerException());
            return -1;
        }
        return model.getType();
    }

    public void initTask(Player player, int modelId) {
        setModelId(modelId);
        setOwnerId(player.getId());
        init();
        player.setCurMainTaskId(modelId);
        player.getCurMainTasks().add(this);
    }

    @Override
    public boolean checkFinish(boolean isPromp, Player player) {
        return super.checkFinish(isPromp, player);
    }

    @Override
    public boolean finishTask(Player player, boolean isGM) {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.MainTaskBaseScript);
        if (is instanceof ITaskScript) {
            return ((ITaskScript) is).onFinishTask(player, modelId, modelId, 1, isGM, subType);
        }
        return false;
    }

    @Override
    public void changeTask(Player player) {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.MainTaskBaseScript);
        if (is instanceof ITaskScript) {
            ((ITaskScript) is).changeTask(player, this, false);
        }
    }

    public taskMessage.mainTaskInfo buildTaskInfo(Player player) {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.MainTaskBaseScript);
        if (is instanceof ITaskScript) {
            return (taskMessage.mainTaskInfo) ((ITaskScript) is).buildTaskInfo(player, this);
        }
        return null;
    }

    @Override
    public List<Integer> targetModels() {
        List<Integer> result = new ArrayList<>(2);
        Cfg_Task_Bean model = CfgManager.getCfg_Task_Container().getValueByKey(modelId);
        if (model == null) {
            logger.error(getModelId() + "主线任务模型找不着 model =");
            return result;
        }
        result.add(Manager.taskManager.deal().getTargetModelByType(getTaskType(), model.getTarget()));
        return result;
    }

    @Override
    public int targetNum() {
        Cfg_Task_Bean model = CfgManager.getCfg_Task_Container().getValueByKey(modelId);
        if (model == null) {
            logger.error(getModelId() + "主线任务模型找不着 model =");
            return 0;
        }
        return Manager.taskManager.deal().getTargetNumByType(getTaskType(), model.getTarget());
    }

    @Override
    public void release() {

    }
}
