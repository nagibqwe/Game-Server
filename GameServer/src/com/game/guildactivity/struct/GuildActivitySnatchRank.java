package com.game.guildactivity.struct;

public class GuildActivitySnatchRank {

    public GuildActivitySnatchRank(long playerId, String name, float ratio) {
        this.playerId = playerId;
        this.name = name;
        this.ratio = ratio;
    }

    /**
     * 玩家唯一id
     */
    private long playerId;

    /**
     * 玩家名字
     */
    private String name;

    /**
     * 玩家伤害
     */
    private long harm;

    /**
     * 排名
     */
    private int rank;

    /**
     * 奖励倍率
     */
    private float ratio;

    /**
     * 获取 玩家唯一id
     *
     * @return playerId 玩家唯一id
     */
    public long getPlayerId() {
        return this.playerId;
    }

    /**
     * 设置 玩家唯一id
     *
     * @param playerId 玩家唯一id
     */
    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    /**
     * 获取 玩家名字
     *
     * @return name 玩家名字
     */
    public String getName() {
        return this.name;
    }

    /**
     * 设置 玩家名字
     *
     * @param name 玩家名字
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取 玩家伤害
     *
     * @return harm 玩家伤害
     */
    public long getHarm() {
        return this.harm;
    }

    /**
     * 设置 玩家伤害
     *
     * @param harm 玩家伤害
     */
    public void setHarm(long harm) {
        this.harm = harm;
    }

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
     * 获取 奖励倍率
     *
     * @return ratio 奖励倍率
     */
    public float getRatio() {
        return this.ratio;
    }

    /**
     * 设置 奖励倍率
     *
     * @param ratio 奖励倍率
     */
    public void setRatio(float ratio) {
        this.ratio = ratio;
    }
}
