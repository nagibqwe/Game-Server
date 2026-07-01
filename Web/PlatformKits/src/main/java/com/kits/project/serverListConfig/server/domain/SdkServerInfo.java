package com.kits.project.serverListConfig.server.domain;

public class SdkServerInfo {
    private Long serverId;
    private String serverName;
    private String serverIp;
    private Long serverPort;
    private Long openState;
    private Long isBackup;
    private Long sortId;
    private Long serverStatus;
    private Long groupType;
    private String serverLabel;
    private String sceId;
    private String appVersion;

    public Long getServerId() {
        return serverId;
    }

    public void setServerId(Long serverId) {
        this.serverId = serverId;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public Long getServerPort() {
        return serverPort;
    }

    public void setServerPort(Long serverPort) {
        this.serverPort = serverPort;
    }

    public Long getOpenState() {
        return openState;
    }

    public void setOpenState(Long openState) {
        this.openState = openState;
    }

    public Long getIsBackup() {
        return isBackup;
    }

    public void setIsBackup(Long isBackup) {
        this.isBackup = isBackup;
    }

    public Long getSortId() {
        return sortId;
    }

    public void setSortId(Long sortId) {
        this.sortId = sortId;
    }

    public Long getServerStatus() {
        return serverStatus;
    }

    public void setServerStatus(Long serverStatus) {
        this.serverStatus = serverStatus;
    }

    public Long getGroupType() {
        return groupType;
    }

    public void setGroupType(Long groupType) {
        this.groupType = groupType;
    }

    public String getServerLabel() {
        return serverLabel;
    }

    public void setServerLabel(String serverLabel) {
        this.serverLabel = serverLabel;
    }

    public String getSceId() {
        return sceId;
    }

    public void setSceId(String sceId) {
        this.sceId = sceId;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }
}
