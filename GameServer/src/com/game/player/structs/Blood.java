package com.game.player.structs;


/**
 * 血脉系统
 * @author zhaibiao
 */
public class Blood {
    
    private int order = 1 ;//当前阶数
    private int level ;//当前等级
    
    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

}
