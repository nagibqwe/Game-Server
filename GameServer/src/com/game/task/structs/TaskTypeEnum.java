/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.task.structs;

import com.game.script.structs.ScriptEnum;
import java.util.HashMap;
import java.util.Map;

/**
 * 任务类型枚举
 * @author admin
 */
public enum TaskTypeEnum {

    MAIN_TASK(0, "主线任务", ScriptEnum.MainTaskBaseScript),
    DAILY_TASK(1, "首席任务", ScriptEnum.DailyTaskBaseScript),
    CONQUER_TASK(2, "帮会任务", ScriptEnum.ConquerTaskBaseScript),
    BRANCH_TASK(3, "支线任务", ScriptEnum.BranchTaskBaseScript),
    BATTLE_TASK(7, "战场任务", ScriptEnum.BattleFiledTaskBaseScript),
    GENDER_TASK(8, "转职任务", ScriptEnum.GenderTaskBaseScript),
    GUIDE_TASK(9, "引导任务", ScriptEnum.GuideTaskBaseScript);

    private int type;
    private String name;
    private int scriptId;

    public static final Map<Integer, TaskTypeEnum> taskTypes = new HashMap<>();

    static {
        for (TaskTypeEnum taskType : TaskTypeEnum.values()) {
            taskTypes.put(taskType.getType(), taskType);
        }
    }

    private TaskTypeEnum(int type, String name, int scriptId) {
        this.type = type;
        this.name = name;
        this.scriptId = scriptId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScriptId() {
        return scriptId;
    }

    public void setScriptId(int scriptId) {
        this.scriptId = scriptId;
    }

    @Override
    public String toString() {
        return super.toString(); //To change body of generated methods, choose Tools | Templates.
    }
}
