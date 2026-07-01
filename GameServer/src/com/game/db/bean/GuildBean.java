package com.game.db.bean;

import com.game.guild.structs.Guild;
import com.game.manager.Manager;
import game.core.db.BaseBean;
import game.core.util.JsonUtils;
import game.core.util.VersionUpdateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GuildBean extends BaseBean {
    private final static Logger log = LogManager.getLogger(GuildBean.class);

    /**
     * 公会id
     */
    private long guildId;
    /**
     * 帮会名
     */
    private String guildName;
    /**
     * 会长id
     */
    private long chairmanId;
    /**
     * 创建时间
     */
    private int createTime;
    /**
     * 当前基地等级
     */
    private int level;
    /**
     * 当前建设度
     */
    private long buildValue;
    /**
     * 其他帮会数据
     */
    private String datas;
    /**
     * 公会的红包数据
     */
    private String guildredpacket = "";


    public long getGuildId() {
        return guildId;
    }

    public void setGuildId(long guildId) {
        this.guildId = guildId;
    }

    public String getGuildName() {
        return guildName;
    }

    public void setGuildName(String guildName) {
        this.guildName = guildName;
    }

    public long getChairmanId() {
        return chairmanId;
    }

    public void setChairmanId(long chairmanId) {
        this.chairmanId = chairmanId;
    }

    public int getCreateTime() {
        return createTime;
    }

    public void setCreateTime(int createTime) {
        this.createTime = createTime;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public long getBuildValue() {
        return buildValue;
    }

    public void setBuildValue(long buildValue) {
        this.buildValue = buildValue;
    }

    public String getDatas() {
        return datas;
    }

    public void setDatas(String datas) {
        this.datas = datas;
    }

    public String getGuildredpacket() {
        return guildredpacket;
    }

    public void setGuildredpacket(String guildredpacket) {
        this.guildredpacket = guildredpacket;
    }

    public Guild toGuild(GuildBean bean) throws Exception {
        Guild guild = JsonUtils.parseObject(VersionUpdateUtil.dataLoad(bean.getDatas()), Guild.class);
        guild.setCreateTime(createTime);
        guild.setId(guildId);
        guild.setName(guildName);
        guild.setExp(buildValue);
        Manager.redPacketManager.setGuildRPLog(bean.getGuildId(), bean.getGuildredpacket());
        return guild;

    }
}
