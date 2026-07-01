package com.game.equip.struct;

import java.util.concurrent.ConcurrentHashMap;

public class SpiritData {

    /**
     * 灵体激活的最高阶数
     */
    private int spiritId;

    /**
     * 解封进度
     */
    private int cfgId;

    /**
     * 装备收集数据<灵体id, 收集装备list>
     */
    private ConcurrentHashMap<Integer, SpiritInfo> spiritInfoMap = new ConcurrentHashMap<>();

    /**
     * 点亮的星数配置表Id
     */
    private int openStar;

    public int getSpiritId() {
        return spiritId;
    }

    public void setSpiritId(int spiritId) {
        this.spiritId = spiritId;
    }

    public int getCfgId() {
        return cfgId;
    }

    public void setCfgId(int cfgId) {
        this.cfgId = cfgId;
    }

    public ConcurrentHashMap<Integer, SpiritInfo> getSpiritInfoMap() {
        return spiritInfoMap;
    }

    public void setSpiritInfoMap(ConcurrentHashMap<Integer, SpiritInfo> spiritInfoMap) {
        this.spiritInfoMap = spiritInfoMap;
    }

    public int getOpenStar() {
        return openStar;
    }

    public void setOpenStar(int openStar) {
        this.openStar = openStar;
    }
}
