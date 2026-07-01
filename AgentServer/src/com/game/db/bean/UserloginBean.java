package com.game.db.bean;

import com.game.login.LoginDataBean;
import game.core.json.TypeReference;
import game.core.util.JsonUtils;

import java.util.Set;

public class UserloginBean {

    private long userId; // 游戏生成的账号id
    private String userName; // 553平台生成的账号ID
    private String platformAccount; // 平台生成的账号
    private String platformName; // 平台名
    private int lastEnterServerId; // 上次进入的区服id
    private String data; // 区服创建角色信息
    private long createTime; // 创建时间
    private int isDelete; // 0未删除，1删除
    private String lastLoginIp;//最后登录的IP
    private long lastEnterRoleId;//上次登录的角色id

    /**
     * 上次登录的角色id
     */
    public long getLastEnterRoleId() {
        return lastEnterRoleId;
    }

    /**
     * 上次登录的角色id
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
     * get 区服创建角色信息
     *
     * @return
     */
    public String getData() {
        return data;
    }

    /**
     * set 区服创建角色信息
     */
    public void setData(String data) {
        this.data = data;
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

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    @SuppressWarnings("unchecked")
    public LoginDataBean toLoginDataBean() {
        LoginDataBean bean = new LoginDataBean();
        bean.setCreateTime(createTime);
        bean.setIsDelete(isDelete);
        bean.setLastEnterServerId(lastEnterServerId);
        bean.setPlatformAccount(platformAccount);
        bean.setPlatformName(platformName);
        Set<Integer> sIdList = JsonUtils.parseObject(data, new TypeReference<Set<Integer>>(){});
        bean.setSIdList(sIdList);
        bean.setUserId(userId);
        bean.setUserName(userName);
        bean.setLastLoginIp(lastLoginIp);
        bean.setLastEnterRoleId(lastEnterRoleId);
        return bean;
    }
}
