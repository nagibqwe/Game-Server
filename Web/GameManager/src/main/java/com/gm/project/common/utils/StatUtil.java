package com.gm.project.common.utils;

import com.game.util.TwoTuple;
import com.gm.common.utils.DateUtils;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class StatUtil {
    public static DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    public static DateFormat sdfhm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static long oneday = 1000 * 60 * 60 * 24;


    /**
     * 计算起始天数的差值
     */
    private static int dValue(Date start, Date end) {
        long diff = end.getTime() - start.getTime();
        return (int) (diff / (1000 * 60 * 60 * 24));
    }
    /**
     * 查询选择日期内的每日登陆用户总数
     * @param startDate
     * @param endDate
     * @return
     */
//    public static List<Date> getDateList(String startDate, String endDate){
//        //查询选择日期内的每日登陆用户总数
//        Date start = DateUtils.parseDate(startDate);
//        Date end = DateUtils.parseDate(endDate);
//        List<Date> dateList = new ArrayList<>();
//        int dvalue = DateUtils.differentDaysByMillisecond(start, end);
//        long dateTime = start.getTime();
//        Date date;
//        for (int i = 0; i < dvalue + 1; i++) {
//            date = new Date(dateTime - oneday);
//            dateList.add(date);
//            dateTime += oneday;
//        }
//        return dateList;
//    }
    public static List<Date> getDateList(String startDate, String endDate) {
        List<Date> dateList = new ArrayList<>();
        Date start = null;
        try {
            start = sdf.parse(startDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date end = null;
        try {
            end = sdf.parse(endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int dvalue = dValue(start, end);
        long oneday = 1000 * 60 * 60 * 24;
        long dateTime = start.getTime();
        Date date;
        for (int i = 0; i < dvalue + 1; i++) {
            date = new Date(dateTime);
            dateList.add(date);
            dateTime += oneday;
        }
        return dateList;
    }

    public static double div(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }



    public static TwoTuple<Long,Long> getDataTime(String startDate, String endDate){
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        try {
            start.setTime(StatUtil.sdf.parse(startDate));
            end.setTime(StatUtil.sdf.parse(endDate));
            end.add(Calendar.DAY_OF_MONTH, 1);
            start.setTimeInMillis(start.getTimeInMillis());
            end.setTimeInMillis(end.getTimeInMillis() - 1000L);
        } catch (ParseException e) {
            // log.error(e.getMessage(), e);
        }
        long  stime = start.getTimeInMillis() / 1000;
        long etime = end.getTimeInMillis() / 1000;
        return new TwoTuple<>(stime,etime);
    }

    /**
     * 判断参数是否为true
     * @param param
     * @param key
     * @return
     */
    public static boolean isParam(Map<String, Object> param,String key){
        if(param.containsKey(key)){
            if(param.get(key) == null){
                return false;
            }
           return (boolean)param.get(key);

        }
        return false;
    }

}
