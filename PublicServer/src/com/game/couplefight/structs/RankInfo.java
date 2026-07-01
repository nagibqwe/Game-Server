package com.game.couplefight.structs;

/**
 * @Auther: gouzhongliang
 * @Date: 2021/11/10 15:04
 */
public class RankInfo {

    private long id;

    private int score;

    public RankInfo(){}

    public RankInfo(long id, int score){
        this.id = id;
        this.score = score;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
