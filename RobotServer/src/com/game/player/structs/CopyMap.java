package com.game.player.structs;

public class CopyMap {
    
    private int copyModelId;        //副本配置表id
    private int hasCount;           //剩余次数
    
    public int getCopyModelId(){
        return copyModelId;
    }
    
    public void setCopyModelId(int copyModelId){
        this.copyModelId = copyModelId;
    }
    
    public int getHasCount(){
        return hasCount;
    }
    
    public void setHasCount(int hasCount){
        this.hasCount = hasCount;
    }
    
    
}
