package com.kits.common.utils;

import java.lang.management.ManagementFactory;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang3.time.DateFormatUtils;

/**
 * 时间工具类
 * 
 * @author ruoyi
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils
{
    public static String YYYY = "yyyy";

    public static String YYYY_MM = "yyyy-MM";

    public static String YYYY_MM_DD = "yyyy-MM-dd";

    public static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    private static String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM", 
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

    /**
     * 获取当前Date型日期
     * 
     * @return Date() 当前日期
     */
    public static Date getNowDate()
    {
        return new Date();
    }

    /**
     * 获取当前日期, 默认格式为yyyy-MM-dd
     * 
     * @return String
     */
    public static String getDate()
    {
        return dateTimeNow(YYYY_MM_DD);
    }

    public static final String getTime()
    {
        return dateTimeNow(YYYY_MM_DD_HH_MM_SS);
    }

    public static final String dateTimeNow()
    {
        return dateTimeNow(YYYYMMDDHHMMSS);
    }

    public static final String dateTimeNow(final String format)
    {
        return parseDateToStr(format, new Date());
    }

    public static final String dateTime(final Date date)
    {
        return parseDateToStr(YYYY_MM_DD, date);
    }

    public static final String parseDateToStr(final String format, final Date date)
    {
        return new SimpleDateFormat(format).format(date);
    }

    public static final Date dateTime(final String format, final String ts)
    {
        try
        {
            return new SimpleDateFormat(format).parse(ts);
        }
        catch (ParseException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * 日期路径 即年/月/日 如2018/08/08
     */
    public static final String datePath()
    {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyy/MM/dd");
    }

    /**
     * 日期路径 即年/月/日 如20180808
     */
    public static final String dateTime()
    {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyyMMdd");
    }

    /**
     * 日期型字符串转化为日期 格式
     */
    public static Date parseDate(Object str)
    {
        if (str == null)
        {
            return null;
        }
        try
        {
            return parseDate(str.toString(), parsePatterns);
        }
        catch (ParseException e)
        {
            return null;
        }
    }

    /**
     * 获取服务器启动时间
     */
    public static Date getServerStartDate()
    {
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        return new Date(time);
    }

    /**
     * 计算相差天数
     */
    public static int differentDaysByMillisecond(Date date1, Date date2)
    {
        return Math.abs((int) ((date2.getTime() - date1.getTime()) / (1000 * 3600 * 24)));
    }

    public static int differentDaysByMillisecond(String date1Str, String date2Str)
    {
        Date date1 = parseDate(date1Str);
        Date date2 = parseDate(date2Str);
        return Math.abs((int) ((date2.getTime() - date1.getTime()) / (1000 * 3600 * 24)));
    }

    /**
     * 计算两个时间差
     */
    public static String getDatePoor(Date endDate, Date nowDate)
    {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        return day + "天" + hour + "小时" + min + "分钟";
    }

    //-----------------------时间扩展----------------------------------------------//
    public static long MONTH_MILLIS = 30 * 24 * 60 * 60 * 1000L;
    public static long WEEK_MILLIS = 7 * 24 * 60 * 60 * 1000L;
    public static long DAY_MILLIS = 24 * 60 * 60 * 1000L;
    public static long HOUR_MILLIS = 60 * 60 * 1000L;
    public static long MIN_MILLIS = 60 * 1000L;

    /** 取得当前小时数 */
    public static long getCurrentHour() {
        Calendar now = Calendar.getInstance();
        return now.get(Calendar.HOUR_OF_DAY);
    }

    /** 取得当前分钟数 */
    public static long getCurrentMinute() {
        Calendar now = Calendar.getInstance();
        return now.get(Calendar.MINUTE);
    }

    /** 取得第二天的0点的毫秒数 */
    public static long getTomorrowZeroMillis() {
        return getTodayZeroMillis() + DAY_MILLIS;
    }

    /** 取得当天的0点的毫秒数 */
    public static long getTodayZeroMillis() {
        Calendar now = Calendar.getInstance();
        now.set(Calendar.HOUR_OF_DAY, 0);
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.SECOND, 0);
        now.set(Calendar.MILLISECOND, 0);
        return now.getTime().getTime();
    }

    public static String dateToString(Date date) {
        String time = "";
        try {
            if (date != null) {
                time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
            }
        } catch (Exception e) {
            System.out.println("dateToString is exception");
        }
        return time;
    }

    public static String dateToString2(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }
    public static String dateToString3(Date date) {
        return new SimpleDateFormat("MM-dd").format(date);
    }

    public static Date shortStringToDate(String s) {
		try {
			return new SimpleDateFormat("yyyy-MM-dd").parse(s);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
    public static Date stringToDate(String s) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(s);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
    public static String sqlDateToString(java.sql.Date date) {
        java.sql.Timestamp ts = new java.sql.Timestamp(date.getTime());
        return ts.toString();
    }
    public static String getNewDateForDay(int day) {
        Calendar cal = new GregorianCalendar();
        cal.add(Calendar.DAY_OF_YEAR, day);
        return dateToString(cal.getTime());
    }
    public static String getNewDateForMinute(int minute) {
        Calendar cal = new GregorianCalendar();
        cal.add(Calendar.MINUTE, minute);
        return dateToString(cal.getTime());
    }
    public static String getNewDateForMinute(String oldDateStr, int minute) {
        Calendar cal = new GregorianCalendar();
        Date oldDate = stringToDate(oldDateStr);
        cal.setTime(oldDate);
        cal.add(Calendar.MINUTE, minute);
        return dateToString(cal.getTime());
    }
    public static String getNewDateForMinute2(String oldDateStr, int minute) {
        Calendar cal = new GregorianCalendar();
        Date oldDate = stringToDate(oldDateStr);
        cal.setTime(oldDate);
        cal.add(Calendar.MINUTE, minute);
        return dateToString2(cal.getTime());
    }

    public static String endTime(String end) {
		StringBuilder str = new StringBuilder(end);
		str.append(" 23:59:59");
		return str.toString();
	}
    public static Date getNewDateForMinute_Date(int minute) {
		Calendar cal = new GregorianCalendar();
		cal.add(Calendar.MINUTE, minute);
		return cal.getTime();
	}
    /**
     * 获取时间段内的天数
     * @param startTime
     * @param endTime
     * @return
     */
    public static List<String> getDateList(String startTime, String endTime) {
        // 返回的日期集合
        List<String> days = new ArrayList<String>();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date start = dateFormat.parse(startTime);
            Date end = dateFormat.parse(endTime);
            Calendar tempStart = Calendar.getInstance();
            tempStart.setTime(start);
            Calendar tempEnd = Calendar.getInstance();
            tempEnd.setTime(end);
            tempEnd.add(Calendar.DATE, +1);// 日期加1(包含结束)
            while (tempStart.before(tempEnd)) {
                days.add(dateFormat.format(tempStart.getTime()));
                tempStart.add(Calendar.DAY_OF_YEAR, 1);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return days;
    }
}
