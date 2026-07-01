package com.game.guildcrossfud.struct;

/**
 * @Desc TODO  计算福地归属
 * @Date 2021/2/24 17:48
 * @Auth ZUncle
 */
public class FudCalcOwn {
    int camp;
    int score;
    int roleTotalScore;
    int kill;
    int serverId;

    public int getCamp() {
        return camp;
    }

    public void setCamp(int camp) {
        this.camp = camp;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getRoleTotalScore() {
        return roleTotalScore;
    }

    public void setRoleTotalScore(int roleTotalScore) {
        this.roleTotalScore = roleTotalScore;
    }

    public int getKill() {
        return kill;
    }

    public void setKill(int kill) {
        this.kill = kill;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }
}
