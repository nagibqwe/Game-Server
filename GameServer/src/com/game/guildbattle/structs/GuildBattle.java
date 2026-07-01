package com.game.guildbattle.structs;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description
 * @auther lw
 * @create 2020-02-11 15:02
 */
public class GuildBattle {

    private long guildId;

    private int type;

    private int rank;

    private long mapId;

    private List<GuildBattleMember> memberList = new ArrayList<>();

    public long getGuildId() {
        return guildId;
    }

    public void setGuildId(long guildId) {
        this.guildId = guildId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public long getMapId() {
        return mapId;
    }

    public void setMapId(long mapId) {
        this.mapId = mapId;
    }

    public List<GuildBattleMember> getMemberList() {
        return memberList;
    }

    public void setMemberList(List<GuildBattleMember> memberList) {
        this.memberList = memberList;
    }
}
