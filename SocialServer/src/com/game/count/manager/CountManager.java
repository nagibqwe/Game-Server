package com.game.count.manager;

import com.game.count.structs.*;
import com.game.manager.Manager;
import com.game.server.struct.ServerParams;
import game.core.util.TimeUtils;

import java.util.HashMap;

/**
 * @Desc TODO
 * @Date 2020/11/17 9:58
 * @Auth ZUncle
 */
public class CountManager {

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {

        INSTANCE;
        CountManager manager;

        Singleton() {
            this.manager = new CountManager();
        }

        CountManager getProcessor() {
            return manager;
        }
    }

    public static CountManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    String covert(CountBase base, long id) {
        return base.getValue() + "_" + id;
    }

    public long getServerVariant(CountVariant cv) {
        return getVariant(Manager.serverManager, cv);
    }

    public void setServerVariant(CountVariant cv, long value) {
        setVariant(Manager.serverManager, cv, value);
        Manager.serverManager.server().saveServerParams(ServerParams.ServerCounts);
    }

    public long getVariant(ICount ic, CountVariant cv) {
        return getVariant(ic, CountBase.Variant, cv.getKey());
    }

    public void setVariant(ICount ic, CountVariant cv, long value) {
        setVariant(ic, CountBase.Variant, cv.getReset(), cv.getHour(), cv.getMinute(), cv.getSecond(), cv.getKey(), value);
    }

    public void addVariant(ICount ic, CountVariant cv, long value) {
        long variant = getVariant(ic, cv);
        setVariant(ic, cv, value + variant);
    }

    public void setVariant(ICount ic, CountBase cb, CountReset cr, long id, long value) {
        setVariant(ic, cb, cr, cb.getHour(), id, value);
    }

    public long getVariant(ICount ic, CountBase cb, long id) {
        String key = covert(cb, id);
        Count count = ic.getCounts().get(key);
        if (count == null) {
            return 0;
        }
        count.reset();
        return count.getCount();
    }

    /**
     * 获取计数集合
     * 九  零一起玩 www.90  175.com
     * @param ic
     * @param cb
     * @param id
     * @return
     */
    public HashMap<Long, Long> getHashValue(ICount ic, CountBase cb, long id) {
        String key = covert(cb, id);
        Count count = ic.getCounts().get(key);
        if (count == null) {
            count = new Count();
            count.setKey(key);
            ic.getCounts().put(count.getKey(), count);
        }
        count.reset();
        return count.getNote();
    }

    public void setVariant(ICount ic, CountBase cb, CountReset cr, int hour, long id, long value) {
        setVariant(ic, cb, cr, hour, 0, 0, id, value);
    }

    public void setVariant(ICount ic, CountBase cb, CountReset cr, int hour, int minute, int second, long id, long value) {
        String key = covert(cb, id);
        Count count = ic.getCounts().get(key);
        if (count == null) {
            count = new Count();
            count.setKey(key);
            ic.getCounts().put(count.getKey(), count);
        }
        count.setType(cr);
        count.setHour(hour);
        count.setMinute(minute);
        count.setSecond(second);
        count.reset();
        count.setCount(value);
        count.setLastTime(TimeUtils.Time());
    }
}
