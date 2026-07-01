package com.kits.project.clientlog.uploader.domain;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author gaozhaoguang
 * @desc ClientLogModel
 * @date Created on 2021/6/17 17:44
 **/
public class ClientLogModel {

    private MultipartFile stack;
    private String logPath;
    private String time;
    private String mem_free;
    private String mem_used;
    private String version;
    private String playerid;
    private String playername;
    private String game;
    private String user;
    private String uuid;
    private String miei;
    private String idfa;


    public MultipartFile getStack() {
        return stack;
    }

    public void setStack(MultipartFile stack) {
        this.stack = stack;
    }

    public String getLogPath() {
        return logPath;
    }

    public void setLogPath(String logPath) {
        this.logPath = logPath;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMem_free() {
        return mem_free;
    }

    public void setMem_free(String mem_free) {
        this.mem_free = mem_free;
    }

    public String getMem_used() {
        return mem_used;
    }

    public void setMem_used(String mem_used) {
        this.mem_used = mem_used;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getPlayerid() {
        return playerid;
    }

    public void setPlayerid(String playerid) {
        this.playerid = playerid;
    }

    public String getPlayername() {
        return playername;
    }

    public void setPlayername(String playername) {
        this.playername = playername;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getMiei() {
        return miei;
    }

    public void setMiei(String miei) {
        this.miei = miei;
    }

    public String getIdfa() {
        return idfa;
    }

    public void setIdfa(String idfa) {
        this.idfa = idfa;
    }
}
