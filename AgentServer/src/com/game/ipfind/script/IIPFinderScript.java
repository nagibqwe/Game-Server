package com.game.ipfind.script;

public interface IIPFinderScript {
    /**
     * 根据ip地址获取地区
     * @param ip
     * @return
     */
    int getRegion(String ip);

    /**
     * 加载数据
     */
    void loadData();
}
