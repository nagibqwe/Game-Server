package com.game.servermatch.structs;

import com.data.Global;
import com.data.struct.ReadArray;
import game.core.util.TimeUtils;

import java.util.*;

/**
 * 系统参数
 * @author zhaibiao
 */
public class LeagueConfig {
    //需要读取的数据
    public static int basis_point = 100;
    public static int add_points = 10;
    public static ReadArray<String> league_apply_time;
    public static ReadArray<String> league_war_time_first;
    public static ReadArray<String> league_war_time_second;
    public static String  league_settlement;
    public static ReadArray<String> city_apply_time;
    public static ReadArray<String> city_war_time;
    public static String league_match_time;
    //城市和副本的对应关系
    public static Map<Integer,Integer> modeId_city = new HashMap<>();
    
    
    
    
//    public static String 
    //时间系列
    public static List<TimeEventBean> timeBeans = new ArrayList<>();
    
    //时间配置表
    public static void load(){
        modeId_city.put(64500, 2);
        modeId_city.put(64501, 3);
        modeId_city.put(64502, 4);
        modeId_city.put(64503, 1);
        basis_point = Global.HolySkyCity_BasePointNum;
        add_points = Global.HolySkyCity_AddPointNum;

        // TODO 这几个时间，应该是一维字符串数组，但是配置目前不对。先直接取0

//        league_apply_time = Global.GuildMatchesSignUpTime.getValuees()[0];
//        league_war_time_first = Global.GuildMatchesFristFightTime.getValuees()[0];
//        league_war_time_second = Global.GuildMatchesSecondFightTime.getValuees()[0];
//        league_settlement = Global.GuildMatchesMonthSettlement;
//        city_apply_time = Global.HolySkyCity_signUpTime.getValuees()[0];
//        city_war_time = Global.HolySkyCity_FightTime.getValuees()[0];
//        league_match_time = Global.GuildMatchesTeamTime;
        
        sysConfig();
    }
    

    
    
    public static void sysConfig() {
        //配置表 策划没有自己写

        String[] str = new String[]{
             "eight_diagrams_trigger,"+String.valueOf(getNature(3)) +",0,1,3,null",//每天一次
              "server_macth_trigger,"+String.valueOf(getNature(3)) +",0,1,3,null",//服务器分组匹配 /每天一次
            //"dayofweekmin_trigger,"+String.valueOf(TimeUtils.getCurWeekBeginTime()) +",0,1,4,null",//每周第一天

            //这两个时间不要
//            "leagueapplybegin_trigger,"+String.valueOf(TimeUtils.getTime(league_apply_time[0])) +","+String.valueOf(getSpacedTime(4))+"0,1,0,weekofcurrentmonthcondtion",//选定守方
//            "leaguewarmatch_trigger,"+String.valueOf(TimeUtils.getTime(league_match_time))+","+String.valueOf(getSpacedTime(4))+"0,1,0,weekofcurrentmonthcondtion",//公会战匹配
//            "leagueApplyEnd_trigger,"+String.valueOf(TimeUtils.getTime(league_apply_time[1])) +",0,1,4,null",



//            "leaguewarbeginfirst_trigger,"+String.valueOf(TimeUtils.getTime(league_war_time_first[0])) +",0,1,4,",
//            "leaguewarendfirst_trigger,"+String.valueOf(TimeUtils.getTime(league_war_time_first[1])) +",0,1,4,",
//
//            "leaguewarbeginsecond_trigger,"+String.valueOf(TimeUtils.getTime(league_war_time_second[0])) +",0,1,4,",
//              "leaguewarendsecond_trigger,"+String.valueOf(TimeUtils.getTime(league_war_time_second[1])) +","+String.valueOf(getSpacedTime(4))+",1,0,weekofcurrentmonthcondtion",//所有战斗结束
//
//               "leaguesettlement_trigger,"+String.valueOf(TimeUtils.getTime(league_settlement)) +",0,1,4,weekofmonthlastcondition",//这个要处理
//            //这两个时间不要
//            "cityapplybegin_trigger,"+String.valueOf(TimeUtils.getTime(city_apply_time[0])) +",0,1,4,",
//            "cityapplyend_trigger,"+String.valueOf(TimeUtils.getTime(city_apply_time[1])) +",0,1,4,",
//
//            "citywarbegin_trigger,"+String.valueOf(TimeUtils.getTime(city_war_time[0])) +",0,1,4,",
//            "citywarend_trigger,"+String.valueOf(TimeUtils.getTime(city_war_time[1])) +",0,1,4,",
        };
        for (String s : str) {
            timeBeans.add(initTimeEventBean(s));
        }
    }

    //获取间隔时间(1 年 2 月 3 日 4 周 5时 6分)
    public static long getSpacedTime(int type){
        switch(type){
            case 1 : return 365*24*60*60*1000L;
            case 2 : return 30*24*60*60*1000L;
            case 3 : return 24*60*60*1000;
            case 4 : return 7*24*60*60*1000;
            case 5 : return 60*60*1000;    
            case 6 : return 60*1000;   
        }
        return 0;
    }
    
    
    public static TimeEventBean initTimeEventBean(String strBean) {
        String[] str = strBean.split(",");
        TimeEventBean bean = new TimeEventBean();
        bean.setTrigger(str[0]);
        bean.setInitial(Long.parseLong(str[1]));
        bean.setSpaced(Long.parseLong(str[2]));
        bean.setRepeat(Integer.parseInt(str[3]) == 1);
        bean.setNature(Integer.parseInt(str[4]));
        bean.setCondition(str[5]);
        return bean;

    }

    //获取当前每月，每日 开始时间
    public static long getNature(int type){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(TimeUtils.Time()));
        switch(type){
            case 1:
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                break;
            case 2:
                calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
                calendar.set(Calendar.HOUR_OF_DAY, 1);
                calendar.set(Calendar.MINUTE, 0);
                break;
            case 3:
                calendar.add(Calendar.DATE, 1);
                calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMinimum(Calendar.HOUR_OF_DAY));
                calendar.set(Calendar.MINUTE, 0);
                break;
            case 4:
                calendar.add(Calendar.WEEK_OF_MONTH, 1);
                calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMinimum(Calendar.HOUR_OF_DAY));
                calendar.set(Calendar.MINUTE, 0);
                break;
            case 5:
                calendar.add(Calendar.HOUR, 1);
                calendar.set(Calendar.MINUTE, 0);
                break;
            case 6:    
                calendar.add(Calendar.MINUTE, 1);
                break;
            case 7: //每天11点59刷新
                calendar.add(Calendar.DATE, 1);
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                break;
        }
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime().getTime();
    }
}
