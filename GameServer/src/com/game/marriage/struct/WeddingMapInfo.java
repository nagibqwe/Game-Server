package com.game.marriage.struct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class WeddingMapInfo {
    /**
     * 是否已经扣除婚宴次数
     */
    private boolean cost;
    /**
     * <玩家id，操作数据>
     */
    private ConcurrentHashMap<Long, WeddingOperation> operation = new ConcurrentHashMap<>();
    /**
     * 婚宴分线副本
     */
    final ConcurrentHashMap<Long, Integer> weddingScene = new ConcurrentHashMap<>();

    /**
     * 当前刷新进度
     */
    private HashMap<Long, Integer> curRefreshHot = new HashMap<>();

    /**
     * 妻子ID
     */
    private long wifeID ;

    /**
     * 丈夫ID
     */

    private long hubbyID;
    /**
     * 8人一桌 开席
     */
    private long weddingTime;
    /**
     * 结束时间
     */
    private long overTime;
    /**
     * 进行阶段
     */
    private int stage;

    /**
     * 热度
     */
    private int hot ;

    /**
     * 婚礼唯一ID
     */
    private long weddingID;

    /**
     * 采集糖果最大次数
     */
    private int gatherMax;

    /**
     * 地图BOSS状态
     */
    private boolean bossIsDie;


    /**
     * 妻子是否购买热度
     */
    private boolean wifeIsBuyHot;

    /**
     * 丈夫是否购买热度
     */

    private boolean hubbyIsBuyHot;

    private List<Long> addhotList = new ArrayList<>();


    private List<WeddingSendData> weddingSendDatas = new ArrayList<>();


    ////////////////////////getter and setter//////////////////////////////////////////

    public boolean isBossIsDie() {
        return bossIsDie;
    }

    public void setBossIsDie(boolean bossIsDie) {
        this.bossIsDie = bossIsDie;
    }

    public List<WeddingSendData> getWeddingSendDatas() {
        return weddingSendDatas;
    }

    public void setWeddingSendDatas(List<WeddingSendData> weddingSendDatas) {
        this.weddingSendDatas = weddingSendDatas;
    }

    public List<Long> getAddhotList() {
        return addhotList;
    }

    public void setAddhotList(List<Long> addhotList) {
        this.addhotList = addhotList;
    }

    public HashMap<Long, Integer> getCurRefreshHot() {
        return curRefreshHot;
    }

    public void setCurRefreshHot(HashMap<Long, Integer> curRefreshHot) {
        this.curRefreshHot = curRefreshHot;
    }

    public int getGatherMax() {
        return gatherMax;
    }

    public void setGatherMax(int gatherMax) {
        this.gatherMax = gatherMax;
    }

    public long getWeddingID() {
        return weddingID;
    }

    public void setWeddingID(long weddingID) {
        this.weddingID = weddingID;
    }

    public int getHot() {
        return hot;
    }

    public void setHot(int hot) {
        this.hot = hot;
    }

    public int getStage() {
        return stage;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }

    public long getOverTime() {
        return overTime;
    }

    public void setOverTime(long overTime) {
        this.overTime = overTime;
    }

    public long getWifeID() {
        return wifeID;
    }

    public void setWifeID(long wifeID) {
        this.wifeID = wifeID;
    }

    public long getHubbyID() {
        return hubbyID;
    }

    public void setHubbyID(long hubbyID) {
        this.hubbyID = hubbyID;
    }

    /**
     * 获取 是否已经扣除婚宴次数
     *
     * @return cost 是否已经扣除婚宴次数
     */
    public boolean isCost() {
        return this.cost;
    }

    /**
     * 设置 是否已经扣除婚宴次数
     *
     * @param cost 是否已经扣除婚宴次数
     */
    public void setCost(boolean cost) {
        this.cost = cost;
    }

    /**
     * 获取 <玩家id，操作数据>
     *
     * @return operation <玩家id，操作数据>
     */
    public ConcurrentHashMap<Long, WeddingOperation> getOperation() {
        return this.operation;
    }

    /**
     * 设置 <玩家id，操作数据>
     *
     * @param operation <玩家id，操作数据>
     */
    public void setOperation(ConcurrentHashMap<Long, WeddingOperation> operation) {
        this.operation = operation;
    }

    public boolean getWifeIsBuyHot() {
        return wifeIsBuyHot;
    }

    public void setWifeIsBuyHot(boolean wifeIsBuyHot) {
        this.wifeIsBuyHot = wifeIsBuyHot;
    }

    public boolean getHubbyIsBuyHot() {
        return hubbyIsBuyHot;
    }

    public void setHubbyIsBuyHot(boolean hubbyIsBuyHot) {
        this.hubbyIsBuyHot = hubbyIsBuyHot;
    }

    public ConcurrentHashMap<Long, Integer> getWeddingScene() {
        return weddingScene;
    }

    public long getWeddingTime() {
        return weddingTime;
    }

    public void setWeddingTime(long weddingTime) {
        this.weddingTime = weddingTime;
    }
}
