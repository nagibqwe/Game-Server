package com.game.task.structs;

import com.data.CfgManager;
import com.data.bean.Cfg_Task_gender_Bean;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.task.script.ITaskScript;
import game.core.script.IScript;
import game.message.taskMessage;

import java.util.ArrayList;
import java.util.List;

public class GenderTask extends Task {

    public boolean initTask(Player player, int modelId) {
        Cfg_Task_gender_Bean bean = CfgManager.getCfg_Task_gender_Container().getValueByKey(modelId);
        if (bean == null) {
            logger.error("转职任务配置出错 " + modelId);
            return false;
        }
        if (bean.getGender() != 9 && player.getCareer() != bean.getGender()) {
            return false;
        }
        if (player.getLevel() < bean.getLevel()) {
            logger.info("等级不足 modelId={} player={}",modelId, player);
            return false;
        }
        if (player.getXsGrade() < bean.getGenderClass()) {
            logger.info("洗髓阶数不足");
            return false;
        }
        setModelId(modelId);
        setIsReceive(true);
        setTargetMap(bean.getPathMap());
        setTargetType(bean.getTask_type());
        setOwnerId(player.getId());
        setFinish(false);
        setCurNum(0);
        Manager.taskManager.deal().fullTaskOptionParam(this, bean.getGoal_npc(), bean.getTask_x_z());
        return true;
    }

    public taskMessage.genderTaskInfo buildTaskInfo(Player player) {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.GenderTaskBaseScript);
        return (taskMessage.genderTaskInfo) ((ITaskScript) is).buildTaskInfo(player, this);
    }

    @Override
    public byte acqType() {
        return GENDER_TASK;
    }

    @Override
    public boolean finishTask(Player player, boolean isGM) {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.GenderTaskBaseScript);
        if (is instanceof ITaskScript) {
            return ((ITaskScript) is).onFinishTask(player, (int) getId(), modelId, 1, isGM, subType);
        }
        return false;
    }

    @Override
    public void changeTask(Player player) {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.GenderTaskBaseScript);
        if (is instanceof ITaskScript) {
            ((ITaskScript) is).changeTask(player, this, false);
        }
    }

    @Override
    public List<Integer> targetModels() {
        List<Integer> result = new ArrayList<>(2);
        Cfg_Task_gender_Bean model = CfgManager.getCfg_Task_gender_Container().getValueByKey(modelId);
        if (model == null) {
            return result;
        }
        result.add(Manager.taskManager.deal().getTargetModelByType(getTargetType() , model.getGoal_npc()));
        return result;
    }

    @Override
    public int targetNum() {
        Cfg_Task_gender_Bean model = CfgManager.getCfg_Task_gender_Container().getValueByKey(modelId);
        if (model == null) {
            return 0;
        }
        return Manager.taskManager.deal().getTargetNumByType(getTargetType(), model.getGoal_npc());
    }

    @Override
    public void release() {

    }
}
