package com.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
/**
 *
 * @author hewei
 */
public abstract class ConfigBase<Bean> {
    private static final Logger log = LogManager.getLogger("configLog");
    protected volatile int[] keyes;
    protected volatile Bean[] valuees;
    private String fileName;            // load.java文件名
    private long javaFileTimestamp;     // load.java文件时间戳

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getJavaFileTimestamp() {
        return javaFileTimestamp;
    }

    public void setJavaFileTimestamp(long javaFileTimestamp) {
        this.javaFileTimestamp = javaFileTimestamp;
    }

    /**
     *
     * @param mapValuees
     */
    @Deprecated
    public void initValue(Map<Integer, Bean> mapValuees) {
        List<Integer> list = new ArrayList<>(mapValuees.keySet());
        Collections.sort(list);
        int size = list.size();
        int[] tmpKeyes = new int[size];
        Bean[] tmpValuees = newBeanArray(size);
        for (int i = 0; i < size; ++i) {
            tmpKeyes[i] = list.get(i);
            tmpValuees[i] = mapValuees.get(tmpKeyes[i]);
        }
        this.keyes = tmpKeyes;
        this.valuees = tmpValuees;
    }

    /**
     *
     * @param size
     * @return
     */
    protected abstract Bean[] newBeanArray(int size);

    /**
     *
     * @return
     */
    public Map<Integer, Bean> newMapValuees() {
        return new HashMap<>();
    }

    /**
     * 通过key查找
     *
     * @param key
     * @return
     */
    public Bean getValueByKey(int key) {
        return binaryGetValue(key);
    }

    /**
     * 通过下标查找
     *
     * @param index
     * @return
     */
    public Bean getValueByIndex(int index) {
        if (index < 0) {
            return null;
        }
        if (index >= size()) {
            return null;
        }
        return valuees[index];
    }

    private void sendExcptionLog(int key)
    {
        String str = fileName +": " + key;
        Thread dd = Thread.currentThread();
        int size = dd.getStackTrace().length;
        if (size > 2) {
            str += "  ,call by" + Thread.currentThread().getStackTrace()[2];
        }
        log.error("context=" + str);
    }

    /**
     * 数据长度
     *
     * @return
     */
    public int size() {
        return keyes.length;
    }

    /**
     * 二分查找
     *
     * @param key
     * @return
     */
    private Bean binaryGetValue(int key) {
        int index = binarySearch(key);
        if (index < 0) {
            sendExcptionLog(key);
            return null;
        }
        return getValueByIndex(index);
    }

    /**
     * * 二分查找算法
     *
     * @param des 查找元素
     * @return des的数组下标，没找到返回-1
     */
    private int binarySearch(int des) {
        int low = 0;
        int high = keyes.length - 1;
        if (high < 0) {
            return -1;
        }
        if (des < keyes[0]) {
            return -1;
        }
        if (des > keyes[high]) {
            return -1;
        }
        int middle;
        while (low <= high) {
            middle = (low + high) / 2;
            if (des == keyes[middle]) {
                return middle;
            } else if (des < keyes[middle]) {
                high = middle - 1;
            } else {
                low = middle + 1;
            }
        }
        return -1;
    }

    /**
     * 所有数据
     *
     * @return
     */
    public Bean[] getValuees(){return  valuees;}

}
