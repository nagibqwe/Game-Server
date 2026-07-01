package com.gm.project.stat.stat_role.domain;

import com.gm.framework.aspectj.lang.annotation.Excel;
import com.gm.framework.web.domain.BaseEntity;
import io.swagger.models.auth.In;

/**
 * 角色信息
 */
public class RoleInfoBean extends BaseEntity {
    private static final long serialVersionUID = 1L;
    @Excel(name = "渠道")
    private String platformName;
    @Excel(name = "创建区服ID")
    private String createsid;
    @Excel(name = "账号名")
    private String funcellUUid;
    @Excel(name = "用户ID")
    private Long userId;
    @Excel(name = "角色ID")
    private Long roleId;
    @Excel(name = "角色名称")
    private String roleName;
    @Excel(name = "角色等级")
    private Integer level;
    @Excel(name = "在线时长(单位：秒)")
    private Integer onlineTime;
    @Excel(name = "充值灵玉")
    private Integer rechargeGold;
    @Excel(name = "剩余灵玉")
    private Integer gold;
    @Excel(name = "角色创建时间")
    private String createTimeStr;
    @Excel(name = "是否删除")
    private Integer isDelete;
    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public String getCreatesid() {
        return createsid;
    }

    public void setCreatesid(String createsid) {
        this.createsid = createsid;
    }

    public String getFuncellUUid() {
        return funcellUUid;
    }

    public void setFuncellUUid(String funcellUUid) {
        this.funcellUUid = funcellUUid;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getOnlineTime() {
        return onlineTime;
    }

    public void setOnlineTime(Integer onlineTime) {
        this.onlineTime = onlineTime;
    }

    public Integer getRechargeGold() {
        return rechargeGold;
    }

    public void setRechargeGold(Integer rechargeGold) {
        this.rechargeGold = rechargeGold;
    }

    public Integer getGold() {
        return gold;
    }

    public void setGold(Integer gold) {
        this.gold = gold;
    }


    public String getCreateTimeStr() {
        return createTimeStr;
    }

    public void setCreateTimeStr(String createTimeStr) {
        this.createTimeStr = createTimeStr;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }
}
