package com.game.login;

import com.game.db.bean.UserloginBean;
import game.core.util.JsonUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Administrator
 */
public class LoginDataBean {

    private long userId; // 游戏生成的账号id
    private String userName; // 553平台生成的账号ID
    private String platformAccount; // 平台生成的账号
    private String platformName; // 平台名九 零一起玩www.9  0175.com
    private int lastEnterServerId; // 上次进入的区服id
    private long createTime; // 创建时间
    private int isDelete; // 0未删除，1删除
    private Set<Integer> sIdList = new HashSet<>(); //注册了角色的服务器列表
    private String lastLoginIp;//最后登录的IP
    private long lastEnterRoleId;//上次登录的角色id

    /**
     * 上次登录的角色id
     *
     * @return
     */
    public long getLastEnterRoleId() {
        return lastEnterRoleId;
    }

    /**
     * 上次登录的角色id
     *
     * @param lastEnterRoleId
     */
    public void setLastEnterRoleId(long lastEnterRoleId) {
        this.lastEnterRoleId = lastEnterRoleId;
    }

    /**
     * get 游戏生成的账号id
     *
     * @return
     */
    public long getUserId() {
        return userId;
    }

    /**
     * set 游戏生成的账号id
     */
    public void setUserId(long userId) {
        this.userId = userId;
    }

    /**
     * get 553平台生成的账号ID
     *
     * @return
     */
    public String getUserName() {
        return userName;
    }

    /**
     * set 553平台生成的账号ID
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * get 平台生成的账号
     *
     * @return
     */
    public String getPlatformAccount() {
        return platformAccount;
    }

    /**
     * set 平台生成的账号
     */
    public void setPlatformAccount(String platformAccount) {
        this.platformAccount = platformAccount;
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
     * get 上次进入的区服id
     *
     * @return
     */
    public int getLastEnterServerId() {
        return lastEnterServerId;
    }

    /**
     * set 上次进入的区服id
     */
    public void setLastEnterServerId(int lastEnterServerId) {
        this.lastEnterServerId = lastEnterServerId;
    }

    /**
     * get 创建时间
     *
     * @return
     */
    public long getCreateTime() {
        return createTime;
    }

    /**
     * set 创建时间
     */
    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    /**
     * get 0未删除，1删除
     *
     * @return
     */
    public int getIsDelete() {
        return isDelete;
    }

    /**
     * set 0未删除，1删除
     */
    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }

    /**
     * 获取注册了角色的服务器id列表
     */
    public Set<Integer> getSIdList() {
        return sIdList;
    }

    /**
     * 设置注册了角色的服务器id列表
     */
    public void setSIdList(Set<Integer> sIdList) {
        this.sIdList = sIdList;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public UserloginBean toUserloginBean() {
        UserloginBean bean = new UserloginBean();
        bean.setCreateTime(createTime);
        bean.setData(JsonUtils.toJSONString(sIdList));
        bean.setIsDelete(isDelete);
        bean.setLastEnterServerId(lastEnterServerId);
        bean.setPlatformAccount(platformAccount);
        bean.setPlatformName(platformName);
        bean.setUserId(userId);
        bean.setUserName(userName);
        bean.setLastLoginIp(lastLoginIp);
        bean.setLastEnterRoleId(lastEnterRoleId);
        return bean;
    }
}
