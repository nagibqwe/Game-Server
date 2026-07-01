package com.game.cooldown.structs;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @Desc TODO
 * @Date 2021/5/8 18:25
 * @Auth ZUncle
 */
public interface ICoolDown {

    //获取cd
    ConcurrentHashMap<String, Cooldown> getCooldowns();

}
