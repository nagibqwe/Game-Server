package com.game.count.structs;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @Desc TODO
 * @Date 2020/9/9 19:42
 * @Auth ZUncle
 */
public interface ICount {

    /**
     * 获取技术数据
     * @return
     */
    ConcurrentHashMap<String, Count> getCounts();

}
