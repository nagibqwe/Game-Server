package com.game.guildbattle.structs;

import java.util.HashMap;

/**
 * @Description
 * @auther lw
 * @create 2020-02-13 15:48
 */
public class GuildBattleData implements Comparable<GuildBattleData>{

    private long guildId;
    /**
     * 0 攻防 1守方
     */
    private int camp;

    /**
     * 伤害统计
     */
    private long allHurt;

    /**
     * 初始化和复活坐标索引
     */
    private int index;

    /**
     * 当前公会排名
     */
    private int rank;
    /**
     * 玩家伤害统计
     */
    private HashMap<Long, GuildBattleMember> memberList = new HashMap<>();

    public long getGuildId() {
        return guildId;
    }

    public void setGuildId(long guildId) {
        this.guildId = guildId;
    }

    public int getCamp() {
        return camp;
    }

    public void setCamp(int camp) {
        this.camp = camp;
    }

    public long getAllHurt() {
        return allHurt;
    }

    public void setAllHurt(long allHurt) {
        this.allHurt = allHurt;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public HashMap<Long, GuildBattleMember> getMemberList() {
        return memberList;
    }

    public void setMemberList(HashMap<Long, GuildBattleMember> memberList) {
        this.memberList = memberList;
    }

    @Override
    public int compareTo(GuildBattleData o) {
        if (o.getAllHurt() > allHurt) {
            return 1;
        }

        if (allHurt == o.getAllHurt()) {
            return 0;
        }

        return -1;
    }
}
