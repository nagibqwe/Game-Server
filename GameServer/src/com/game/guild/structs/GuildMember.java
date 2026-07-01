package com.game.guild.structs;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.game.count.structs.Count;
import com.game.count.structs.ICount;
import com.game.db.bean.GuildMemberBean;
import com.game.manager.Manager;
import com.game.map.structs.MapUtils;
import com.game.newfashion.manager.NewFashionManager;
import com.game.player.structs.PlayerWorldInfo;
import game.core.util.JsonUtils;
import game.message.GuildMessage;

import java.util.concurrent.ConcurrentHashMap;

public class GuildMember implements ICount {

    @JsonIgnore
    private transient long id;
    @JsonIgnore
    private transient int position;
    @JsonIgnore
    private transient long contribute;
    @JsonIgnore
    private transient long guildId;
    @JsonIgnore
    private transient long joinTime;
    @JsonIgnore
    private transient PlayerWorldInfo playerWorldInfo = new PlayerWorldInfo();

    ConcurrentHashMap<String, Count> counts = new ConcurrentHashMap<>();

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

    public PlayerWorldInfo getPlayerWorldInfo() {
        return playerWorldInfo;
    }

    public void setPlayerWorldInfo(PlayerWorldInfo playerWorldInfo) {
        this.playerWorldInfo = playerWorldInfo;
    }

    public void setCounts(ConcurrentHashMap<String, Count> counts) {
        this.counts = counts;
    }
    public long gainPower() {
        return getPlayerWorldInfo().getFightPower();
    }


    public GuildMemberBean toGuildMemberBean() {
        GuildMemberBean bean = new GuildMemberBean();
        bean.setId(id);
        bean.setGuildId(guildId);
        bean.setPosition(position);
        bean.setJoinTime(joinTime);
        bean.setContribute(contribute);
        bean.setDatas(JsonUtils.toJSONString(this));
        return bean;
    }

    public GuildMessage.GuildMemberInfo.Builder toGuildMemberMsg(Guild guild) {
        GuildMessage.GuildMemberInfo.Builder builder = GuildMessage.GuildMemberInfo.newBuilder();
        builder.setPosition(position);
        builder.setRoleId(id);
        builder.setName(playerWorldInfo.getRolename());
        builder.setCareer(playerWorldInfo.getCareer());
        builder.setLv(playerWorldInfo.getLevel());
        builder.setVip(playerWorldInfo.getPlayerVip());
        builder.setAllcontribute(contribute);
        builder.setLastOffTime(playerWorldInfo.getLastOffTime());
        builder.setFighting(playerWorldInfo.getFightPower());
        builder.setIsProxy(Manager.guildsManager.manager().isProxyChairMan(this, guild));
        //@todo 头像修改
        builder.setHead(MapUtils.getHead(playerWorldInfo));

        return builder;
    }

    /**
     * 获取计数数据
     *
     * @return
     */
    @Override
    public ConcurrentHashMap<String, Count> getCounts() {
        return counts;
    }
}
