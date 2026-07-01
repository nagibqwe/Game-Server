package com.game.statestifle.structs;

/**
 * 器灵信息
 */
public class SoulSpiritInfo {

    private int id;                 //id

    private int promoteLv;          //晋升等级

    private long promoteProgress;   //晋升进度

    private int evolveLv;           //进化等级

    public SoulSpiritInfo() {}

    public SoulSpiritInfo(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPromoteLv() {
        return promoteLv;
    }

    public void setPromoteLv(int promoteLv) {
        this.promoteLv = promoteLv;
    }

    public long getPromoteProgress() {
        return promoteProgress;
    }

    public void setPromoteProgress(long promoteProgress) {
        this.promoteProgress = promoteProgress;
    }

    public int getEvolveLv() {
        return evolveLv;
    }

    public void setEvolveLv(int evolveLv) {
        this.evolveLv = evolveLv;
    }
}
