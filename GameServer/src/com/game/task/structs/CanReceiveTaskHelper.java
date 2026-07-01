/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.task.structs;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.task.manager.TaskManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *可接任务
 * @author admin
 */
public class CanReceiveTaskHelper<T> extends Task{
    
    private int [] levelBetween = new int [2];
    
    private int taskId;

    private int type = -1;
    // 用来装数据库对应的任务具体实体
    private List<T> entity = new ArrayList<>();
    
    private List<CanReceiveTask> canReceiveTasks = new ArrayList<>();

    public List<CanReceiveTask> getCanReceiveTasks() {
        return canReceiveTasks;
    }

    public void setCanReceiveTasks(List<CanReceiveTask> canReceiveTasks) {
        this.canReceiveTasks = canReceiveTasks;
    }

    public List<T> getEntity() {
        return entity;
    }

    public void setEntity(List<T> entity) {
        this.entity = entity;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }
    
    public void setType(int type){
        this.type = type;
    }
    
    public int getType(){
        return type;
    }
    
    public void setLevelBetween(int minLevel,int maxLevel){
        levelBetween[0] = minLevel;
        levelBetween[1] = maxLevel;
    }
    
    public boolean playerIsLevelBetween(Player player){
        if(player.getLevel()<=levelBetween[1] && player.getLevel() >=levelBetween[0]){
            return true;
        }
        return false;
    }
    
    public void addT(T t){
        entity.add(t);
    }
    
    public List<T> getT(){
        return entity;
    }
    
    public static List<Integer> getTaskIds(Player player, int type){
        Map<TaskCondition, List<Integer>> temp = Manager.taskManager.getCanReceiveTasks().get(type);
        List<Integer> taskIds = new ArrayList<>();
        TaskCondition condition = new TaskCondition(player.getCareer(), player.getLevel(),  player.getLevel());
        if(temp.containsKey(condition)){
            return temp.get(condition);
        }else {
            return taskIds;
        }
    }
    
    @Override
    public boolean finishTask(Player player, boolean isGM) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void changeTask(Player player) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public byte acqType() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Integer> targetModels() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int targetNum() {
        return 0;
    }

    @Override
    public void release() {

    }
}
