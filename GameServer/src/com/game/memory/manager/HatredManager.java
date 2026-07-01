/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.memory.manager;

import com.game.structs.Hatred;
import game.core.pool.MemoryPool;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger; 

/**
 *
 * @author zenghai <zenghai@haowan123.com>
 */
public class HatredManager {

    private static final Logger log = LogManager.getLogger(HatredManager.class);

    private final MemoryPool<Hatred> pool = new MemoryPool<>(10000);

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {

        INSTANCE;
        HatredManager manager;

        Singleton() {
            this.manager = new HatredManager();
        }

        HatredManager getProcessor() {
            return manager;
        }
    }

    //HatredManager
    public static HatredManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    public Hatred getHatred() {
        try {
            return pool.get(Hatred.class);
        } catch (IllegalAccessException | InstantiationException e) {
            log.error(e, e);
        }
        return null;
    }

    public void clearHatred(Hatred hatred) {
        if (hatred == null) {
            log.error("仇恨对象为空", new NullPointerException());
            return;
        }

        hatred.release();
        pool.put(hatred);
    }

}
