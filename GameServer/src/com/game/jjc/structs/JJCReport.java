package com.game.jjc.structs;

/**
 *
 * @author zenghai
 */
public class JJCReport {

    private boolean succ;  //是否胜利
    private String target; //挑战对象
    private int lastRank;  //上一次排名
    private int rank;  // 排名
    private int time; //时间
    private boolean tiaozhao;//是否挑战

    public int getLastRank() {
        return lastRank;
    }

    public void setLastRank(int lastRank) {
        this.lastRank = lastRank;
    }

    public boolean isSucc() {
        return succ;
    }

    public void setSucc(boolean succ) {
        this.succ = succ;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
    
    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public boolean isTiaozhao() {
        return tiaozhao;
    }

    public void setTiaozhao(boolean tiaozhao) {
        this.tiaozhao = tiaozhao;
    }
    
}
