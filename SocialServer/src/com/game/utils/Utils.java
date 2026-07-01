package com.game.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

/**
 * @Desc TODO
 * @Date 2021/7/26 17:06
 * @Auth ZUncle
 */
public class Utils {

    public static String getChatTableName(String name) {
        return "2&_" + name;
    }

    public static <T> T randomItem(Collection<T> collection) {
        if (collection == null || collection.isEmpty()) {
            return null;
        }
        int t = (int) (collection.size() * ThreadLocalRandom.current().nextDouble());
        int i = 0;
        for (Iterator<T> item = collection.iterator(); i <= t && item.hasNext(); ) {
            T next = item.next();
            if (i == t) {
                return next;
            }
            i++;
        }
        return null;
    }

    /**
     * 包含最大最小值
     * 九 零一起 玩 www.90  175.com
     * @param min
     * @param max
     * @return
     */
    public static int random(int min, int max) {
        if (max - min <= 0) {
            return min;
        }
        return min + ThreadLocalRandom.current().nextInt(max - min + 1);
    }


    public static <T> List<T> find(Collection<T> c, Function<T,Boolean> filter) {
        List<T> list = new ArrayList<>();
        for (T t: c) {
            if (filter.apply(t)) {
                list.add(t);
            }
        }
        return list;
    }
    public static <T> List<T> split(Collection<T> c, Function<T,Boolean> filter) {
        List<T> list = new ArrayList<>();
        Iterator<T> iterator = c.iterator();
        while (iterator.hasNext()){
            T next = iterator.next();
            if (filter.apply(next)) {
                list.add(next);
                iterator.remove();
            }
        }
        return list;
    }
    public static <T> T findOne(Collection<T> c, Function<T,Boolean> filter) {
        for (T t: c) {
            if (filter.apply(t)) {
                return t;
            }
        }
        return null;
    }
    public static <T> T findOne(T[] c, Function<T,Boolean> filter) {
        for (T t: c) {
            if (filter.apply(t)) {
                return t;
            }
        }
        return null;
    }

}
