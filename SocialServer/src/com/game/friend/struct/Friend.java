package com.game.friend.struct;

/**
 * 好友关系
 */
public class Friend extends RelationInfo {

    /**
     * 亲密度
     */
    private int intimacy = 0;

    public int getIntimacy() {
        return intimacy;
    }

    public void setIntimacy(int intimacy) {
        this.intimacy = intimacy;
    }

    @Override
    public String toString() {
        return "Friend{" +
//                "roleId=" + this.getRoleId() +
//                ", info=" + this.getInfo().toString() +
                "intimacy=" + intimacy +
                '}';
    }
}
