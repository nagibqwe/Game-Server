package com.game.count.manager;


import com.game.count.structs.*;
import com.game.utils.ServerParamUtil;
import game.core.util.TimeUtils;

import java.util.concurrent.ConcurrentHashMap;

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
        return CountManager.Singleton.INSTANCE.getProcessor();
    }

    String covert(CountBase base, int id) {
        return base.getValue() + "_" + id;
    }

    public long getVariant(ICount ic, CountVariant cv) {
        return getVariant(ic, CountBase.Variant, cv.getKey());
    }

    public void setVariant(ICount ic, CountVariant cv, long value) {
        setVariant(ic, CountBase.Variant, cv.getReset(), cv.getHour(), cv.getMinute(), cv.getSecond(), cv.getKey(), value);
    }

    public long getServerVariant(CountBase cb, int id) {
        return getVariant(() -> ServerParamUtil.counts, cb, id);
    }

    public void setServerVariant(CountBase cb, CountReset cr, int id, long value) {
        setVariant(() -> ServerParamUtil.counts, cb, cr, cb.getHour(), id, value);
    }

    public void setVariant(ICount ic, CountBase cb, CountReset cr, int id, long value) {
        setVariant(ic, cb, cr, cb.getHour(), id, value);
    }

    public long getVariant(ICount ic, CountBase cb, int id) {
        String key = covert(cb, id);
        Count count = ic.getCounts().get(key);
        if (count == null) {
            return 0;
        }
        count.reset();
        return count.getCount();
    }

    public void setVariant(ICount ic, CountBase cb, CountReset cr, int hour, int id, long value) {
        setVariant(ic, cb, cr, hour, 0, 0, id, value);
    }

    public void setVariant(ICount ic, CountBase cb, CountReset cr, int hour, int minute, int second, int id, long value) {
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
