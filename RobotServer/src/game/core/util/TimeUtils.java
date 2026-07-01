/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.core.util;

import game.core.db.DbServerConfig;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 *
 * @author Administrator
 */
public class TimeUtils {

    //设置的时间值
    private static long m_ServerBeginTime = 0;
    private static boolean isTimeGMSet = false;

    //当前时间值与系统时间的差值
    private static long m_BetweenNowAndSetTime = 0;

    public static void setServerBeginTime(long ServerBeginTime) {
        m_BetweenNowAndSetTime = ServerBeginTime - System.currentTimeMillis();
        m_ServerBeginTime = ServerBeginTime;
    }

    public static void setTime(long setTime) {
        m_BetweenNowAndSetTime = setTime - System.currentTimeMillis();
        isTimeGMSet = true;
    }

    //获得游戏的当前时间值
    public static boolean isTimeGMSet() {
        return isTimeGMSet;
    }

    //获得游戏的当前时间值
    public static long Time() {
        return System.currentTimeMillis() + m_BetweenNowAndSetTime;
    }

    //获取当天0时0分0秒 到现在流逝的时间
    public static long getTodayPassMillis() {
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(Time());
        int hour = instance.get(Calendar.HOUR_OF_DAY);
        int min = instance.get(Calendar.MINUTE);
        int second = instance.get(Calendar.SECOND);
        int millis = instance.get(Calendar.MILLISECOND);

        return hour * 3600000 + min * 60000 + second * 1000 + millis;
    }

    //获取今天开始时间
    public static long getTodayBeginTime() {
        long curTime = Time();
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(curTime);
        instance.set(Calendar.MILLISECOND, 0);
        int year = instance.get(Calendar.YEAR);
        int month = instance.get(Calendar.MONTH);
        int date = instance.get(Calendar.DATE);
        instance.set(year, month, date, 0, 0, 0);
        return instance.getTimeInMillis();
    }

    /**
     * 取得当前日期所在周的第一天
     *
     * @param date
     * @return
     */
    public static long getFirstDayOfWeek() {
        long curTime = Time();
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(curTime);
        instance.setFirstDayOfWeek(Calendar.MONDAY);
        instance.set(Calendar.DAY_OF_WEEK, instance.getFirstDayOfWeek());
        return instance.getTimeInMillis();
    }

    //获取本月开始时间
    public static long getMonthBeginTime() {
        long curTime = Time();
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(curTime);
        instance.set(Calendar.MILLISECOND, 0);
        int year = instance.get(Calendar.YEAR);
        int month = instance.get(Calendar.MONTH);
        instance.set(year, month, 1, 0, 0, 0);
        return instance.getTimeInMillis();
    }

    //获取下个月开始时间
    public static long getNextMonthBeginTime() {
        long curTime = Time();
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(curTime);
        instance.set(Calendar.MILLISECOND, 0);
        int year = instance.get(Calendar.YEAR);
        int month = instance.get(Calendar.MONTH);
        if (month >= 12) {
            year += 1;
            month = 1;
        } else {
            month += 1;
        }
        instance.set(year, month, 1, 0, 0, 0);
        return instance.getTimeInMillis();
    }

