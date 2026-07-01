package com.game.couplefight.structs;

/**
 * 玩家冠军赛信息
 * @Auther: gouzhongliang
 * @Date: 2021/7/12 20:14
 */
public class TeamChampionsInfo {

    /**1天榜 2地榜*/
    private int type;
    /**积分*/
    private int score;
    /**排名*/
    private int rank;

    public TeamChampionsInfo(){}

    public TeamChampionsInfo(int type){
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}
