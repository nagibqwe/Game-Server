package com.game.player.structs;

import com.game.guild.structs.Guild;
import com.game.manager.Manager;
import game.core.db.BaseBean;
import game.core.net.Config.ServerConfig;
import game.core.util.AutoIncrementIntArray;
import game.core.util.StringUtils;
import game.message.PlayerMessage;


public class PlayerWorldInfo extends BaseBean {

    private long userId;//账号id
    private long roleid; // 角色ID
    private String rolename; // 角色名
    private byte career; // 职业
    private int level; // 等级
    private String plat;//来源渠道
    private int createTime;//角色创建时间
    private int csid;//角色创建服id
    private int lastOffTime;//角色上次离线时间,0表示在线
    private int horseId; //当前选乘坐骑
    private int wingId;//翅膀
    private long fightPower;//战斗力
    private long guildId;//仙盟id

    private int fashionHeadId;        //时装头像
    private int fashionHeadFrameId;   //时装头像框
    private int fashionBodyId; //所穿时装身体Id
    private int fashionWeaponId; //所穿时装武器Id
    private int fashionHalo;
    private int fashionMatrix;
    private byte sex;   //性别
    private int stateVip;   //境界
    private int shiHaiLevel;//识海等级
    private int playerVip; //玩家VIP
    private int spiritId;//灵体阶数
    private int soulArmorId; //魂甲品质
    private int serverId; //服务器id 跨服好友用

    private String customHeadPath; //自定义头像路径
    private boolean useCustomHead; // 是否使用自定义头像 1 表示使用

    public int getPlayerVip() {
        return playerVip;
    }

    public void setPlayerVip(int playerVip) {
        this.playerVip = playerVip;
    }

    /**
     * 用于存储扩展数据
     */
    private final transient AutoIncrementIntArray intdata = new AutoIncrementIntArray(1);//所以Int的参数数据

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

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
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

    /**
     * get 角色ＩＤ
     *
     * @return
     */
    public long getRoleid() {
        return roleid;
    }

    /**
     * set 角色ＩＤ
     *
     * @param roleid
     */
    public void setRoleid(long roleid) {
        this.roleid = roleid;
    }

    /**
     * get 角色名
     *
     * @return
     */
    public String getRolename() {
        if (Manager.registerManager.getRoleName(roleid) == null) {
            return rolename;
        }
        return Manager.registerManager.getRoleName(roleid); //从名字缓存列表中获取
    }
    public String getRolename2() {
       return rolename;

    }
    public int getHorseId() {
        return horseId;
    }

    public void setHorseId(int horseId) {
        this.horseId = horseId;
    }

    public String getPlat() {
        return plat;
    }

    public void setPlat(String plat) {
        this.plat = plat;
    }

    /**
     * set 角色名
     *
     * @param rolename
     */
    public void setRolename(String rolename) {
        this.rolename = rolename;
    }

    public int getWingId() {
        return wingId;
    }

    public void setWingId(int wingId) {
        this.wingId = wingId;
    }

    /**
     * get 职业
     *
     * @return
     */
    public byte getCareer() {
        return career;
    }

    /**
     * set 职业
     *
     * @param career
     */
    public void setCareer(byte career) {
        this.career = career;
    }

    public byte getSex() {
        return sex;
    }

    public void setSex(byte sex) {
        this.sex = sex;
    }

    /**
     * get 等级
     *
     * @return
     */
    public int getLevel() {
        return level;
    }

    /**
     * set 等级
     *
     * @param level
     */
    public void setLevel(int level) {
        this.level = level;
    }


    public int getCsid() {
        return csid;
    }

    public void setCsid(int csid) {
        this.csid = csid;
    }

    public int getLastOffTime() {
        return lastOffTime;
    }

    public void setLastOffTime(int lastOffTime) {
        this.lastOffTime = lastOffTime;
    }

    public long getFightPower() {
        return fightPower;
    }

    public void setFightPower(long fightPower) {
        this.fightPower = fightPower;
    }

    public long getGuildId() {
        return guildId;
    }

    public void setGuildId(long guildId) {
        this.guildId = guildId;
    }

