package com.game.luckydraw.structs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author gaozhaoguang
 * @desc 一周福利抽奖数据
 * @date Created on 2020/8/20 17:28
 **/
public class LuckyDrawWeekData {
    /**
     * 角色的抽奖记录
     */
    private final ConcurrentHashMap<Long, List<LuckyDrawRecord>> roleRecords = new ConcurrentHashMap<>();
    /**
     * 服务器的抽奖记录
     */
    private final List<LuckyDrawRecord> serverRecords = new ArrayList<>();

    /**
     * 当前默认奖励索引的列表,其中key表示奖励类型
     */
    private final ConcurrentHashMap<Integer,List<Integer>> awards = new ConcurrentHashMap<>();

    /**
     *角色当前是否打开抽奖面板
     */
    private final ConcurrentHashMap<Long,Boolean> playerOpenPanels = new ConcurrentHashMap<>();

    public ConcurrentHashMap<Long, List<LuckyDrawRecord>> getRoleRecords() {
        return roleRecords;
    }

    public List<LuckyDrawRecord> getServerRecords() {
        return serverRecords;
    }

    public ConcurrentHashMap<Integer, List<Integer>> getAwards() {
        return awards;
    }

    public ConcurrentHashMap<Long, Boolean> getPlayerOpenPanels() {
        return playerOpenPanels;
    }
}
