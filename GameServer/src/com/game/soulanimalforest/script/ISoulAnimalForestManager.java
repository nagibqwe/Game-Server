package com.game.soulanimalforest.script;

import com.game.boss.struct.BossData;
import com.game.map.structs.MapObject;
import com.game.player.structs.Player;
import com.game.soulanimalforest.structs.CrossBossData;
import game.core.script.IScript;
import game.message.SoulAnimalForestMessage;

import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author xuchangming <xysoko@qq.com>
 */
public interface ISoulAnimalForestManager extends IScript {

    /**
     * 处理请求本地副本的面板信息
     */
    void onReqSoulAnimalForestLocalPanel(Player player, SoulAnimalForestMessage.ReqSoulAnimalForestLocalPanel messInfo);

    /**
     * 处理请求跨服副本的面板信息
     */
    void onReqSoulAnimalForestCrossPanel(Player player, SoulAnimalForestMessage.ReqSoulAnimalForestCrossPanel messInfo);



    //处理跨服过来的数据值

    void onP2FResSoulAnimalForestBossInfo(SoulAnimalForestMessage.P2FResSoulAnimalForestBossInfo messInfo);

    public void onP2FUpdateOneSoulAnimalForestBossInfo(SoulAnimalForestMessage.P2FUpdateOneSoulAnimalForestBossInfo messInfo);

    public void onP2FResCloneMonsterDie(SoulAnimalForestMessage.P2FResCloneMonsterDie messInfo);

    void onP2GResSoulAnimalForestCrossBossRefreshTip(SoulAnimalForestMessage.P2GResSoulAnimalForestCrossBossRefreshTip messInfo);
    
    /**
     * 加载跨服的BOSS数据
     */
    void crossBossInfoInit();

    /**
     * 加载魂兽数据
     */
    void load();

    public void calcAnimalIslandBossBirth();

    void syncSoulAnilmasnfo(MapObject map, int bossId,int type);

    public void createMonster(MapObject mapObject, ConcurrentHashMap<Integer, Integer> cristal, int type, BossData bd, int maxNum);

    public void createCrossMonster(MapObject mapObject, ConcurrentHashMap<Integer, Integer> cristal, int type, CrossBossData bd);

}
