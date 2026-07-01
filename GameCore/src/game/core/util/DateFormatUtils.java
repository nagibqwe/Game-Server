/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.core.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 时间日期格式转换处理
 *
 * @author xuchangming <xysoko@qq.com>
 */
public class DateFormatUtils
{
    /**
     * 锁对象
     */
    private static final Object lockObj = new Object();

    /**
     * 存放不同的日期模板格式的sdf的Map
     */
    private static final Map<String, ThreadLocal<SimpleDateFormat>> sdfMap = new HashMap<>();

    /**
     * 返回一个ThreadLocal的sdf,每个线程只会new一次sdf
     *
     * @param pattern
     * @return
     */
    private static SimpleDateFormat getSdf(final String pattern)
    {
        ThreadLocal<SimpleDateFormat> tl = sdfMap.get(pattern);
        synchronized (lockObj)
        {
            tl = sdfMap.get(pattern);
            if (tl == null)
            {
                // 只有Map中还没有这个pattern的sdf才会生成新的sdf并放入map
//                    System.out.println("put new sdf of pattern " + pattern + " to map");
                // 这里是关键,使用ThreadLocal<SimpleDateFormat>替代原来直接new SimpleDateFormat
                tl = new ThreadLocal<SimpleDateFormat>()
                {
                    @Override
                    protected SimpleDateFormat initialValue()
                    {
                        System.out.println("thread: " + Thread.currentThread() + " init pattern: " + pattern);
                        return new SimpleDateFormat(pattern);
                    }
                };
                sdfMap.put(pattern, tl);
            }
        }

        return tl.get();
    }

    /**
     * 是用ThreadLocal<SimpleDateFormat>来获取SimpleDateFormat,这样每个线程只会有一个SimpleDateFormat
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String format(Date date, String pattern)
    {
        return getSdf(pattern).format(date);
    }

    /**
     * 将字符串格式日期转换成日期类
     * @param dateStr 要转换的字符串
     * @param pattern 日期的格式 如yyyy-MM-dd HH:mm:ss
     * @return 返回日期类
     * @throws ParseException 
     */
    public static Date parse(String dateStr, String pattern) throws ParseException
    {
        return getSdf(pattern).parse(dateStr);
    }

}