    //获得格式化的时间值
    public static String NowToString() {
        long now = Time();
        Date date = new Date(now);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date);
    }

    /**
     * netbeans/eclipse等IDE下 运行/调试 关闭时IDE内部调用Process.destroy() <br />
     * 无法触发到JVM shutdown hook, 导致关闭时无法回存 <br />
     * 故在IDE环境下添加了system property: ideDebug
     *
     * @return
     */
    public static boolean isIDEEnvironment() {
        String val = System.getProperty("ideDebug");
        return val != null && val.equals("true");
    }

    /**
     * 判断两个时间是否在同一天
     *
     * @param time
     * @param time2
     * @return
     */
    public static boolean isSameDay(long time, long time2) {
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(time);
        int d1 = instance.get(Calendar.DAY_OF_YEAR);
        int y1 = instance.get(Calendar.YEAR);
        instance.setTimeInMillis(time2);
        int d2 = instance.get(Calendar.DAY_OF_YEAR);
        int y2 = instance.get(Calendar.YEAR);
        return d1 == d2 && y1 == y2;
    }

    /**
     * 获取1970至今的天数 （计数会在在每天指定的小时+1，用来判断每天X点清数据之类的）
     *
     * @param hour 每天第X个小时+1
     * @return
     */
    public static int getCurDay(int hour) {
        TimeZone zone = TimeZone.getDefault();	//默认时区
        long s = Time() / 1000 + hour * 3600;
        if (zone.getRawOffset() != 0) {
            s = s + zone.getRawOffset() / 1000;
        }
        s = s / 86400; //86400 = 24 * 60 * 60 (一天时间的秒数)
        return (int) s;
    }

    /**
     * 指定时间的年份
     *
     * @param time
     * @return
     */
    public static int getYear(long time) {
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(time);
        return instance.get(Calendar.YEAR);
    }

    /**
     * 指定时间的月份,0-11
     *
     * @param time
     * @return
     */
    public static int getMonth(long time) {
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(time);
        return instance.get(Calendar.MONTH);
    }

    /**
     * 获取日期(一个月内的第几天)
     *
     * @param time
     * @return
     */
    public static int getDayOfMonth(long time) {
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(time);
        return instance.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取小时
     *
     * @param time
     * @return
     */
    public static int getDayOfHour(long time) {
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(time);
        return instance.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 获取分钟
     *
     * @param time
     * @return
     */
    public static int getDayOfMin(long time) {
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(time);
        return instance.get(Calendar.MINUTE);
    }

    /**
     * 获取秒
     *
     * @param time
     * @return
     */
    public static int getDayOfSecond(long time) {
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(time);
        return instance.get(Calendar.SECOND);
    }

    /**
     * 获取指定时间 是一月内的第几周
     *
     * @param time
     * @return
     */
    public static int getDayOfWeekInMonth(long time) {
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(time);
        return instance.get(Calendar.DAY_OF_WEEK_IN_MONTH);
    }

    /**
     * 获取星期几
     *
     * @param time
     * @return
     */
    public static int getDayOfWeek(long time) {
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(time);
        int i = instance.get(Calendar.DAY_OF_WEEK);
        if (i == 1) {
            return 7;
        } else {
            i -= 1;
        }
        return i;
    }

    /**
     * 获取一年内的第几天
     *
     * @param time
     * @return
     */
    public static int getDayOfYear(long time) {
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(time);
        return instance.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * 字符串转日期("yyyy-MM-dd HH:mm:ss");
     *
     * @param date
     * @return
     * @throws java.text.ParseException
     */
    public static Date getDateByString(String date) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.parse(date);
    }

    /**
     * 判断两个时间中间所差天数
     *
     * @param time1
     * @param time2
     * @return
     */
    public static int getBetweenDays(long time1, long time2) {
        Calendar instance1 = Calendar.getInstance();
        instance1.setTimeInMillis(time1);
        instance1.set(Calendar.HOUR_OF_DAY, 0);
        instance1.set(Calendar.MINUTE, 0);
        instance1.set(Calendar.SECOND, 0);
        instance1.set(Calendar.MILLISECOND, 0);
        Calendar instance2 = Calendar.getInstance();
        instance2.setTimeInMillis(time2);
        instance2.set(Calendar.HOUR_OF_DAY, 0);
        instance2.set(Calendar.MINUTE, 0);
        instance2.set(Calendar.SECOND, 0);
        instance2.set(Calendar.MILLISECOND, 0);
        return (int) ((instance1.getTimeInMillis() - instance2.getTimeInMillis()) / (24 * 60 * 60 * 1000));
    }

    public static int getOpenAreaDay() {
        try {
            Date open = getDateByString(DbServerConfig.getServerOpenTime());
            long zday = GetCurTimeInMin(4, open.getTime());
            long sday = GetCurTimeInMin(4, System.currentTimeMillis());
            int day = (int) (sday - zday) + 1;
            return day;
        } catch (ParseException ex) {
        }
        return 0;
    }

    /**
     * 获取1970至今的时间, 1获取秒，2 分钟，3小时，4天数,5周数
     *
     * @param x
     * @param time
     * @return
     */
    public static long GetCurTimeInMin(int x, long time) {
        TimeZone zone = TimeZone.getDefault();	//默认时区
        long s = time / 1000;
        if (zone.getRawOffset() != 0) {
            s = s + zone.getRawOffset() / 1000;
        }
        switch (x) {
            case 1:
                break;
            case 2:
                s = s / 60;
                break;
            case 3:
                s = s / 3600;
                break;
            case 4:
                s = s / 86400;
                break;
            case 5:
                s = s / 86400 + 3;// 补足天数，星期1到7算一周
                s = s / 7;
                break;
            default:
                break;
        }
        return s;
    }
}
