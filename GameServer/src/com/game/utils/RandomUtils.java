
package com.game.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

/**
 * 随机数工具
 *
 * @author soko
 */
public class RandomUtils {

    private static final Logger logger = LogManager.getLogger(RandomUtils.class);

    public static int random(int max) {
        return ThreadLocalRandom.current().nextInt(max);
    }

    /**
     * 包含最大最小值
     *
     * @param min
     * @param max
     * @return
     */
    public static long randomLong(long min, long max) {
        if (max - min <= 0) {
            return min;
        }
        return min + ThreadLocalRandom.current().nextLong(max - min + 1);
    }

    /**
     * 包含最大最小值
     *
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

    /**
     * 根据几率 计算是否生成
     *
     * @param probability
     * @param gailv
     * @return
     */
    public static boolean isGenerate(int probability, int gailv) {
        if (gailv == 0) {
            gailv = 1000;
        }
        int random_seed = ThreadLocalRandom.current().nextInt(gailv + 1);
        return probability >= random_seed;
    }

    /**
     * gailv/probability 比率形式
     *
     * @param probability
     * @param gailv
     * @return
     */
    public static boolean isGenerate2(int probability, int gailv) {
        if (probability == gailv) {
            return true;
        }
        if (gailv == 0) {
            return false;
        }
        int random_seed = ThreadLocalRandom.current().nextInt(probability);
        return random_seed + 1 <= gailv;
    }

    /**
     * 根据几率 计算是否生成
     *
     * @param probability
     * @return
     */
    public static boolean defaultIsGenerate(int probability) {
        @SuppressWarnings("static-access")
        int random_seed = ThreadLocalRandom.current().nextInt(10000);
        return probability >= random_seed + 1;
    }

    /**
     * 从 min 和 max 中间随机一个值
     *
     * @param max
     * @param min
     * @return 包含min max
     */
    public static int randomValue(int max, int min) {
        int temp = max - min;
        temp = ThreadLocalRandom.current().nextInt(temp + 1);
        temp = temp + min;
        return temp;
    }

    /**
     * 返回在0-maxcout之间产生的随机数时候小于num
     *
     * @param num
     * @param maxcout
     * @return
     */
    public static boolean isGenerateToBoolean(float num, int maxcout) {
        double count = ThreadLocalRandom.current().nextInt() * maxcout;

        return count < num;
    }

    /**
     * 返回在0-maxcout之间产生的随机数时候小于num
     *
     * @param num
     * @param maxcout
     * @return
     */
    public static boolean isGenerateToBoolean(int num, int maxcout) {
        double count = ThreadLocalRandom.current().nextDouble() * maxcout;

        return count < num;
    }

    /**
     * 随机产生min到max之间的整数值 包括min max
     *
     * @param min
     * @param max
     * @return
     */
    public static int randomIntValue(int min, int max) {
        return (int) (ThreadLocalRandom.current().nextDouble() * (max - min + 1)) + min;
    }

    public static float randomFloatValue(float min, float max) {
        if (min > max) {
            return (float) (ThreadLocalRandom.current().nextDouble() * (double) (min - max)) + max;
        }
        return (float) (ThreadLocalRandom.current().nextDouble() * (double) (max - min)) + min;
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
     * @param probs 根据总机率返回序号
     * @return
     */
    public static int randomIndexByProb(List<Integer> probs) {
        try {
            LinkedList<Integer> newprobs = new LinkedList<>();
            for (int i = 0; i < probs.size(); i++) {
//				if (probs.get(i) > 0) {
                if (i == 0) {
                    newprobs.add(probs.get(i));
                } else {
                    newprobs.add(newprobs.get(i - 1) + probs.get(i));
                }
//				}
            }
            if (newprobs.size() <= 0) {
                return -1;
            }
            int last = newprobs.getLast();
//			String[] split = last.split(Symbol.XIAHUAXIAN_REG);
            int randomValue = random(last);
            for (int i = 0; i < newprobs.size(); i++) {
                int value = newprobs.get(i);
//				String[] split2 = string.split(Symbol.XIAHUAXIAN_REG);
//				if(Integer.parseInt(split2[1])>random){
                if (value > randomValue) {
                    return i;
                }
            }
        } catch (Exception e) {
            logger.error("计算机率错误" + probs.toString(), e);
        }
        return -1;
    }

    /**
     * 根据权重随机
     * @param c
     * @param func
     * @param <T>
     * @return
     */
    public static <T> T random(Collection<T> c, Function<T, Integer> func) {
        TreeMap<Float, T> weightMap = new TreeMap<>();
        for (T param : c) {
            Float weight = weightMap.size() == 0 ? 0f : weightMap.lastKey();
            weight += func.apply(param);
            weightMap.put(weight, param);
        }
        float randomWeight = RandomUtils.randomFloatValue(0f, weightMap.lastKey());
        SortedMap<Float, T> sort = weightMap.tailMap(randomWeight, false);
        return weightMap.get(sort.firstKey());
    }
    public static <T> T random(T[] c, Function<T, Integer> func) {
        TreeMap<Float, T> weightMap = new TreeMap<>();
        for (T param : c) {
            Float weight = weightMap.size() == 0 ? 0f : weightMap.lastKey();
            weight += func.apply(param);
            weightMap.put(weight, param);
        }
        float randomWeight = RandomUtils.randomFloatValue(0f, weightMap.lastKey());
        SortedMap<Float, T> sort = weightMap.tailMap(randomWeight, false);
        return weightMap.get(sort.firstKey());
    }

    /**
     * m 个 整数中随机抽取 n 个数
     * 要求：m > n > 0；
     *
     * @return
     */
    public static List<Integer> mChooseN(List<Integer> source, int n) {
        List<Integer> result = new ArrayList<>(8);
        int length = source.size();
        Integer[] sourceArray = new Integer[length];
        source.toArray(sourceArray);
        if (length == 0 || n <= 0) {
            return result;
        }
        //先洗牌
        for (int i = 0; i < length; i++) {
            int random = ThreadLocalRandom.current().nextInt(length);
            int temp = sourceArray[i];
            sourceArray[i] = sourceArray[random];
            sourceArray[random] = temp;
        }
        //摸前n张
        for (int i = 0; i < n; i++) {
            result.add(sourceArray[i]);
        }
        return result;
    }

}
