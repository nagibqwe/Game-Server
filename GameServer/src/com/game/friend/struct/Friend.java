package com.game.friend.struct;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.game.count.structs.Count;

/**
 * 好友关系
 */
public class Friend extends RelationInfo {

    /**
     * 是否为离线添加 1表示 是
     */
    private int isOffLineAdd;

    /**
     * 亲密度
     */
    private int intimacy = 0;



    public Friend() {
    }

    public int getIntimacy() {
        return intimacy;
    }

    public void setIntimacy(int intimacy) {
        this.intimacy = intimacy;
    }

    @Override
    public String toString() {
        return "Friend{" +
                "roleId=" + this.getRoleId() +
                ", info=" + this.getInfo().toString() +
                "intimacy=" + intimacy +
                '}';
    }

    public int getIsOffLineAdd() {
        return isOffLineAdd;
    }

    public void setIsOffLineAdd(int isOffLineAdd) {
        this.isOffLineAdd = isOffLineAdd;
    }



}
