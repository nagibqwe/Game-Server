package com.game.skill.config;


import game.core.util.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *配置表的读取数据工具
 * @author soko(xysoko@qq.com)
 * @date 2018/9/5
 */
public class SkillReadConfigUtil {
    private static final Logger log = LogManager.getLogger(SkillReadConfigUtil.class);

    /**
     * 获得传入的数组中的某一下标的整型数值
     * @param tmp 数组
     * @param index 下标号
     * @param defineValue 如果找不到的默认值
     * @return 返回查找值
     */
    public static int getIntValue(String[] tmp , int index, int defineValue){
        if( tmp == null){
            log.error("传入的数据为空了， 请查看一下问题！", new NullPointerException());
            return defineValue;
        }

        if( tmp.length <= index){
            log.info("传的数组的大小是小于指定的数组大小的!");
            return defineValue;
        }
        return getIntValue(tmp[index], defineValue);
    }
    /**
     * 转换传送的字符串中的整型数值
     * @param value 下标号
     * @param defineVale 如果找不到的默认值
     * @return 返回查找值
     */
    public static int getIntValue(String value, int defineVale) {
        if (StringUtils.isNumber(value)) {
            return Integer.parseInt(value);
        }
        return defineVale;
    }

    /**
     * 获得传入的数组中的某一下标的单精浮点数值
     * @param tmp 数组
     * @param index 下标号
     * @param defineValue 如果找不到的默认值
     * @return 返回查找值
     */
    public static float getFloatValue(String[] tmp , int index, float defineValue){
        if( tmp == null){
            log.error("传入的数据为空了， 请查看一下问题！", new NullPointerException());
            return defineValue;
        }

        if( tmp.length <= index){
            log.info("传的数组的大小是小于指定的数组大小的!");
            return defineValue;
        }
        return getFloatValue(tmp[index], defineValue);
    }
    /**
     * 转换传送的字符串中的单精浮点数值
     * @param value 下标号
     * @param defineValue 如果找不到的默认值
     * @return 返回查找值
     */
    public static float getFloatValue(String value, float defineValue) {
        try {
            Float ft = Float.parseFloat(value);
            if (ft != null) {
                return ft;
            }
        } catch (NumberFormatException e) {

        }
        return defineValue;
    }

    /**
     * 获得传入的数组中的某一下标的长整型数值
     * @param tmp 数组
     * @param index 下标号
     * @param defineValue 如果找不到的默认值
     * @return 返回查找值
     */
    public static long getLongValue(String[] tmp , int index, long defineValue){
        if( tmp == null){
            log.error("传入的数据为空了， 请查看一下问题！", new NullPointerException());
            return defineValue;
        }

        if( tmp.length <= index){
            log.info("传的数组的大小是小于指定的数组大小的!");
            return defineValue;
        }
        return getLongValue(tmp[index], defineValue);
    }
    /**
     * 转换传送的字符串中的长整型数值
     * @param value 下标号
     * @param defineValue 如果找不到的默认值
     * @return 返回查找值
     */
    public static long getLongValue(String value, long defineValue) {
        if (StringUtils.isNumber(value)) {
            return Long.parseLong(value);
        }
        return defineValue;
    }

    /**
     * 获得传入的数组中的某一下标的字符串，检查并返回指定默认值
     * @param tmp 数组
     * @param index 下标号
     * @param defineValue 如果找不到的默认值
     * @return 返回查找值
     */
    public static String getStringValue(String[] tmp , int index, String defineValue){
        if( tmp == null){
            log.error("传入的数据为空了， 请查看一下问题！", new NullPointerException());
            return defineValue;
        }

        if( tmp.length <= index){
//            log.info("传的数组的大小是小于指定的数组大小的!");
            return defineValue;
        }
        return getStringValue(tmp[index], defineValue);
    }
    /**
     * 转换传送的字符串中的的字符串，检查并返回指定默认值
     * @param value 下标号
     * @param defineValue 如果找不到的默认值
     * @return 返回查找值
     */
    public static String getStringValue(String value, String defineValue) {

        if (StringUtils.isNotEmpty(value)) {
            return value;
        }

        return defineValue;

    }
}
