package com.game.count.manager;

import com.game.count.structs.*;
import com.game.player.structs.Player;
import com.game.utils.ServerParamUtil;
import game.core.util.TimeUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author admin
 */
public class CountManager {

    private CountManager() {

    }

    private enum Singleton {
        INSTANCE;
        CountManager processor;

        Singleton() {
            this.processor = new CountManager();
        }

        CountManager getProcessor() {
            return processor;
        }
    }

    public static CountManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }


    public long getVariant(ICount player, VariantType type) {
        String key = getCountKey(BaseCountType.Variant, type.getKey());
        Count count = player.getCounts().get(key);
        if (count == null) {
         return 0;
        }
        count.reset();
        return count.getCount();
    }

    public void setVariant(ICount player, VariantType type, long count) {
        String key = getCountKey(BaseCountType.Variant, type.getKey());
        Count c = getCount(player.getCounts(), key, type.getType());
        c.setHour(type.getHour());
        c.setCount(count);
        c.setLastTime(TimeUtils.Time());
    }

    public boolean getBooleanCountValue(ICount player, BooleanCount count) {
        long value = getVariant(player, count.getVariantType());
        if (value == 0) {
            return false;
        }
        return (value & (1L << count.getKey())) != 0;
    }

    public void setBooleanCountValue(ICount player, BooleanCount count, boolean set) {
        long value = getVariant(player, count.getVariantType());
        if (set) {
            value |= (1L << count.getKey());
        } else {
            value &= (~(1L << count.getKey()));
        }
        setVariant(player, count.getVariantType(), value);
    }
    public void addVariant(ICount player, VariantType type, int hour, long addCount) {
        String key = getCountKey(BaseCountType.Variant, type.getKey());
        Count c = getCount(player.getCounts(), key, type.getType());
        c.setHour(hour);
        c.setCount(c.getCount() + addCount);
        c.setLastTime(TimeUtils.Time());
    }

    public void addVariant(ICount player, VariantType type, long addCount) {
        addVariant(player, type, type.getHour(), addCount);
    }

    public long getCount(ICount player, BaseCountType type, long key) {
        return getCount(player.getCounts(), getCountKey(type, key));
    }

    public void setCount(ICount player, BaseCountType type, long key, Count.RefreshType refreshType, long count) {
        setCount(player.getCounts(), getCountKey(type, key), refreshType, count);
    }

    public void addCount(ICount player, BaseCountType type, long key, Count.RefreshType refreshType, long addCount) {
        addCount(player.getCounts(), getCountKey(type, key), refreshType, addCount);
    }
    public long getServerVariant(VariantType type) {
        return getVariant(() -> ServerParamUtil.counts, type);
    }
    public void setServerVariant(VariantType type, int num) {
        setVariant(()->ServerParamUtil.counts, type, num);
        ServerParamUtil.saveCounts();
    }
    public void addServerVariant(VariantType type, int addNum) {
        addVariant(()->ServerParamUtil.counts, type, addNum);
        ServerParamUtil.saveCounts();
    }
    public int getServerCount(BaseCountType type, long id) {
        String key = getCountKey(type, id);
        return (int) getCount(ServerParamUtil.counts, key);
    }
    public void addServerCount(BaseCountType type, Count.RefreshType countType, long id, int addNum) {
        String key = getCountKey(type, id);
        addCount(ServerParamUtil.counts, key, countType, addNum);
        ServerParamUtil.saveCounts();
    }

    public void setServerCount(BaseCountType type, Count.RefreshType countType, long id, int num) {
        String key = CountManager.getCountKey(type, id);
        setCount(ServerParamUtil.counts, key, countType, num);
        ServerParamUtil.saveCounts();
    }

    private long getCount(Map<String, Count> counts, String key) {
        Count c = counts.get(key);
        if (c == null) {
            return 0;
        }
        c.reset();
        return c.getCount();
    }

    private void setCount(Map<String, Count> counts, String key, Count.RefreshType refreshType, long count) {
        Count c = getCount(counts, key, refreshType);
        c.setCount(count);
        c.setLastTime(TimeUtils.Time());
    }

    private void addCount(Map<String, Count> counts, String key, Count.RefreshType refreshType, long addCount) {
        Count c = getCount(counts, key, refreshType);
        c.setCount(c.getCount() + addCount);
        c.setLastTime(TimeUtils.Time());
    }

    private static String getCountKey(BaseCountType type, long key) {
        return type.getValue() + "_" + key;
    }

    private Count getCount(Map<String, Count> counts, String key, Count.RefreshType refreshType) {
        Count c = counts.get(key);
        if (c == null) {
            c = new Count();
            c.setKey(key);
            c.setType(refreshType);
            counts.put(key, c);
        } else {
            c.setType(refreshType);
            c.reset();
        }
        return c;
    }

}
