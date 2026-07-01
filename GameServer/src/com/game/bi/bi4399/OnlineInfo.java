package com.game.bi.bi4399;

import java.util.HashSet;
import java.util.Set;

public class OnlineInfo {

    private String platform;

    private String device;

    private Set<Long> roleIds = new HashSet<>();

    private Set<String> devices = new HashSet<>();

    private Set<String> ips = new HashSet<>();

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public Set<Long> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(Set<Long> roleIds) {
        this.roleIds = roleIds;
    }

    public Set<String> getDevices() {
        return devices;
    }

    public void setDevices(Set<String> devices) {
        this.devices = devices;
    }

    public Set<String> getIps() {
        return ips;
    }

    public void setIps(Set<String> ips) {
        this.ips = ips;
    }
}
