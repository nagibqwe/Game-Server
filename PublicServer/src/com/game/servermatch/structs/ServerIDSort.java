package com.game.servermatch.structs;

import com.game.gameserver.manager.GameServerManager;

import java.util.Comparator;

/**
 * Created by 542 on 2019/8/28.
 */
public class ServerIDSort implements Comparator<GameServerInfo> {


    @Override
    public int compare(GameServerInfo g1,GameServerInfo g2)
    {
        long opentime1 =   GameServerManager.getOpenServerTime(g1.getOpenTime());
        long opentime2 =   GameServerManager.getOpenServerTime(g2.getOpenTime());
        if ( opentime1 > opentime2){
            return 1;
        }else if (opentime1 == opentime2){
            if (g1.getServerId() < g2.getServerId()){
                return 1;
            }
        }
        return -1;
    }
}
