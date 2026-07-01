package com.game.openserverac.structs;

import java.util.HashMap;

public class OpenServerRevel {

    /**
     * ID
     */
    private int id;

    /**
     * 排名
     */
    private int rank;

    /**
     * 当前值
     */
    private int value;

    /**
     * 个人领奖状态
     */
    private HashMap<Integer, Integer> personState = new HashMap<>();


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public HashMap<Integer, Integer> getPersonState() {
        return personState;
    }

    public void setPersonState(HashMap<Integer, Integer> personState) {
        this.personState = personState;
    }
}
