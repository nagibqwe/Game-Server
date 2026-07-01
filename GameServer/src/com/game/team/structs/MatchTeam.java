package com.game.team.structs;

public class MatchTeam {

    private long teamId;

    private int targetId;

    private int num;

    private int matchOverTime;

    private boolean finish;

    public MatchTeam(long teamId, int targetId, int num) {
        this.teamId = teamId;
        this.targetId = targetId;
        this.num = num;
    }

    public long getTeamId() {
        return teamId;
    }

    public void setTeamId(long teamId) {
        this.teamId = teamId;
    }

    public int getTargetId() {
        return targetId;
    }

    public void setTargetId(int targetId) {
        this.targetId = targetId;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getMatchOverTime() {
        return matchOverTime;
    }

    public void setMatchOverTime(int matchOverTime) {
        this.matchOverTime = matchOverTime;
    }

    public boolean isFinish() {
        return finish;
    }

    public void setFinish(boolean finish) {
        this.finish = finish;
    }
}
