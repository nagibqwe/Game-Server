/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.cooldown.manager;

import com.game.cooldown.structs.Cooldown;
import com.game.cooldown.structs.CooldownTypes;
import com.game.map.structs.BaseNpc;
import game.core.pool.MemoryPool;
import game.core.util.TimeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Administrator
 */
public class CooldownManager {
    public final static int LLONG = 0;
    public final static int REPLACE = 1;
    public final static int ADD = 2;

    protected Logger log = LogManager.getLogger(CooldownManager.class);
    private final MemoryPool<Cooldown> cooldownPool = new MemoryPool<>(100000);

    private final ConcurrentHashMap<String, Long> systemcool = new ConcurrentHashMap<>();

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {

        INSTANCE;
        CooldownManager processor;

        Singleton() {
            this.processor = new CooldownManager();
        }

        CooldownManager getProcessor() {
            return processor;
        }
    }

    public static CooldownManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    private String getKey(CooldownTypes type, String key){

        String cooldownKey = null;
        if (key == null) {
            cooldownKey = type.getValue();
        } else {
            cooldownKey = type.getValue() + "_" + key;
        }
        return cooldownKey;
    }

    /**
     * 给怪物添加冷却
     *
     * @param obj
     * @param type    类型
     * @param key     关键字
     * @param delay   冷却时间
     * @param optype   0 留下长的 1 直接替换 2 累加
     */
    public void addCooldown(BaseNpc obj, CooldownTypes type, String key, long delay, int optype) {
        //获得玩家
        if (obj == null) {
            return;
        }

        //初始化冷却关键字
        String cooldownKey = getKey(type, key);

        if (obj.containCooldown(cooldownKey)) {
            Cooldown cooldown = obj.getCooldown(cooldownKey);

            long now = TimeUtils.Time();
            long last = cooldown.getRemainTime();
            // 之前的比现在还长,就不替换了
            if(last < delay || optype == REPLACE){
                cooldown.setStart(now);
                cooldown.setDelay(delay);
            }
            else if(optype == ADD){
                cooldown.setStart(now);
                cooldown.setDelay(last + delay);
            }
        } else {
            //初始化冷却信息
            Cooldown cooldown = createCooldown();
            cooldown.setType(type.getValue());
            cooldown.setKey(cooldownKey);
            cooldown.setStart(TimeUtils.Time());
            cooldown.setDelay(delay);

            //添加冷却
            obj.addCooldown(cooldownKey, cooldown);
        }
    }

    /**
     * 移除冷却
     *
     * @param obj
     * @param type    类型
     * @param key     关键字
     */
    public void removeCooldown(BaseNpc obj, CooldownTypes type, String key) {
        //获得玩家
        if (obj == null) {
            return;
        }

        //初始化冷却关键字
        String cooldownKey = getKey(type, key);


        //移除冷却
        if (obj.containCooldown(cooldownKey)) {
            Cooldown cooldown = obj.removeCooldown(cooldownKey);
            cooldownPool.put(cooldown);
        }
    }

    public long getLastcd(BaseNpc obj, CooldownTypes type, String key){
        //获得玩家
        if (obj == null) {
            return 0;
        }

        //初始化冷却关键字
        String cooldownKey = getKey(type, key);

        if (!obj.containCooldown(cooldownKey)) {
            return 0;
        }

        Cooldown cooldown = obj.getCooldown(cooldownKey);
        if(cooldown == null)
            return 0;
        return cooldown.getRemainTime();
    }

    /**
     * 是否在冷却中
     * @param obj
     * @param type    冷却类型
     * @param key     关键字
     * @return
     */
    public boolean isCooldowning(BaseNpc obj, CooldownTypes type, String key) {

        if (obj == null) {
            return true;
        }

        //初始化冷却关键字
        String cooldownKey = getKey(type, key);

        //查看冷却
        if (obj.containCooldown(cooldownKey)) {
            Cooldown cooldown = obj.getCooldown(cooldownKey);
            return System.currentTimeMillis() <= cooldown.getStart() + cooldown.getDelay(); //冷却时间已经结束
        }
        return false;
    }

    private Cooldown createCooldown() {
        try {
            return cooldownPool.get(Cooldown.class);
        } catch (Exception e) {
            log.error(e, e);
        }
        return null;
    }

    public void cleanAllCooldown(BaseNpc BaseNpc) {

        if (BaseNpc == null) {
            return;
        }

        if (BaseNpc.getCooldowns().isEmpty()) {
            return;
        }

        Iterator<Cooldown> iter = BaseNpc.getCooldowns().iterator();
        while (iter.hasNext()) {
            Cooldown en = iter.next();
            cooldownPool.put(en);//还原到池中去
        }
        BaseNpc.getCooldowns().clear();

    }
}
