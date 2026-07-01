package com.game.player.structs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.game.count.structs.Count;
import com.game.count.structs.ICount;
import com.game.home.struct.House;
import game.core.db.BaseBean;
import game.core.util.JsonUtils;
import game.message.PlayerMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;


public class GlobalPlayerWorldInfo extends BaseBean implements ICount {

    @JsonIgnore
    transient  String plat;    //来源渠道
    @JsonIgnore
    transient int serverId;   //玩家服务器ID
    @JsonIgnore
    transient int createServerId;//角色创建服id
    @JsonIgnore
    transient int createTime;//角色创建时间
    @JsonIgnore
    transient long userId;//账号id
    @JsonIgnore
    transient long id; // 角色ID
    @JsonIgnore
    transient String roleName; // 角色名
    @JsonIgnore
    transient long fightPower;//战斗力
    @JsonIgnore
    transient int level; // 等级
    @JsonIgnore
    transient byte career; // 职业
    @JsonIgnore
    transient int playerVip; //玩家VIP
    @JsonIgnore
    transient String data;
    @JsonIgnore
    transient long lastSaveDB;
    @JsonIgnore
    transient long readySaveDB;

    private float expRate;  //经验倍率
    private long popularity;  //历史人气
    private int lastOffTime;//角色上次离线时间,0表示在线
    private int horseId; //当前选乘坐骑
    private int wingId;//翅膀
    private long guildId;//仙盟id
    private int fashionHeadId;        //时装头像
    private int fashionHeadFrameId;   //时装头像框globalPlayerWorldInfo
    private int fashionBodyId; //所穿时装身体Id
    private int fashionWeaponId; //所穿时装武器Id
    private int fashionHalo;
    private int fashionMatrix;
    private byte sex;   //性别
    private int stateVip;   //境界
    private int shiHaiLevel;//识海等级
    private int spiritId;//灵体阶数
    private int soulArmorId; //魂甲品质

    //计数器
    private ConcurrentHashMap<String, Count> counts = new ConcurrentHashMap<>();
    //家园
    private House house = new House();
    //社区设置信息
    private PlayerCommunityInfoSettingInfo playerCommunityInfoSettingInfo = new PlayerCommunityInfoSettingInfo();
    //留言时间
    private List<CommunityLeaveMessageInfo> commcunityLeaveMessageInfoList = new ArrayList<>();

    private String customHeadPath; //自定义头像路径
    private boolean useCustomHead; // 是否使用自定义头像 1 表示使用

    //朋友圈数据
    private List<FriendCircleInfo> friendCircleInfoList = new ArrayList<>();

    private String guildName; //公会名字
    /**
     * 更新数据
     * @param globalPlayerWorldInfo
     */
    public void update(PlayerMessage.GlobalPlayerWorldInfo globalPlayerWorldInfo) {
        this.id = globalPlayerWorldInfo.getRoleid();
        this.plat = globalPlayerWorldInfo.getPlat();
        this.createServerId = globalPlayerWorldInfo.getCsid();
        this.createTime = globalPlayerWorldInfo.getCreateTime();
        this.userId = globalPlayerWorldInfo.getUserId();
        this.roleName = globalPlayerWorldInfo.getRolename();
        this.fightPower = globalPlayerWorldInfo.getFightPower();
        this.level = globalPlayerWorldInfo.getLevel();
        this.career = (byte) globalPlayerWorldInfo.getCareer();
        this.playerVip = globalPlayerWorldInfo.getPlayerVip();

        this.lastOffTime = globalPlayerWorldInfo.getLastOffTime();
        this.horseId = globalPlayerWorldInfo.getHorseId();
        this.wingId = globalPlayerWorldInfo.getWingId();
        this.guildId = globalPlayerWorldInfo.getGuildId();
        this.fashionHeadId = globalPlayerWorldInfo.getFashionHeadId();
        this.fashionHeadFrameId = globalPlayerWorldInfo.getFashionHeadFrameId();
        this.fashionBodyId = globalPlayerWorldInfo.getFashionBodyId();
        this.fashionWeaponId = globalPlayerWorldInfo.getFashionWeaponId();
        this.fashionHalo = globalPlayerWorldInfo.getFashionHalo();
        this.fashionMatrix = globalPlayerWorldInfo.getFashionMatrix();
        this.sex = (byte) globalPlayerWorldInfo.getSex();
        this.stateVip = globalPlayerWorldInfo.getStateVip();
        this.shiHaiLevel = globalPlayerWorldInfo.getShiHaiLevel();
        this.spiritId = globalPlayerWorldInfo.getSpiritId();
        this.soulArmorId = globalPlayerWorldInfo.getSoulArmorId();
        this.serverId = globalPlayerWorldInfo.getServerId();

        this.customHeadPath = globalPlayerWorldInfo.getCustomHeadPath();
        this.useCustomHead = globalPlayerWorldInfo.getUseCustomHead();
        this.guildName = globalPlayerWorldInfo.getGuildName();
    }

