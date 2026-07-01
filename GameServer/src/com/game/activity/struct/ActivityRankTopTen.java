package com.game.activity.struct;

/**
 * @author hewei@haowan123.com
 */
public class ActivityRankTopTen implements Comparable<ActivityRankTopTen> {

    /**
     * 用户id
     */
    private long userId;

    /**
     * 角色id
     */
    private long roleId;

    /**
     * 角色名
     */
    private String name;

    /**
     * 数量
     */
    private int num;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return userId + "_" + roleId + "_" + name + "_" + num;
    }

    @Override
    public int compareTo(ActivityRankTopTen o) {
        return o.getNum() - num;
    }

}