    /**
     * 是否在线
     *
     * @return
     */
    public boolean isOnLine() {
        return lastOffTime == 0;
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

    public int getCreateTime() {
        return createTime;
    }

    public void setCreateTime(int createTime) {
        this.createTime = createTime;
    }

    public AutoIncrementIntArray getIntdata() {
        return intdata;
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

    /**
     * 转行成proto消息
     * @return
     */
    public PlayerMessage.GlobalPlayerWorldInfo.Builder toGlobalPlayerWorldInfo() {

        PlayerMessage.GlobalPlayerWorldInfo.Builder  builder = PlayerMessage.GlobalPlayerWorldInfo.newBuilder();
        builder.setUserId(this.getUserId());
        builder.setRoleid(this.getRoleid());
        builder.setRolename(this.getRolename());
        builder.setCareer(this.getCareer());
        builder.setLevel(this.getLevel());
        //取游戏服配置
        builder.setPlat(ServerConfig.getServerPlatform());
        builder.setCreateTime(this.getCreateTime());
        builder.setCsid(this.getCsid());
        builder.setLastOffTime(this.getLastOffTime());
        builder.setHorseId(this.getHorseId());
        builder.setWingId(this.getWingId());
        builder.setFightPower(this.getFightPower());
        builder.setGuildId(this.getGuildId());
        builder.setFashionHeadId(this.getFashionHeadId());
        builder.setFashionHeadFrameId(this.getFashionHeadFrameId());
        builder.setFashionBodyId(this.getFashionBodyId());
        builder.setFashionWeaponId(this.getFashionWeaponId());
        builder.setFashionHalo(this.getFashionHalo());
        builder.setFashionMatrix(this.getFashionMatrix());
        builder.setSex(this.getSex());
        builder.setStateVip(this.getStateVip());
        builder.setShiHaiLevel(this.getShiHaiLevel());
        builder.setPlayerVip(this.getPlayerVip());
        builder.setSpiritId(this.getSpiritId());
        builder.setSoulArmorId(this.getSoulArmorId());
        builder.setServerId(ServerConfig.getServerId());

        if(!StringUtils.isEmpty(this.getCustomHeadPath())){
            builder.setCustomHeadPath(this.getCustomHeadPath());
        }

        builder.setUseCustomHead(this.useCustomHead);
        Guild guild = Manager.guildsManager.getGuildById(guildId);
        if (guild != null) {
            builder.setGuildName(guild.getName());
        }
        return builder;
    }


    public void fromGlobalPlayerWorldInfo(PlayerMessage.GlobalPlayerWorldInfo builder) {

        this.setUserId(builder.getUserId());
        this.setRoleid(builder.getRoleid());
        this.setRolename(builder.getRolename());
        this.setCareer((byte)builder.getCareer());
        this.setLevel(builder.getLevel());
        //取游戏服配置
        this.setPlat(ServerConfig.getServerPlatform());
        this.setCreateTime(builder.getCreateTime());
        this.setCsid(builder.getCsid());
        this.setLastOffTime(builder.getLastOffTime());
        this.setHorseId(builder.getHorseId());
        this.setWingId(builder.getWingId());
        this.setFightPower(builder.getFightPower());
        this.setGuildId(builder.getGuildId());
        this.setFashionHeadId(builder.getFashionHeadId());
        this.setFashionHeadFrameId(builder.getFashionHeadFrameId());
        this.setFashionBodyId(builder.getFashionBodyId());
        this.setFashionWeaponId(builder.getFashionWeaponId());
        this.setFashionHalo(builder.getFashionHalo());
        this.setFashionMatrix(builder.getFashionMatrix());
        this.setSex((byte)builder.getSex());
        this.setStateVip(builder.getStateVip());
        this.setShiHaiLevel(builder.getShiHaiLevel());
        this.setPlayerVip(builder.getPlayerVip());
        this.setSpiritId(builder.getSpiritId());
        this.setSoulArmorId(builder.getSoulArmorId());
        this.setServerId(builder.getServerId());
        this.setUseCustomHead(builder.getUseCustomHead());
        if(!StringUtils.isEmpty(this.getCustomHeadPath())){
            this.setCustomHeadPath(this.getCustomHeadPath());
        }
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
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
}
