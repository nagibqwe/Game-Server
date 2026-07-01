package com.game.server.struct;

/**
 * @Desc TODO
 * @Date 2021/8/9 16:59
 * @Auth ZUncle
 */
public enum  ServerParams {

    ServerCounts("ServerCounts"),   //服务器计数器

    HomeRankTurn("HomeRankTurn"),   //家装大赛赛季

    ;
    final String key;

    ServerParams(String key) {
        this.key = key;
    }

    public final String getKey() {
        return key;
    }
}
