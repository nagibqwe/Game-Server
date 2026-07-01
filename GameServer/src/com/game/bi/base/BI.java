package com.game.bi.base;

import game.core.util.TimeUtils;

/**
 * @explain: BI公共字段
 * @time Created on 2019/12/30 19:58.
 * @author: tc
 */
public class BI {
    // 平台游戏的游戏id,用于区分游戏，由sdk获取
    private int app_id = 0;
    // 游戏运行平台 IOS=1/安卓=2/H5=3
    private int platform = 0;
    // 平台唯一的渠道id,用于区分渠道厂商，由sdk获取
    private String channel_id = "";
    // 游戏包体id，由sdk获取
    private String source_id = "";
    // 游戏大区id，如无大区设置，记录为 0
    private String zone_id = "";
    // 服务器id	1
    private int server_id = 0;
    // 设备id	aa
    private String device_id = "";
    // 平台唯一的账号id
    private String account_id = "";
    // 全局唯一的角色id
    private long role_id = 0;
    // 用户创建的角色名
    private String role_name = "";
    // 角色性别
    private int role_sex = 0;
    // 角色职业
    private int role_prof = 0;
    // 角色等级
    private int role_level = 0;
    // 角色vip等级
    private int role_vip_level = 0;
    //玩家战力
    private long role_combat = 0;
    // 客户端资源版本6.21.3
    private String app_version = "";
    // 运营商中国联通
    private String merchant = "";
    // 网络类型5G
    private String net_type = "";
    // 手机操作系统,如：android、iOS
    private String os = "";
    // 操作系统版本号,如：2.3.4
    private String os_version = "";
    // 屏幕分辨率，如：480*800
    private String screen = "";

    private String serverName = "";

    // 最后修改时间
    private long lastModifyTime = 0;

    // SDK获取的平台ID
    private int cpplatformId = 0;
    // SDK获取的gameID
    private int cpgameId  = 0;
    // 用户设备ID，Android系统取IMEI码；iOS6.0以前系统取设备号，iOS6.0及以后的系统取广告标示符（IDFA -Identifier For Advertising）, PC可以采用mac地址。请注意不要用MD5加密did字段
    private String cpdid = "";
    // 设备名称，如：三星GT-S5830
    private String cpdevice_name = "";
    //第三方渠道userId
    private String cpUserId = "";
    //第三方渠道userName
    private String cpUserName = "";
    //第三方渠道游戏简称
    private String cpGameName = "";
    //第三方渠道游戏平台简称
    private String cpPlatFormGameName = "";
    //玩家IP
    private String user_ip = "";
    //游戏版本号
    private String game_version = "";
    //最近一次地图ID
    private String map_id = "";

    // 更新最后一次修改时间
    public void updateLastModifyTime() {
        this.lastModifyTime = TimeUtils.Time();
    }

    public int getApp_id() {
        return app_id;
    }

    public void setApp_id(int app_id) {
        this.app_id = app_id;
    }

    public int getPlatform() {
        return platform;
    }

    public void setPlatform(int platform) {
        this.platform = platform;
    }

    public String getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(String channel_id) {
        if (channel_id == null || channel_id.equals(""))
            this.channel_id = "";
        else
            this.channel_id = channel_id.replaceAll("\n|\r", "");
    }

    public String getSource_id() {
        return source_id;
    }

    public void setSource_id(String source_id) {
        if (source_id == null || source_id.equals(""))
            this.source_id = "";
        else
            this.source_id = source_id.replaceAll("\n|\r", "");
    }

    public String getZone_id() {
        return zone_id;
    }

    public void setZone_id(String zone_id) {
        if (zone_id == null || zone_id.equals(""))
            this.zone_id = "";
        else
            this.zone_id = zone_id.replaceAll("\n|\r", "");
    }

    public int getServer_id() {
        return server_id;
    }

    public void setServer_id(int server_id) {
        this.server_id = server_id;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        if (device_id == null || device_id.equals(""))
            this.device_id = "";
        else
            this.device_id = device_id.replaceAll("\n|\r", "");
    }

    public String getAccount_id() {
        return account_id;
    }

    public void setAccount_id(String account_id) {
        if (account_id == null || account_id.equals(""))
            this.account_id = "";
        else
            this.account_id = account_id;
    }

    public long getRole_id() {
        return role_id;
    }

    public void setRole_id(long role_id) {
        this.role_id = role_id;
    }

    public String getRole_name() {
        return role_name;
    }

    public void setRole_name(String role_name) {
        if (role_name == null || role_name.equals(""))
            this.role_name = "";
        else
            this.role_name = role_name;
    }

    public int getRole_sex() {
        return role_sex;
    }

    public void setRole_sex(int role_sex) {
        this.role_sex = role_sex;
    }

    public int getRole_prof() {
        return role_prof;
    }

    public void setRole_prof(int role_prof) {
        this.role_prof = role_prof;
    }

    public int getRole_level() {
        return role_level;
    }

    public void setRole_level(int role_level) {
        this.role_level = role_level;
    }

    public int getRole_vip_level() {
        return role_vip_level;
    }

    public void setRole_vip_level(int role_vip_level) {
        this.role_vip_level = role_vip_level;
    }

    public long getRole_combat() {
        return role_combat;
    }

    public void setRole_combat(long role_combat) {
        this.role_combat = role_combat;
    }

    public String getApp_version() {
        return app_version;
    }

    public void setApp_version(String app_version) {
        if (app_version == null || app_version.equals(""))
            this.app_version = "";
        else
            this.app_version = app_version.replaceAll("\n|\r", "");
    }

    public String getMerchant() {
        return merchant;
    }

    public void setMerchant(String merchant) {
        if (merchant == null || merchant.equals(""))
            this.merchant = "";
        else
            this.merchant = merchant.replaceAll("\n|\r", "");
    }

