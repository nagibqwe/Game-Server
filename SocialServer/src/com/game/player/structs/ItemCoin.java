package com.game.player.structs;

/**
 * @Desc TODO
 * @Date 2021/8/11 14:37
 * @Auth ZUncle
 */
public enum  ItemCoin {
    Gem(1),             //灵玉
    Exp(8),             //经验
    Gold(12),           //元宝
    Popularity(35),     //仙府人气
    HouseCoin(36),      //仙府币
    ;
    final int coin;

    ItemCoin(int coin) {
        this.coin = coin;
    }

    public int getCoin() {
        return coin;
    }
}
