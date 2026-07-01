/**
 * Auto generated, do not edit it
 * <p>
 * role
 */
package com.game.db.bean;

import game.core.db.BaseBean;

public class roleBean extends BaseBean {

    private long roleid; // 角色ＩＤ
    private String rolename; // 角色名
    private long userId; // 账号ＩＤ
    private String platformName; // 平台名
    private byte career; // 职业
    private int degree; // 转职阶位
    private int lv; // 等级
    private int weapon;//武器
    private int wingId;//披风
    private String roledata; // 角色数据
    private int createTime; // 创建时间
    private int deleteTime; // 角色删除时间，0表示未删除
    private int serverId; // 服务器id
    private int equipMinStar;//最低星级
    private int languageType; //语言类型，0：中文简体，1：中文繁体，2：泰文，3：越南文，4：韩文 等等
    private int fashionBodyId;//时装身体ID
    private int fashionWeaponId;//时装武器ID
    private long lastLoginTime;//上一次登录的时间
    private int useIconState;//自定义头像的状态值

    @Override
    public String toString() {
        String s = "RoleBean {"
                + "roleid = " + roleid
                + "，rolename = " + rolename
                + "，userId = " + userId
                + "，platformName = " + platformName
                + "，career = " + career
                + "，degree = " + degree
                + "，lv = " + lv
                + "，weapon = " + weapon
                + "，wingId = " + wingId
                + "，roledata = " + roledata
                + "，createTime = " + createTime
                + "，deleteTime = " + deleteTime
                + "，serverId = " + serverId
                + "，equipMinStar = " + equipMinStar
                + "，languageType = " + languageType
                + "，fashionBodyId = " + fashionBodyId
                + "，fashionWeaponId = " + fashionWeaponId
                + "，lastLoginTime = " + lastLoginTime
                + "，useIconState = " + useIconState
                + "}";
        return s;
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
        return rolename;
    }

    /**
     * set 角色名
     */
    public void setRolename(String rolename) {
        this.rolename = rolename;
    }

    /**
     * get 账号ＩＤ
     *
     * @return
     */
    public long getUserId() {
        return userId;
    }

    /**
     * set 账号ＩＤ
     */
    public void setUserId(long userId) {
        this.userId = userId;
    }

    /**
     * get 平台名
     *
     * @return
     */
    public String getPlatformName() {
        return platformName;
    }

    /**
     * set 平台名
     */
    public void setPlatformName(String platformName) {
        this.platformName = platformName;
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
     */
    public void setCareer(byte career) {
        this.career = career;
    }

    /**
     * get 转职阶位
     *
     * @return
     */
    public int getDegree() {
        return degree;
    }

    /**
     * set 转职阶位
     */
    public void setDegree(int degree) {
        this.degree = degree;
    }

    /**
     * get 等级
     *
     * @return
     */
    public int getLv() {
        return lv;
    }

    /**
     * set 等级
     */
    public void setLv(int lv) {
        this.lv = lv;
    }

    /**
     * get 武器
     *
     * @return
     */
    public int getWeapon() {
        return weapon;
    }

    /**
     * set 武器
     */
    public void setWeapon(int weapon) {
        this.weapon = weapon;
    }

    /**
     * get 披风
     *
     * @return
     */
    public int getWingId() {
        return wingId;
    }

    /**
     * set 披风
     */
    public void setWingId(int wingId) {
        this.wingId = wingId;
    }

    /**
     * get 角色数据
     *
     * @return
     */
    public String getRoledata() {
        return roledata;
    }

    /**
     * set 角色数据
     */
    public void setRoledata(String roledata) {
        this.roledata = roledata;
    }

    /**
     * get 创建时间
     *
     * @return
     */
    public int getCreateTime() {
        return createTime;
    }

    /**
     * set 创建时间
     */
    public void setCreateTime(int createTime) {
        this.createTime = createTime;
    }

    /**
     * get 删除时间
     *
     * @return
     */
    public int getDeleteTime() {
        return deleteTime;
    }

    /**
     * set 删除时间
     */
    public void setDeleteTime(int deleteTime) {
        this.deleteTime = deleteTime;
    }

    /**
     * get 服务器id
     *
     * @return
     */
    public int getServerId() {
        return serverId;
    }

    /**
     * set 服务器id
     */
    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public int getEquipMinStar() {
        return equipMinStar;
    }

    public void setEquipMinStar(int equipMinStar) {
        this.equipMinStar = equipMinStar;
    }

    public int getLanguageType() {
        return languageType;
    }

    public void setLanguageType(int languageType) {
        this.languageType = languageType;
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

    public long getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(long lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public int getUseIconState() {
        return useIconState;
    }

    public void setUseIconState(int useIconState) {
        this.useIconState = useIconState;
    }
        
}
