package com.game.marriage.struct;

/**
 * @Desc TODO  仙缘 心锁
 * @Date 2020/8/6 16:32
 * @Auth ZUncle
 */
public class MarryLoveLock {

    int stage = 0;          //阶
    int grade = 0;          //级
    int exp;                //心锁经验
    boolean open = false;   //是否解锁

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public int getStage() {
        return stage;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }
}
