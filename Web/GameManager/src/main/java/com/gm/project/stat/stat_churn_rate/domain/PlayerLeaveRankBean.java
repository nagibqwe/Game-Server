package com.gm.project.stat.stat_churn_rate.domain;

public class PlayerLeaveRankBean {

    private Integer rank;

    private Integer userLeaveCount;

    private Integer vipUserLeaveCount;

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Integer getUserLeaveCount() {
        return userLeaveCount;
    }

    public void setUserLeaveCount(Integer userLeaveCount) {
        this.userLeaveCount = userLeaveCount;
    }

    public Integer getVipUserLeaveCount() {
        return vipUserLeaveCount;
    }

    public void setVipUserLeaveCount(Integer vipUserLeaveCount) {
        this.vipUserLeaveCount = vipUserLeaveCount;
    }
}
