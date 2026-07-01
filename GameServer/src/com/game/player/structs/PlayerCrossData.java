package com.game.player.structs;

import game.core.map.Position;
import game.core.util.CrossState;
import game.core.util.TimeUtils;

/**
 * Created by 542 on 2019/11/14.
 */
public class PlayerCrossData {

    public   long synFightAttTime = 0;//同步战斗服的属性的上一次时间值
    public  boolean toFightServer = false;//是否进入了战斗服
    public  int toFightSid = 0;//战斗服的serverId ;
    public  int toZoneModelId = 0;//跨服副本 ;
    public  long toFightId = 0;//在当前战斗服的场景战斗ID（房间ID）
    public  Position toFightPos; //战斗服的坐标
    public  int fightCampNo = 0;//战场的阵营编号
    public  String platSid = "";//战场同步回游戏服的验证
    public  boolean isReqFight = false;//是否请求过战斗服
    public  long reqFightTime = 0;//请求进入战斗服的时间
    public  int crossState = CrossState.PCS_LOCAL;//跨服的状态机

    public boolean isToFightServer() {
        return toFightServer;
    }

    public void setToFightServer(boolean toFightServer) {
        this.toFightServer = toFightServer;
    }

    public boolean isCrossReqFight() {
        if (isReqFight) {
            long now = TimeUtils.Time();
            if (now - reqFightTime < 60000) {
                return true;
            }
        }
        return false;
    }
}
