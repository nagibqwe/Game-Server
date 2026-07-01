package com.backend.struct.log.entity.ranklist;

import com.backend.annotation.log.FieldDesc;
import com.backend.annotation.log.Table;
import com.backend.struct.log.IConvertor;
import com.backend.struct.log.TableType;

import java.util.Map;

/**
 * 排行榜的日志
 */
@Table(name = "ranklistlog", tableType = TableType.Month)
public class RankListLog implements IConvertor {

    @FieldDesc
    private String date;            //记录此排行榜日志的日期

    @FieldDesc(selectKey = true)
    private int rankKind;           //排行榜类别，参考RankKind类定义

    @FieldDesc
    private int rank;               //榜上排行名次

    @FieldDesc
    private long roleId;            //角色Id

    @FieldDesc
    private String roleName;        //角色名

    @FieldDesc
    private String rankData;        //进行排行的数据

    @FieldDesc
    private String platformName;    //平台名

    @FieldDesc(desc = "logentity.commonlogbean.time")
    private long time;              //时间

    @Override
    public Map<String, String> convert(Map<String, String> data) {
        return data;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getRankKind() {
        return rankKind;
    }

    public void setRankKind(int rankKind) {
        this.rankKind = rankKind;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRankData() {
        return rankData;
    }

    public void setRankData(String rankData) {
        this.rankData = rankData;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
