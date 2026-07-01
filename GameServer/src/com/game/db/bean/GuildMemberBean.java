package com.game.db.bean;

import com.game.guild.structs.GuildMember;
import game.core.db.BaseBean;
import game.core.util.JsonUtils;
import game.core.util.StringUtils;

public class GuildMemberBean extends BaseBean {

    private long id;

    private int position;

    private long contribute;

    private long guildId;

    private long joinTime;

    private String datas;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public long getContribute() {
        return contribute;
    }

    public void setContribute(long contribute) {
        this.contribute = contribute;
    }

    public long getGuildId() {
        return guildId;
    }

    public void setGuildId(long guildId) {
        this.guildId = guildId;
    }

    public long getJoinTime() {
        return joinTime;
    }

    public void setJoinTime(long joinTime) {
        this.joinTime = joinTime;
    }

    public String getDatas() {
        return datas;
    }

    public void setDatas(String datas) {
        this.datas = datas;
    }

    public GuildMember toGuildMember() {
        if (StringUtils.isEmpty(datas)){
            datas = "{}";
        }
        GuildMember bean = JsonUtils.parseObject(datas, GuildMember.class);
        bean.setId(id);
        bean.setGuildId(guildId);
        bean.setPosition(position);
        bean.setJoinTime(joinTime);
        bean.setContribute(contribute);
        return bean;
    }
}
