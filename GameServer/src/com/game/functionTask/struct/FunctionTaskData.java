package com.game.functionTask.struct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 功能任务数据
 * @Auther: gouzhongliang
 * @Date: 2021/9/28 15:03
 */
public class FunctionTaskData {
    private int days;//开服天数
    private List<Integer> rechargeIds = new ArrayList<>();//
    private Map<Integer,FunctionTask> tasks = new HashMap<>();//功能任务

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public List<Integer> getRechargeIds() {
        return rechargeIds;
    }

    public void setRechargeIds(List<Integer> rechargeIds) {
        this.rechargeIds = rechargeIds;
    }

    public Map<Integer, FunctionTask> getTasks() {
        return tasks;
    }

    public void setTasks(Map<Integer, FunctionTask> tasks) {
        this.tasks = tasks;
    }
}
