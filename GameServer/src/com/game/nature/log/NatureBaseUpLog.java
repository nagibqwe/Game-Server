package com.game.nature.log;

import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.CommonLogBean;

/**
 * 为什么不加消耗的item的记录
 * 因为这里保存的actionid和使用物品时的actionid是同一个，通过这个actionid可以把两条日志关联起来
 * 减少db的io
 * */
public class NatureBaseUpLog extends CommonLogBean {
    /**
     * 玩家id
     * */
    private long playerId;
    /**
     * action id
     * */
    private long actionId;
    /**
     * 旧等级/旧id
     * */
    private int oldLevel;
    /**
     * 新等级/新id
     * */
    private int newLevel;
    /**
     * 升级成功后所激活的技能列表(可能激活多个，就用skillId1,skillId2...这样的格式存储)
     * */
    private String skillActivated;
    /**
     * 升级成功后所激活的模型列表（可能激活多个，就用modelId1,modelId2...这样的格式存储）
     * */
    private String modelActivated;

    @Override
    public TableCheckStepEnum getRollingStep() {
        return TableCheckStepEnum.MONTH;
    }

    @Log(logField = "playerId", fieldType = "bigint", index = "0")
    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    @Log(logField = "actionId", fieldType = "bigint", index = "0")
    public long getActionId() {
        return actionId;
    }

    public void setActionId(long actionId) {
        this.actionId = actionId;
    }

    @Log(logField = "oldLevel", fieldType = "int", index = "0")
    public int getOldLevel() {
        return oldLevel;
    }

    public void setOldLevel(int oldLevel) {
        this.oldLevel = oldLevel;
    }

    @Log(logField = "newLevel", fieldType = "int", index = "0")
    public int getNewLevel() {
        return newLevel;
    }

    public void setNewLevel(int newLevel) {
        this.newLevel = newLevel;
    }

    @Log(logField = "skillActivated", fieldType = "varchar(255)", index = "0")
    public String getSkillActivated() {
        return skillActivated;
    }

    public void setSkillActivated(String skillActivated) {
        this.skillActivated = skillActivated;
    }

    @Log(logField = "modelActivated", fieldType = "varchar(512)", index = "0")
    public String getModelActivated() {
        return modelActivated;
    }

    public void setModelActivated(String modelActivated) {
        this.modelActivated = modelActivated;
    }
}
