package com.game.guild.structs;

import com.data.Global;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.game.cooldown.structs.Cooldown;
import com.game.cooldown.structs.ICoolDown;
import com.game.count.structs.Count;
import com.game.count.structs.ICount;
import com.game.db.bean.GuildBean;
import com.game.guildactivity.struct.GuildActivityInfo;
import com.game.manager.Manager;
import game.core.util.JsonUtils;
import game.core.util.VersionUpdateUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class Guild implements ICoolDown, ICount {

    /**
     * 公会ID
     */
    private long id;

    /**
     * 公会图标
     */
    private int icon;

    /**
     * 公会名字
     */
    private String name;

    /**
     * 宣言
     */
    private String notice = "";

    /**
     * 公会创建时间
     */
    private int createTime;

    /**
     * 是否需要管理员同意
     */
    private boolean isAutoApply;

    /**
     * 加入限制等级
     */
    private int limitLv;

    /**
     * 加入限制战力
     */
    private long limitFightPoint;

    /**
     * 福地添加人数上限
     */
    private boolean fudLimit;

    /**
     * 会长建设度
     */
    private long exp;

    /**
     * 公会建筑等级
     */
    private HashMap<Integer, Integer> constructions = new HashMap<>();

    /**
     * 公会日志
     */
    private List<GuildLog> log_list = new CopyOnWriteArrayList<>();

    /**
     * 工会升级通知日志
     */
    private HashSet<Long> noticeList = new HashSet<>();

    /**
     * 仙盟宝箱奖励
     */
    private HashMap<Long, GuildGift>  guildGift = new HashMap<>();

    private List<GuildGift> giftHistory = new ArrayList<>();

    /**
     * 公会成员
     */
    @JsonIgnore
    private transient ConcurrentHashMap<Long, GuildMember> members = new ConcurrentHashMap<>();

    /**
     * 公会申请
     */
    @JsonIgnore
    private transient ArrayList<Long> requestList = new ArrayList<>();

    /**
     * 公会会长
     */
    @JsonIgnore
    private transient GuildMember chairMan = null;

    /**
     * 工会战力
     */
    @JsonIgnore
    private transient long fightPower;

    @JsonIgnore
    private final transient ConcurrentHashMap<String, Cooldown> cooldowns = new ConcurrentHashMap<>();

    /**
     * 获得称号的排名玩家,从1开始
     */
    private ConcurrentHashMap<Long, Integer> titleRankMap = new ConcurrentHashMap<>();

    //计数列表
    private ConcurrentHashMap<String, Count> counts = new ConcurrentHashMap<>();

    /**
     * 代理公会会长
     */
    @JsonIgnore
    private transient GuildMember proxyChairMan = null;

    /**
     * 代理公会时间
     */
    @JsonIgnore
    private transient long proxyTime;

    /**
     * 维修资金连续几次扣不成功
     */
    private int times = 0;

    //**************************getter and setter**********************************

    public List<GuildGift> getGiftHistory() {
        return giftHistory;
    }

    public void setGiftHistory(List<GuildGift> giftHistory) {
        this.giftHistory = giftHistory;
    }

    public boolean isFudLimit() {
        return fudLimit;
    }

    public void setFudLimit(boolean fudLimit) {
        this.fudLimit = fudLimit;
    }

    public HashMap<Long, GuildGift> getGuildGift() {
        return guildGift;
    }

    public void setGuildGift(HashMap<Long, GuildGift> guildGift) {
        this.guildGift = guildGift;
    }

    public long getFightPower() {
        return fightPower;
    }

    public void setFightPower(long fightPower) {
        this.fightPower = fightPower;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public int getCreateTime() {
        return createTime;
    }

    public void setCreateTime(int createTime) {
        this.createTime = createTime;
    }

    public boolean isAutoApply() {
        return isAutoApply;
    }

    public void setAutoApply(boolean autoApply) {
        isAutoApply = autoApply;
    }

    public int getLimitLv() {
        return limitLv;
    }

    public void setLimitLv(int limitLv) {
        this.limitLv = limitLv;
    }

    public long getLimitFightPoint() {
        return limitFightPoint;
    }

    public void setLimitFightPoint(long limitFightPoint) {
        this.limitFightPoint = limitFightPoint;
    }

    public long getExp() {
        return exp;
    }

    public void setExp(long exp) {
        this.exp = exp;
    }

    public HashMap<Integer, Integer> getConstructions() {
        return constructions;
    }

    public void setConstructions(HashMap<Integer, Integer> constructions) {
        this.constructions = constructions;
    }

    public ConcurrentHashMap<Long, GuildMember> getMembers() {
        return members;
    }

    public void setMembers(ConcurrentHashMap<Long, GuildMember> members) {
        this.members = members;
    }

    public ArrayList<Long> getRequestList() {
        return requestList;
    }

    public void setRequestList(ArrayList<Long> requestList) {
        this.requestList = requestList;
    }

    public GuildMember getChairMan() {
        return chairMan;
    }

    public void setChairMan(GuildMember chairMan) {
        this.chairMan = chairMan;
    }

    public GuildMember getProxyChairMan() {
        return proxyChairMan;
    }

    public void setProxyChairMan(GuildMember proxyChairMan) {
        this.proxyChairMan = proxyChairMan;
    }

    public long getProxyTime() {
        return proxyTime;
    }

    public void setProxyTime(long proxyTime) {
        this.proxyTime = proxyTime;
    }

    public ConcurrentHashMap<Long, Integer> getTitleRankMap() {
        return titleRankMap;
    }

    public void setTitleRankMap(ConcurrentHashMap<Long, Integer> titleRankMap) {
        this.titleRankMap = titleRankMap;
    }

    public List<GuildLog> getLog_list() {
        return log_list;
    }

    public void setLog_list(List<GuildLog> log_list) {
        this.log_list = log_list;
    }


    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public HashSet<Long> getNoticeList() {
        return noticeList;
    }

    public void setNoticeList(HashSet<Long> noticeList) {
        this.noticeList = noticeList;
    }

    /**
     * 获取公会等级
     *
     * @return
     */
    public int getLevel() {
        return constructions.get(GuildSysConfig.TYPE_BASE);
    }

    public long gainGuildPower() {
        List<GuildMember> list = sortMember();
        long allFight = 0L;
        int max = Math.min(Global.GuildFightLimit, list.size());
        for (int i = 0; i < max; i++) {
            allFight += list.get(i).gainPower();
        }
        this.fightPower = allFight / max;
        return this.fightPower;
    }

    /**
     * 公会成员的战力排序，大到小
     *
     * @return
     */
    public List<GuildMember> sortMember() {
        List<GuildMember> memberList = new ArrayList<>(members.values());
        memberList.sort(Comparator.comparingLong(GuildMember::gainPower).thenComparingLong(GuildMember::getJoinTime).reversed());
        return memberList;
    }

    public int gainRankCount(int rank) {
        int num = 0;
        Iterator<Map.Entry<Long, GuildMember>> iterator = members.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Long, GuildMember> entry = iterator.next();
            if (entry.getValue().getPosition() == rank) {
                num++;
            }
        }
        return num;
    }

    public GuildBean toGuildBean() {
        if (chairMan != null) {
            return toGuildBean(chairMan.getId());
        }
        GuildBean bean = new GuildBean();
        return toGuildBean(bean);
    }

    public GuildBean toGuildBean(long chairmanId) {
        GuildBean bean = new GuildBean();
        bean.setChairmanId(chairmanId);
        return toGuildBean(bean);
    }

    public GuildBean toGuildBean(GuildBean bean) {
        bean.setCreateTime(createTime);
        bean.setGuildId(id);
        bean.setGuildName(name);
        bean.setLevel(getLevel());
        bean.setBuildValue(exp);
        String str = JsonUtils.toJSONString(this);
        str = VersionUpdateUtil.dataSave(str, 512);
        bean.setDatas(str);
        String rpstr = Manager.redPacketManager.guildRPLog(id);
        bean.setGuildredpacket(rpstr);
        return bean;
    }

    @Override
    public ConcurrentHashMap<String, Cooldown> getCooldowns() {
        return cooldowns;
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
