/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.utils;

import com.game.map.structs.*;
import com.game.structs.Position;
import game.message.CommonMessage;

/**
 *
 * @author hewei@haowan123.com
 */
public class Utils {

    public static String makeRandomCode() {
        return makeRandomCode(RandomUtils.random(2, 6));
    }

    public static String makeRandomCode(int num) {
        String[] str = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
        String inviterid = "";
        int one = 0;
        int length = str.length;
        for (int i = 0; i < num; i++) {
            one = RandomUtils.random(length);
            inviterid += str[one];
        }
        return inviterid;
    }

    public static MapPeople makeMapPeople(CommonMessage.PlayerInfo info) {
        MapPeople mapPeople = new MapPeople();
        mapPeople.setId(info.getPlayerId());
        mapPeople.getCurPos().setX(info.getX());
        mapPeople.getCurPos().setY(info.getY());
        mapPeople.setName(info.getName());
//        mapPeople.setCampNo(info.getCamp());
        mapPeople.setSpeed(info.getMoveSpeedFinal()/100f);
        return mapPeople;
    }

    public static MapMonster makeMapMonster(CommonMessage.MonsterInfo info) {
        MapMonster mapMonster = new MapMonster();
        mapMonster.setId(info.getId());
        mapMonster.setModelId(info.getDataID());
        mapMonster.getCurPos().setX(info.getX());
        mapMonster.getCurPos().setY(info.getY());
        mapMonster.setCampNo(info.getCamp());
        mapMonster.setSpeed(info.getMoveSpeedFinal()/100f);
        mapMonster.setName(String.valueOf(info.getDataID()));
        return mapMonster;
    }

    public static MapGather makeMapGather(CommonMessage.GatherInfo info) {
        MapGather mapGather = new MapGather();
        mapGather.setId(info.getGatherId());
        mapGather.setModelId(info.getDataID());
        mapGather.getCurPos().setX(info.getX());
        mapGather.getCurPos().setY(info.getY());
        mapGather.setCampNo(0);
        return mapGather;
    }

    public static MapNPC makeMapNPC(CommonMessage.NpcInfo info) {
        MapNPC mapNPC = new MapNPC();
        mapNPC.setId(info.getNpcId());
        mapNPC.setModelId(info.getDataID());
        mapNPC.getCurPos().setX(info.getX());
        mapNPC.getCurPos().setY(info.getY());
        mapNPC.setCampNo(0);
        return mapNPC;
    }

    public static MapPet makeMapPet(CommonMessage.PetInfo info) {
        MapPet mapPet = new MapPet();
        mapPet.setId(info.getId());
        mapPet.setModelId(info.getDataID());
        mapPet.getCurPos().setX(info.getX());
        mapPet.getCurPos().setY(info.getY());
        mapPet.setCampNo(0);
        return mapPet;
    }

    /**
     * 两点之间的距离
     *
     * @param pos1
     * @param pos2
     * @return
     */
    public static float getDistance(Position pos1, Position pos2) {
        return (float)Math.sqrt((pos1.getX() - pos2.getX()) * (pos1.getX() - pos2.getX()) + (pos1.getY() - pos2.getY()) * (pos1.getY() - pos2.getY()));
    }
    
        //获取一个单位向量
    public static Position getDir(Position begin, Position end) {
        float distance = getDistance(begin, end);
        if (distance == 0f) {
            return new Position(0, -1);
        }
        float x = (end.getX() - begin.getX()) / distance;
        float y = (end.getY() - begin.getY()) / distance;
        return new Position(x, y);
    }

    public static boolean isIDEEnvironment() {
        String val = System.getProperty("ideDebug");
        return val != null && val.equals("true");
    }
}
