package com.game.couplefight.structs;

/**
 * @Auther: gouzhongliang
 * @Date: 2021/7/12 20:13
 */
public class TeamTrialsInfo {
    /**参与次数*/
    private int count;
    /**胜利次数*/
    private int winCount;
    /**积分*/
    private int score;
    /**胜率*/
    private int rate;
    /**排名*/
    private int rank;
    /**匹配开始时间*/
    private transient long time;
    /**最后一次匹配到的战队*/
    private transient long lastMatchTeam;
    /**匹配成功后是否同意进入战斗m*/
    private transient boolean wjoin;
    /**匹配成功后是否同意进入战斗w*/
    private transient boolean mjoin;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getWinCount() {
        return winCount;
    }

    public void setWinCount(int winCount) {
        this.winCount = winCount;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getLastMatchTeam() {
        return lastMatchTeam;
    }

    public void setLastMatchTeam(long lastMatchTeam) {
        this.lastMatchTeam = lastMatchTeam;
    }

    public boolean isWjoin() {
        return wjoin;
    }

    public void setWjoin(boolean wjoin) {
        this.wjoin = wjoin;
    }

    public boolean isMjoin() {
        return mjoin;
    }

    public void setMjoin(boolean mjoin) {
        this.mjoin = mjoin;
    }
}
