package com.game.couplefight.structs;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 玩家小组赛信息
 * @Auther: gouzhongliang
 * @Date: 2021/7/12 20:13
 */
public class TeamGroupsInfo {

    private int index;

    private int score;

    private int count;

    private int winCount;

    private int rate;
    /**小组赛 队伍所在的组*/
    @JsonIgnore
    private transient Group group;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public int getWinCount() {
        return winCount;
    }

    public void setWinCount(int winCount) {
        this.winCount = winCount;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}
