package com.game.guildbattle.structs;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description
 * @auther lw
 * @create 2020-02-11 15:02
 */
public class GuildBattleWin {

    private long guildId;

    /**
     * 0:连赢 1:终结
     */
    private int type;

    private int num;

    private int hasGet;

    private String beGuildName = "";

    private List<Long> roleIds = new ArrayList<>();

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

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getHasGet() {
        return hasGet;
    }

    public void setHasGet(int hasGet) {
        this.hasGet = hasGet;
    }

    public String getBeGuildName() {
        return beGuildName;
    }

    public void setBeGuildName(String beGuildName) {
        this.beGuildName = beGuildName;
    }

    public List<Long> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<Long> roleIds) {
        this.roleIds = roleIds;
    }
}