    //序列化玩家数据
    public void toDB(){
        this.data = JsonUtils.toJSONString(this);
    }
    //解析DATA
    public GlobalPlayerWorldInfo toCache(){
        GlobalPlayerWorldInfo temp = JsonUtils.parseObject(data, GlobalPlayerWorldInfo.class);
        temp.setId(id);
        temp.setPlat(plat);
        temp.setServerId(serverId);
        temp.setCreateServerId(createServerId);
        temp.setCreateTime(createTime);
        temp.setUserId(userId);
        temp.setRoleName(roleName);
        temp.setFightPower(fightPower);
        temp.setLevel(level);
        temp.setCareer(career);
        temp.setPlayerVip(playerVip);
        return temp;
    }

    public House getHouse() {
        return house;
    }

    public void setHouse(House house) {
        this.house = house;
    }

    public String getPlat() {
        return plat;
    }

    public void setPlat(String plat) {
        this.plat = plat;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public int getCreateServerId() {
        return createServerId;
    }

    public void setCreateServerId(int createServerId) {
        this.createServerId = createServerId;
    }

    public int getCreateTime() {
        return createTime;
    }

    public void setCreateTime(int createTime) {
        this.createTime = createTime;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public long getFightPower() {
        return fightPower;
    }

    public void setFightPower(long fightPower) {
        this.fightPower = fightPower;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public byte getCareer() {
        return career;
    }

    public void setCareer(byte career) {
        this.career = career;
    }

    public int getLastOffTime() {
        return lastOffTime;
    }

    public void setLastOffTime(int lastOffTime) {
        this.lastOffTime = lastOffTime;
    }

    public int getHorseId() {
        return horseId;
    }

    public void setHorseId(int horseId) {
        this.horseId = horseId;
    }

    public int getWingId() {
        return wingId;
    }

    public void setWingId(int wingId) {
        this.wingId = wingId;
    }

    public long getGuildId() {
        return guildId;
    }

    public void setGuildId(long guildId) {
        this.guildId = guildId;
    }

    public int getFashionHeadId() {
        return fashionHeadId;
    }

    public void setFashionHeadId(int fashionHeadId) {
        this.fashionHeadId = fashionHeadId;
    }

    public int getFashionHeadFrameId() {
        return fashionHeadFrameId;
    }

    public void setFashionHeadFrameId(int fashionHeadFrameId) {
        this.fashionHeadFrameId = fashionHeadFrameId;
    }

    public int getFashionBodyId() {
        return fashionBodyId;
    }

    public void setFashionBodyId(int fashionBodyId) {
        this.fashionBodyId = fashionBodyId;
    }

    public int getFashionWeaponId() {
        return fashionWeaponId;
    }

    public void setFashionWeaponId(int fashionWeaponId) {
        this.fashionWeaponId = fashionWeaponId;
    }

    public int getFashionHalo() {
        return fashionHalo;
    }

    public void setFashionHalo(int fashionHalo) {
        this.fashionHalo = fashionHalo;
    }

    public int getFashionMatrix() {
        return fashionMatrix;
    }

    public void setFashionMatrix(int fashionMatrix) {
        this.fashionMatrix = fashionMatrix;
    }

    public byte getSex() {
        return sex;
    }

    public void setSex(byte sex) {
        this.sex = sex;
    }

    public int getStateVip() {
        return stateVip;
    }

    public void setStateVip(int stateVip) {
        this.stateVip = stateVip;
    }

    public int getShiHaiLevel() {
        return shiHaiLevel;
    }

    public void setShiHaiLevel(int shiHaiLevel) {
        this.shiHaiLevel = shiHaiLevel;
    }

    public int getPlayerVip() {
        return playerVip;
    }

    public void setPlayerVip(int playerVip) {
        this.playerVip = playerVip;
    }

    public int getSpiritId() {
        return spiritId;
    }

    public void setSpiritId(int spiritId) {
        this.spiritId = spiritId;
    }

    public int getSoulArmorId() {
        return soulArmorId;
    }

    public void setSoulArmorId(int soulArmorId) {
        this.soulArmorId = soulArmorId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setCounts(ConcurrentHashMap<String, Count> counts) {
        this.counts = counts;
    }

    public long getLastSaveDB() {
        return lastSaveDB;
    }

    public void setLastSaveDB(long lastSaveDB) {
        this.lastSaveDB = lastSaveDB;
    }

    public long getReadySaveDB() {
        return readySaveDB;
    }

    public void setReadySaveDB(long readySaveDB) {
        this.readySaveDB = readySaveDB;
    }

    public float getExpRate() {
        return expRate;
    }

    public void setExpRate(float expRate) {
        this.expRate = expRate;
    }

    public long getPopularity() {
        return popularity;
    }

    public void setPopularity(long popularity) {
        this.popularity = popularity;
    }

    @Override
    public String toString() {
        return "Player{" +
                "plat='" + plat + '\'' +
                ", serverId=" + serverId +
                ", userId=" + userId +
                ", id=" + id +
                ", roleName='" + roleName + '\'' +
                '}';
    }


    public PlayerCommunityInfoSettingInfo getPlayerCommunityInfoSettingInfo() {
        return playerCommunityInfoSettingInfo;
    }

    public void setPlayerCommunityInfoSettingInfo(PlayerCommunityInfoSettingInfo playerCommunityInfoSettingInfo) {
        this.playerCommunityInfoSettingInfo = playerCommunityInfoSettingInfo;
    }

    public List<CommunityLeaveMessageInfo> getCommcunityLeaveMessageInfoList() {
        return commcunityLeaveMessageInfoList;
    }

    public void setCommcunityLeaveMessageInfoList(List<CommunityLeaveMessageInfo> commcunityLeaveMessageInfoList) {
        this.commcunityLeaveMessageInfoList = commcunityLeaveMessageInfoList;
    }

    /**
     * 获取技术数据
     *
     * @return
     */
    @Override
    public ConcurrentHashMap<String, Count> getCounts() {
        return counts;
    }

    public String getCustomHeadPath() {
        return customHeadPath;
    }

    public void setCustomHeadPath(String customHeadPath) {
        this.customHeadPath = customHeadPath;
    }

    public boolean isUseCustomHead() {
        return useCustomHead;
    }

    public void setUseCustomHead(boolean useCustomHead) {
        this.useCustomHead = useCustomHead;
    }

    public List<FriendCircleInfo> getFriendCircleInfoList() {
        return friendCircleInfoList;
    }

    public void setFriendCircleInfoList(List<FriendCircleInfo> friendCircleInfoList) {
        this.friendCircleInfoList = friendCircleInfoList;
    }

    public String getGuildName() {
        return guildName;
    }

    public void setGuildName(String guildName) {
        this.guildName = guildName;
    }
}
