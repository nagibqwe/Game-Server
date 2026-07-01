package com.kits.project.serverList.domain;

public class LoginServerList {
    private String svr_host;
    private String svr_name;
    private int svr_port;

    public String getSvr_host() {
        return svr_host;
    }

    public void setSvr_host(String svr_host) {
        this.svr_host = svr_host;
    }

    public String getSvr_name() {
        return svr_name;
    }

    public void setSvr_name(String svr_name) {
        this.svr_name = svr_name;
    }

    public int getSvr_port() {
        return svr_port;
    }

    public void setSvr_port(int svr_port) {
        this.svr_port = svr_port;
    }
}
