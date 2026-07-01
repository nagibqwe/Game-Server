package com.kits.project.serverList.domain;

public class ServerList {
    private int svr_id;
    private String svr_name;
    private String svr_host;
    private int svr_port;
    private int svr_sort;
    private int svr_status;
    private int group_type;
    private String svr_label;
    private int register_num;

    public int getSvr_id() {
        return svr_id;
    }

    public void setSvr_id(int svr_id) {
        this.svr_id = svr_id;
    }

    public String getSvr_name() {
        return svr_name;
    }

    public void setSvr_name(String svr_name) {
        this.svr_name = svr_name;
    }

    public String getSvr_host() {
        return svr_host;
    }

    public void setSvr_host(String svr_host) {
        this.svr_host = svr_host;
    }

    public int getSvr_port() {
        return svr_port;
    }

    public void setSvr_port(int svr_port) {
        this.svr_port = svr_port;
    }


    public int getSvr_sort() {
        return svr_sort;
    }

    public void setSvr_sort(int svr_sort) {
        this.svr_sort = svr_sort;
    }

    public int getSvr_status() {
        return svr_status;
    }

    public void setSvr_status(int svr_status) {
        this.svr_status = svr_status;
    }

    public int getGroup_type() {
        return group_type;
    }

    public void setGroup_type(int group_type) {
        this.group_type = group_type;
    }

    public String getSvr_label() {
        return svr_label;
    }

    public void setSvr_label(String svr_label) {
        this.svr_label = svr_label;
    }

    public int getRegister_num() {
        return register_num;
    }

    public void setRegister_num(int register_num) {
        this.register_num = register_num;
    }
}
