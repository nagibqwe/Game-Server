/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.guild.structs;

import game.core.util.TimeUtils;
import game.message.GuildMessage.GuildLogInfo;

/**
 * 公会日志
 */
public class GuildLog {

    private int time = (int) (TimeUtils.Time() / 1000);       //时间
    private int type;       //类型
    private String[] info;//log其他参数

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String[] getInfo() {
        return info;
    }

    public void setInfo(String[] info) {
        this.info = info;
    }

    public GuildLogInfo.Builder toGuildLogInfoBuilder() {
        GuildLogInfo.Builder msg = GuildLogInfo.newBuilder();
        msg.setTime(time);
        msg.setType(type);
        //组装数据
        for (String s : info) {
            msg.addStr(s);
        }
        return msg;
    }
}
