package com.game.vip.structs;

/**
 * VIP宝珠
 */
public class VipPearl {
    /**
     * 宝珠状态 0未佩戴 1时限宝珠 2永久宝珠
     */
    private int state;
    /**
     * 过期的时间戳
     */
    private int deadline;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getDeadline() {
        return deadline;
    }

    public void setDeadline(int deadline) {
        this.deadline = deadline;
    }

    public boolean canFree(){
        if(state==1||state==2){
            return true;
        }
        return false;
    }
}
