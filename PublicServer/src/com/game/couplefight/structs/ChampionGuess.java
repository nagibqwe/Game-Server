package com.game.couplefight.structs;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 冠军赛 竞猜
 * @Auther: gouzhongliang
 * @Date: 2021/7/19 17:41
 */
public class ChampionGuess {

    /**玩家-竞猜金额*/
    private Map<Long, Integer> guess = new ConcurrentHashMap<>();


}
