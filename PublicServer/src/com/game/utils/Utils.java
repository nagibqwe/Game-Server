package com.game.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

/**
 * @Desc TODO
 * @Date 2020/11/17 13:54
 * @Auth ZUncle
 */
public class Utils {

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

    public static <T> List<T> find(Collection<T> cc, Function<T,Boolean> filter) {
        List<T> list = new ArrayList<>();
        for (T t: cc) {
            if (filter.apply(t)) {
                list.add(t);
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
