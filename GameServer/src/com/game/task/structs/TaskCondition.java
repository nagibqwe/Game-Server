/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.task.structs;

import com.game.player.structs.Player;

/**
 *
 * @author admin
 */
public class TaskCondition {
    //职业
    private int carrer;
    //最低等级
    private int minLevel;
    //最高等级
    private int maxLevel;
    //任务类型中的子类型
    private int subType;

    public int getSubType() {
        return subType;
    }

    public void setSubType(int subType) {
        this.subType = subType;
    }

    public int getCarrer() {
        return carrer;
    }

    public void setCarrer(int carrer) {
        this.carrer = carrer;
    }

    public int getMinLevel() {
        return minLevel;
    }

    public void setMinLevel(int minLevel) {
        this.minLevel = minLevel;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }

    public TaskCondition(int carrer, int minLevel, int maxLevel) {
        this.carrer = carrer;
        this.subType = 0;
        if(maxLevel < minLevel){
            this.maxLevel = minLevel;
            this.minLevel = maxLevel;
        }else {
            this.minLevel = minLevel;
            this.maxLevel = maxLevel;
        }
    }

    public TaskCondition(int carrer, int minLevel, int maxLevel, int subType) {
        this.carrer = carrer;
        this.subType = subType;
        if(maxLevel < minLevel){
            this.maxLevel = minLevel;
            this.minLevel = maxLevel;
        }else {
            this.minLevel = minLevel;
            this.maxLevel = maxLevel;
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + this.carrer;
        hash = 13 * hash + this.minLevel;
        hash = 13 * hash + this.maxLevel;
        hash = 13 * hash + this.subType;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TaskCondition other = (TaskCondition) obj;
        if (this.carrer != other.carrer) {
            return false;
        }
        if (this.minLevel != other.minLevel) {
            return false;
        }
        if (this.maxLevel != other.maxLevel) {
            return false;
        }
        if(this.subType != other.subType) {
            return false;
        }
        return true;
    }
    
    public boolean compare(Player player){
        if((this.carrer >= 0 && this.carrer < 100) && (this.carrer != player.getCareer())){
            return false;
        }
        if((this.minLevel <= player.getLevel()) && (player.getLevel() <= this.getMaxLevel())){
            return true;
        }
        return false;
    }

    /**
     * 用于日常任务比较
     * @param player
     * @return
     */
    public boolean subTypeTaskCompare(Player player, int subType){
        //9为通用 不为9 就要判断职业
        if(this.carrer != 9){
            if((this.carrer >= 0 && this.carrer < 100) && (this.carrer != player.getCareer())){
                return false;
            }
        }


        if((this.maxLevel < player.getLevel()) || (player.getLevel() < this.minLevel)){
            return false;
        }
        if(this.subType != subType) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "TaskCondition{" +
                ", carrer=" + carrer +
                ", minLevel=" + minLevel +
                ", maxLevel=" + maxLevel +
                ", subType=" + subType +
                '}';
    }

    /**
     * 
     * @param elseCondition
     * @return 
     */
    public boolean checkHaveCross(TaskCondition elseCondition){
        if(elseCondition.getMaxLevel() > getMaxLevel()){
            if(elseCondition.getMinLevel() > getMaxLevel()){
                return true;
            }
        }
        return false;
    }
    
}
