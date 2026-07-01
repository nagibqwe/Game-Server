package com.game.cooldown.manager;

import com.game.cooldown.structs.Cooldown;
import com.game.cooldown.structs.CooldownTypes;
import com.game.cooldown.structs.ICoolDown;
import game.core.pool.MemoryPool;
import game.core.util.TimeUtils;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * @author Administrator
 */
public class CooldownManager {

    protected Logger log = LogManager.getLogger(CooldownManager.class);
    private final MemoryPool<Cooldown> cooldownPool = new MemoryPool<>(100000);

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

    /**
     * 给玩家添加冷却
     *
     * @param player
     * @param type   类型
     * @param key    关键字
     * @param delay  冷却时间
     */
    public void addCooldown(ICoolDown player, CooldownTypes type, Object key, long delay) {
        //初始化冷却关键字
        String cooldownKey = null;
        if (key == null) {
            cooldownKey = type.getValue();
        } else {
            cooldownKey = type.getValue() + "_" + key;
        }
        if (player.getCooldowns().containsKey(cooldownKey)) {
            Cooldown cooldown = player.getCooldowns().get(cooldownKey);
            cooldown.setStart(TimeUtils.Time());
            cooldown.setDelay(delay);
        } else {
            //初始化冷却信息
            Cooldown cooldown = createCooldown();
            cooldown.setType(type.getValue());
            cooldown.setKey(cooldownKey);
            cooldown.setStart(TimeUtils.Time());
            cooldown.setDelay(delay);

            //添加冷却
            player.getCooldowns().put(cooldownKey, cooldown);
        }
    }

    /**
     * 给玩家添加冷却
     *
     * @param player
     * @param type   类型
     * @param key    关键字
     * @return
     */
    public long getCooldownTime(ICoolDown player, CooldownTypes type, Object key) {
        // 初始化冷却关键字
        String cooldownKey = null;
        if (key == null) {
            cooldownKey = type.getValue();
        } else {
            cooldownKey = type.getValue() + "_" + key;
        }
        Cooldown cooldown = player.getCooldowns().get(cooldownKey);
        if (cooldown != null) {
            return cooldown.getRemainTime();
        }
        return 0;
    }

    /**
     * 获取CD 对象
     * @param player
     * @param type
     * @param key
     * @return
     */
    public Cooldown getCooldown(ICoolDown player, CooldownTypes type, Object key) {
        // 初始化冷却关键字
        String cooldownKey = null;
        if (key == null) {
            cooldownKey = type.getValue();
        } else {
            cooldownKey = type.getValue() + "_" + key;
        }
        return player.getCooldowns().get(cooldownKey);
    }

    /**
     * 移除冷却
     *
     * @param player
     * @param type   类型
     * @param key    关键字
     */
    public void removeCooldown(ICoolDown player, CooldownTypes type, Object key) {
        //初始化冷却关键字
        String cooldownKey = null;
        if (key == null) {
            cooldownKey = type.getValue();
        } else {
            cooldownKey = type.getValue() + "_" + key;
        }

        //移除冷却
        if (player.getCooldowns().containsKey(cooldownKey)) {
            Cooldown cooldown = player.getCooldowns().remove(cooldownKey);
            cooldownPool.put(cooldown);
        }
    }

    /**
     * 是否在冷却中
     *
     * @param player
     * @param type   冷却类型
     * @param key    关键字
     * @return
     */
    public boolean isCooldowning(ICoolDown player, CooldownTypes type, Object key) {
        return isCooldowning(player, type, key, 100);
    }

    /**
     * 是否在冷却中
     *
     * @param player
     * @param type   冷却类型
     * @param allow
     * @param key    关键字
     * @return
     */
    public boolean isCooldowning(ICoolDown player, CooldownTypes type, Object key, int allow) {
        //初始化冷却关键字
        String cooldownKey = null;
        if (key == null) {
            cooldownKey = type.getValue();
        } else {
            cooldownKey = type.getValue() + "_" + key;
        }

        //查看冷却
        if (player.getCooldowns().containsKey(cooldownKey)) {
            Cooldown cooldown = player.getCooldowns().get(cooldownKey);
            //放宽100毫秒冷却
            return TimeUtils.Time() <= cooldown.getStart() + cooldown.getDelay() - allow;
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

    public void cleanAllCooldown(ICoolDown fighter) {

        if (fighter == null) {
            return;
        }

        if (fighter.getCooldowns().isEmpty()) {
            return;
        }
        List<Cooldown> release = new ArrayList<>(fighter.getCooldowns().values());
        fighter.getCooldowns().clear();

        release.forEach(this::putCooldown);
    }

    //重新压入冷却系统
    void putCooldown(Cooldown cd) {
        cd.release();
        cooldownPool.put(cd);
    }
}
