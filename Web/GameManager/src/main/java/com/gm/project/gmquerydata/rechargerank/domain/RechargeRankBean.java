package com.gm.project.gmquerydata.rechargerank.domain;

import com.gm.framework.aspectj.lang.annotation.Excel;
import com.gm.framework.web.domain.BaseEntity;


/**
 *
 * 
 * @author gm
 * @date 2021-08-06
 */
public class RechargeRankBean
{
    private static final long serialVersionUID = 1L;


    @Excel(name = "排名")
    private Integer rank;
    /** dau */
    @Excel(name = "用户ID")
    private String userId;
    @Excel(name = "角色ID")
    private String roleId;

    @Excel(name = "角色名")
    private String roleName;

    @Excel(name = "等级")
    private Long level;

    @Excel(name = "职业")
    private Integer career;

    @Excel(name = "总充值元宝")
    private Long rechargeGold ;

    @Excel(name = "剩余元宝")
    private Long remainGold ;


    @Excel(name = "创建时间")
    private String createTime ;


    @Excel(name = "在线时长(秒)")
    private Long onlineTime;


    @Excel(name = "上次登录时间")
    private String lastLoginTime;

    @Excel(name = "所在服")
    private Integer createSid;


    @Excel(name = "充值金额(元)")
    private Integer totalRecharge;

    @Excel(name = "充值次数")
    private Integer rechargeCount;


    @Excel(name = "单次最大充值(元)")
    private Integer maxRecharge;


    @Excel(name = "单次最小充值(元)")
    private Integer minRecharge ;


    @Excel(name = "平均充值(元)")
    private Float avgRecharge;
    @Excel(name = "最后充值时间")
    private String lastRechargeTime;


    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }



    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Long getLevel() {
        return level;
    }

    public void setLevel(Long level) {
        this.level = level;
    }

    public Integer getCareer() {
        return career;
    }

    public void setCareer(Integer career) {
        this.career = career;
    }


    public Long getRemainGold() {
        return remainGold;
    }

    public void setRemainGold(Long remainGold) {
        this.remainGold = remainGold;
    }


    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Long getOnlineTime() {
        return onlineTime;
    }

    public void setOnlineTime(Long onlineTime) {
        this.onlineTime = onlineTime;
    }

    public String getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Integer getCreateSid() {
        return createSid;
    }

    public void setCreateSid(Integer createSid) {
        this.createSid = createSid;
    }

    public Integer getTotalRecharge() {
        return totalRecharge;
    }

    public void setTotalRecharge(Integer totalRecharge) {

        this.totalRecharge = totalRecharge/100;
    }

    public Integer getRechargeCount() {
        return rechargeCount;
    }

    public void setRechargeCount(Integer rechargeCount) {
        this.rechargeCount = rechargeCount;
    }

    public Integer getMaxRecharge() {
        return maxRecharge;
    }

    public void setMaxRecharge(Integer maxRecharge) {
        this.maxRecharge = maxRecharge/100;
    }

    public Integer getMinRecharge() {
        return minRecharge;
    }

    public void setMinRecharge(Integer minRecharge) {

        this.minRecharge = minRecharge/100;
    }

    public Float getAvgRecharge() {
        return avgRecharge;
    }

    public void setAvgRecharge(Float avgRecharge) {
        this.avgRecharge = avgRecharge/100;
    }

    public String getLastRechargeTime() {
        return lastRechargeTime;
    }

    public void setLastRechargeTime(String lastRechargeTime) {
        this.lastRechargeTime = lastRechargeTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public Long getRechargeGold() {
        return rechargeGold;
    }

    public void setRechargeGold(Long rechargeGold) {
        this.rechargeGold = rechargeGold;
    }
}
