/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.jjc.script;

import com.game.jjc.structs.JJC;
import com.game.player.structs.Player;
import game.core.script.IScript;
import game.message.JJCMessage;
import game.message.JJCMessage.ReqAddChance;
import game.message.JJCMessage.ReqChallenge;
import game.message.JJCMessage.ReqChangeTarget;
import game.message.JJCMessage.ReqGetAward;
import game.message.JJCMessage.ReqGetReport;
import game.message.JJCMessage.ReqGetYesterdayRank;
import game.message.JJCMessage.ReqOpenJJC;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 协议处理接口
 *
 * @author soko <zenghai@haowan123.com>
 */
public interface IJJCHandler extends IScript{

    public void OnReqAddChance(Player player, ReqAddChance mess);

    public void OnReqChallenge(Player player, ReqChallenge mess);

    public void OnReqChangeTarget(Player player, ReqChangeTarget mess);

    public void OnReqGetAward(Player player, ReqGetAward mess);

    public void onReqGetFirstReward(Player player, JJCMessage.ReqGetFirstReward mess);

    public void OnReqGetReport(Player player, ReqGetReport mess);

    public void OnReqGetYesterdayRank(Player player, ReqGetYesterdayRank mess);

    public void OnReqOpenJJC(Player player, ReqOpenJJC mess);
    
    public void Online(Player player);
    
    public void loadAll();
    //排序
    public void sort(int newsort, JJC hero);
    
    //删除玩家的排名
    public void onDelete(Player player, int oldCareer);

    void setPlayerJJCHistoryRank(Player player, int newRank);

    public void sendRewardRedPoint(Player player);

    void sendArenaRankReward(ConcurrentHashMap<Integer, Long> rankMap);


    /**
     * 一键扫荡
     * @param player
     */
    void onReqOneKeySweep(Player player);

}
