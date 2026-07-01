package com.game.treasurehunt.struct;

import game.core.util.IDConfigUtil;

/**
 * Created by 瞿冰冰
 * 2019/9/17
 */
public class HuntReward {

    private int id;

    private int num;

    private boolean bind;

    private int type;//1 表示道具 2 表示仙魄

    private long uid = 0;//唯一ID

    public boolean isBind() {
        return bind;
    }

    public void setBind(boolean bind) {
        this.bind = bind;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public static HuntReward createHuntReward(Integer id, Integer num, boolean bind, int type) {
        HuntReward huntReward = new HuntReward();
        huntReward.setBind(bind);
        huntReward.setId(id);
        huntReward.setNum(num);
        huntReward.setType(type);
        huntReward.setUid(IDConfigUtil.getId());
        return huntReward;
    }
}
