package com.game.worldanswer.structs;

import io.netty.channel.ChannelHandlerContext;

/**
 * Created by clc on 2019/7/15.
 */
public class Answer {

    private long id;//玩家Id
    private boolean isInPanel = false;//是否在答题界面内
    private String name;//玩家名称
    private int  integral = 0;//答题积分
    private long  exp = 0; //获得经验
    private int   level = 0;
    private int  gold = 0;//金币
    private int  syncAnswerCount = 0;//每题答案同步到其他玩家的计数，不得超过15条
    private ChannelHandlerContext context = null;// 保存一个玩家从普通服发过来的类似netPid，方便直接从公共服直接发给客户端



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIsInPanel(boolean isInPanel){this.isInPanel = isInPanel;}

    public  boolean getIsInPanel(){return isInPanel;}

    public void setAddIntegral(int integral){this.integral+=integral;}

    public int getIntegral(){return  integral;}

    public void setSyncAnswerCount(int syncAnswerCount){this.syncAnswerCount = syncAnswerCount;}

    public int getSyncAnswerCount(){return  syncAnswerCount;}

    public void setExp(long exp){this.exp = exp;}

    public void setAddExp(long exp){this.exp += exp;}

    public long getExp(){return exp;}

    public void  setAddGold(int gold){this.gold += gold;}

    public int getGold(){return  gold;}

    public void setContext(ChannelHandlerContext context){this.context = context;}

    public ChannelHandlerContext getContext(){return  context;}

    public void  setLevel(int level){this.level = level;}

    public int getLevel(){return level;}
}
