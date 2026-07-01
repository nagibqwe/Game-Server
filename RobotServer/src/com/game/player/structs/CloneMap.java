package com.game.player.structs;

public class CloneMap {
    /**
     * 副本配置表ID
     */
    private int cloneModelId;
    /**
     * 剩余次数
     */
    private int hasCount;
    
    public int getCloneModelId(){
        return cloneModelId;
    }
    
    public void setCloneModelId(int cloneModelId){
        this.cloneModelId = cloneModelId;
    }
    
    public int getHasCount(){
        return hasCount;
    }
    
    public void setHasCount(int hasCount){
        this.hasCount = hasCount;
    }
    
    
}
