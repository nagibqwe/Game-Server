package com.gm.project.stat.stat_online.domain;

import com.gm.framework.aspectj.lang.annotation.Excel;
import com.gm.framework.web.domain.BaseEntity;

public class AllOnlineCountBean extends BaseEntity {
    private static final long serialVersionUID = 1L;
    @Excel(name = "区服ID")
    private Integer serverId;
    @Excel(name = "区服名称")
    private String serverName;
    @Excel(name = "时间")
    private Long time;
    @Excel(name = "在线人数")
    private Integer num;

    public Integer getServerId() {
        return serverId;
    }

    public void setServerId(Integer serverId) {
        this.serverId = serverId;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }
}
