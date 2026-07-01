package com.game.eightdiagrams.structs;

/**
 * Created by cxl on 2019/9/21.
 * 战况
 */
public class CityBattleProgress {

    private int sid = 0;

    private int playerNum = 0;

    private long bossHurt = 0;

    private int colorCamp = 0;

    private String serverName = "";



    public void setSid(int sid){this.sid = sid;}

    public int getSid(){return  sid;}

    public void setPlayerNum(int playerNum){this.playerNum = playerNum;}

    public int getPlayerNum(){return playerNum;}

    public void setBossHurt(long hurt){this.bossHurt +=hurt;}

    public long getBossHurt(){return bossHurt;}

    public void setColorCamp(int colorCamp){this.colorCamp = colorCamp;}

    public int getColorCamp(){return colorCamp;}

    public void setServerName(String serverName){this.serverName = serverName;}

    public String getServerName(){return serverName;}


}