    public String getNet_type() {
        return net_type;
    }

    public void setNet_type(String net_type) {
        if (net_type == null || net_type.equals(""))
            this.net_type = "";
        else
            this.net_type = net_type.replaceAll("\n|\r", "");
    }

    public int getCpplatformId() {
        return cpplatformId;
    }

    public void setCpplatformId(int cpplatformId) {
        this.cpplatformId = cpplatformId;
    }

    public int getCpgameId() {
        return cpgameId;
    }

    public void setCpgameId(int cpgameId) {
        this.cpgameId = cpgameId;
    }

    public String getCpdid() {
        return cpdid;
    }

    public void setCpdid(String cpdid) {
        this.cpdid = cpdid == null ? cpdid : cpdid.replaceAll("\n|\r", "");
    }

    public String getCpdevice_name() {
        return cpdevice_name;
    }

    public void setCpdevice_name(String cpdevice_name) {
        this.cpdevice_name = cpdevice_name == null ? cpdevice_name : cpdevice_name.replaceAll("\n|\r", "");
    }

    public String getCpUserName() {
        return cpUserName;
    }

    public void setCpUserName(String cpUserName) {
        this.cpUserName = cpUserName == null ? cpUserName : cpUserName.replaceAll("\n|\r", "");
    }

    public String getCpPlatFormGameName() {
        return cpPlatFormGameName;
    }

    public void setCpPlatFormGameName(String cpPlatFormGameName) {
        this.cpPlatFormGameName = cpPlatFormGameName == null ? cpPlatFormGameName : cpPlatFormGameName.replaceAll("\n|\r", "");
    }

    public String getScreen() {
        return screen;
    }

    public void setScreen(String screen) {
        this.screen = screen == null ? screen : screen.replaceAll("\n|\r", "");
    }

    public String getCpUserId() {
        return cpUserId;
    }

    public void setCpUserId(String cpUserId) {
        this.cpUserId = cpUserId == null ? cpUserId : cpUserId.replaceAll("\n|\r", "");
    }

    public String getCpGameName() {
        return cpGameName;
    }

    public void setCpGameName(String cpGameName) {
        this.cpGameName = cpGameName == null ? cpGameName : cpGameName.replaceAll("\n|\r", "");
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName == null ? serverName : serverName.replaceAll("\n|\r", "");
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os == null ? os : os.replaceAll("\n|\r", "");
    }

    public String getOs_version() {
        return os_version;
    }

    public void setOs_version(String os_version) {
        this.os_version = os_version == null ? os_version : os_version.replaceAll("\n|\r", "");
    }

    public String getUser_ip() {
        return user_ip;
    }

    public void setUser_ip(String user_ip) {
        this.user_ip = user_ip;
    }

    public String getGame_version() {
        return game_version;
    }

    public void setGame_version(String game_version) {
        this.game_version = game_version;
    }

    public String getMap_id() {
        return map_id;
    }

    public void setMap_id(String map_id) {
        this.map_id = map_id;
    }

    /**
     * 注意顺序，需要和glog_schema.xml基础字段对应，不要随便修改此方法
     * @return
     */
    @Override
    public String toString() {
        return TimeUtils.NowToStringWithZone() + '|' +
                this.getZone_id() + '|' +
                this.getServer_id() + '|' +
                this.getApp_version() + '|' +
                this.getAccount_id() + '|' +
                this.getRole_id() + '|' +
                this.getRole_name() + '|' +
                this.getRole_sex() + '|' +
                this.getRole_prof() + '|' +
                this.getRole_level() + '|' +
                this.getRole_vip_level() + '|' +
                this.getRole_combat() + '|' +
                this.getApp_id() + '|' +
                this.getChannel_id() + '|' +
                this.getSource_id() + '|' +
                this.getPlatform() + '|' +
                this.getDevice_id() + '|' +
                this.getCpdevice_name() + '|' +
                this.getScreen() + "|" +
                this.getOs_version() + "|" +
                this.getUser_ip() + "|" +
                this.getMap_id();
    }

    public String toString2() {
        return "BI{" +
                "app_id=" + app_id + '\'' +
                ", platform=" + platform + '\'' +
                ", channel_id='" + channel_id + '\'' +
                ", source_id='" + source_id + '\'' +
                ", zone_id='" + zone_id + '\'' +
                ", server_id=" + server_id + '\'' +
                ", device_id='" + device_id + '\'' +
                ", account_id='" + account_id + '\'' +
                ", role_id=" + role_id + '\'' +
                ", role_name='" + role_name + '\'' +
                ", role_level=" + role_level + '\'' +
                ", role_vip_level=" + role_vip_level + '\'' +
                ", role_combat='" + role_combat + '\'' +
                ", app_version='" + app_version + '\'' +
                ", merchant='" + merchant + '\'' +
                ", net_type='" + net_type + '\'' +
                ", lastModifyTime=" + lastModifyTime + '\'' +
                ", os='" + os + '\'' +
                ", os_version='" + os_version + '\'' +
                ", screen='" + screen + '\'' +
                ", serverName='" + serverName + '\'' +
                ", cpplatformId=" + cpplatformId + '\'' +
                ", cpgameId=" + cpgameId + '\'' +
                ", cpdid='" + cpdid + '\'' +
                ", cpdevice_name='" + cpdevice_name + '\'' +
                ", cpUserId='" + cpUserId + '\'' +
                ", cpUserName='" + cpUserName + '\'' +
                ", cpGameName='" + cpGameName + '\'' +
                ", cpPlatFormGameName='" + cpPlatFormGameName + '\'' +
                ", user_ip='" + user_ip + '\'' +
                ", game_version='" + game_version + '\'' +
                '}';
    }


}
