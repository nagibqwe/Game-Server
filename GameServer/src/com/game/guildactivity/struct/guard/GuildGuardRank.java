package com.game.guildactivity.struct.guard;

/**
 * 守护宗派伤害排名
 */
public class GuildGuardRank {
    /**
     * 排名
     */
    private int rank;
    /**
     * 角色唯一id
     */
    private long roleId;
    /**
     * 名字
     */
    private String name;
    /**
     * 伤害
     */
    private long harm;

    //*******************************方法*******************************************

    public GuildGuardRank() {

    }

    public GuildGuardRank(long roleId, String name) {
        this.roleId = roleId;
        this.name = name;
    }

    //************************************getter and setter**********************************************************

    /**
     * 获取 排名
     *
     * @return rank 排名
     */
    public int getRank() {
        return this.rank;
    }

    /**
     * 设置 排名
     *
     * @param rank 排名
     */
    public void setRank(int rank) {
        this.rank = rank;
    }

    /**
     * 获取 角色唯一id
     *
     * @return roleId 角色唯一id
     */
    public long getRoleId() {
        return this.roleId;
    }

    /**
     * 设置 角色唯一id
     *
     * @param roleId 角色唯一id
     */
    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    /**
     * 获取 名字
     *
     * @return name 名字
     */
    public String getName() {
        return this.name;
    }

    /**
     * 设置 名字
     *
     * @param name 名字
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取 伤害
     *
     * @return harm 伤害
     */
    public long getHarm() {
        return this.harm;
    }

    /**
     * 设置 伤害
     *
     * @param harm 伤害
     */
    public void setHarm(long harm) {
        this.harm = harm;
    }
}
