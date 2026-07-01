/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.gold.manager;

import com.game.db.dao.GoldDao;
import com.game.gold.structs.Gold;
import com.game.player.structs.Player;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author hewei
 */
public class GoldManager {

    //账号充值数据；一个账号在一个服下充值通用；key=userId_createServerId；账号id和角色所创建服的id
    private static final ConcurrentHashMap<String, Gold> golds = new ConcurrentHashMap<>();

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {

        INSTANCE;                   //一个枚举的元素，它就代表了Singleton的一个实例//一个枚举的元素，它就代表了Singleton的一个实例
        GoldManager manager;

        Singleton() {
            this.manager = new GoldManager();
        }

        GoldManager getProcessor() {
            return manager;
        }
    }

    //获取GoldManager的实例对象
    public static GoldManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    /**
     * 启服加载元宝数据到内存
     */
    public void initGolds() {
        GoldDao dao = new GoldDao();
        List<Gold> list = dao.selectAll();
        if (list == null) {
            return;
        }
        for (Gold gold : list) {
            String key = makeGoldKey(gold.getUserId(), gold.getServerId());
            golds.put(key, gold);
        }
    }

    /**
     * 账号登录发送初始化gold
     *
     * @param roleid
     * @param loginServerid
     * @param platformName
     */
    public void initGold(long roleid, int loginServerid, String platformName) {
        String key = makeGoldKey(roleid, loginServerid);
        Gold gold = golds.get(key);
        if (gold == null) {
            gold = new Gold();
            gold.setServerId(loginServerid);
            gold.setUserId(roleid);
            gold.setPlatformName(platformName);
            golds.put(key, gold);
        }
    }

    /**
     * 玩家上线获取元宝数据
     *
     * @param player
     */
    public void playerOnLine(Player player) {
        String key = makeGoldKey(player.getId(), player.getCreateServerId());
        Gold gold = golds.get(key);
        if (gold == null) {
            gold = new Gold();
            gold.setServerId(player.getCreateServerId());
            gold.setUserId(player.getId());
            gold.setPlatformName(player.getPlatformName());
            golds.put(key, gold);
        }

        if (gold.getPlatformName().length() < 1) {
            gold.setPlatformName(player.getPlatformName());
        }

        player.setGold(gold);
    }

    public static String makeGoldKey(long userId, int createServerId) {
        return userId + "_" + createServerId;
    }

    public Gold GetGold(Player player) {
        String key = makeGoldKey(player.getUserId(), player.getCreateServerId());
        return golds.get(key);
    }

    public Gold GetGold(long userid, int createSid) {
        String key = makeGoldKey(userid, createSid);
        return golds.get(key);
    }
}
