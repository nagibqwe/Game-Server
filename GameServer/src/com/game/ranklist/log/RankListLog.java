/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.ranklist.log;

import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.BaseLogBean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 排行榜的日志
 */
public class RankListLog extends BaseLogBean {

    private static final Logger log = LogManager.getLogger(RankListLog.class);

    private String date;        //记录此排行榜日志的日期
    private int rankKind;       //排行榜类别，参考RankKind类定义
    private int rank;           //榜上排行名次
    private long roleId;        //角色Id
    private String roleName;    //角色名
    private String rankData;    //进行排行的数据
    private String platformName;//平台名

    @Log(logField = "date", fieldType = "varchar(200)", index = "0")
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Log(logField = "rankKind", fieldType = "int", index = "0")
    public int getRankKind() {
        return rankKind;
    }

    public void setRankKind(int rankKind) {
        this.rankKind = rankKind;
    }

    @Log(logField = "rank", fieldType = "int", index = "0")
    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    @Log(logField = "roleId", fieldType = "bigint", index = "2")
    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    @Log(logField = "roleName", fieldType = "varchar(100)", index = "0")
    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Log(logField = "rankData", fieldType = "varchar(300)", index = "0")
    public String getRankData() {
        return rankData;
    }

    public void setRankData(String rankData) {
        this.rankData = rankData;
    }

    @Log(logField = "platformName", fieldType = "varchar(128)", index = "0")
    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    @Override
    public TableCheckStepEnum getRollingStep() {
        return TableCheckStepEnum.MONTH;
    }

    @Override
    public void logToFile() {
        log.error(buildSql());
    }
}
