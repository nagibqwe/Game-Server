package com.game.functionTask.manager;

import com.game.functionTask.script.IFunctionTaskScript;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import game.core.script.IScript;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * 核心功能任务
 * @Auther: gouzhongliang
 * @Date: 2021/9/28 10:08
 */
public enum FunctionTaskManager {

    instance;

    private static final Logger log = LogManager.getLogger(FunctionTaskManager.class);

    /**当前任务id*/
    private List<Integer> taskIds = new ArrayList<>();
    /**当前充值id*/
    private Integer rechargeId;

    public List<Integer> getTaskIds() {
        return taskIds;
    }

    public void setTaskIds(List<Integer> taskIds) {
        this.taskIds = taskIds;
    }

    public Integer getRechargeId() {
        return rechargeId;
    }

    public void setRechargeId(Integer rechargeId) {
        this.rechargeId = rechargeId;
    }

    public IFunctionTaskScript getScript(){
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.FunctionTaskScript);
        if(is instanceof IFunctionTaskScript){
            return (IFunctionTaskScript)is;
        }
        log.error("没有实现脚本->IFunctionTaskScript");
        return null;
    }

    /**
     * 初始化
     */
    public void init(){
        getScript().init();
    }

    /**
     * 获取奖励
     * @param player
     * @param id
     */
    public void getAward(Player player, int id) {

    }
}
