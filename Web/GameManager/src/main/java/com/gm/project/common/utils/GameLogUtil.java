package com.gm.project.common.utils;

import com.gm.common.dbclient.DBClient;
import com.gm.common.dbclient.DBServerMgr;
import com.gm.common.dbclient.TableType;
import com.gm.common.utils.DateUtils;
import com.gm.common.utils.StringUtils;
import com.gm.project.gmtool.server.domain.TServer;

import java.util.*;

public class GameLogUtil {


    public static Map<String,Object> getParamMap(String startDate,String endDate,Integer serverId,Integer pageSize){
        Map<String,Object> param = new HashMap<>();
        param.put("startDate",startDate);
        param.put("endDate",endDate);
        param.put("serverId",serverId);
        param.put("pageSize",pageSize);
        return param;
    }




    public static <T> List<T> getChatLogDataList(Class<T> clazz,Map<String, Object> param) {

        StringBuilder where = (StringBuilder)param.get("where") ;
        String selectServerId = "";
        String tableName = "";
        if(param.containsKey("tableNameList"))
        {
            selectServerId = param.get("tableNameList").toString().split("_")[0];
            tableName = param.get("tableNameList").toString().split("_")[1];
        }
        DBClient dbClient = DBServerMgr.getInstance().getLogDBClient(Integer.parseInt(selectServerId));
        if (dbClient == null) {
            return null;
        }
        if(param.containsKey("startDate")){
            if(param.get("startDate")!=null&& !"".equals(param.get("startDate"))){
                //时间通用 判断
                Calendar start = Calendar.getInstance();
                //参数
                String startDate = param.get("startDate").toString();
                start.setTime(DateUtils.parseDate(startDate));
                long startTime = start.getTimeInMillis() / 1000;
                //时间
                where.append(" and time >=").append(startTime);
            }

        }
        if(param.containsKey("endDate")){
            if(param.get("endDate")!=null&& !"".equals(param.get("endDate"))){
                String endDate = param.get("endDate").toString();
                Calendar end = Calendar.getInstance();
                end.setTime(DateUtils.parseDate(endDate));
                long endTime = end.getTimeInMillis() / 1000;
                where.append(" and time<=").append(endTime);
            }

        }

        where.append(" order by time desc");
        int count = dbClient.qryTotalCount("select count(*) from " + tableName + where.toString());
        param.put("count",count);
        //分页数据
        if(param.containsKey("pageNum")){
            int pageNum = (int)param.get("pageNum");
            int pageSize = (int)param.get("pageSize");
            where.append(" limit "+(pageNum-1)*pageSize+","+pageSize+"");
        }

        List<T> dataList = dbClient.selectList("select * from " + tableName + where.toString(), clazz);
        return dataList;
    }


    /**
     * 通用日志查询方法
     *

     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> getLogDataList(Class<T> clazz,Map<String, Object> param) {
        //参数
        String tableName = param.get("tableName").toString();
        String startDate = param.get("startDate").toString();
        String endDate = param.get("endDate").toString();
        StringBuilder where = (StringBuilder)param.get("where") ;
        int selectServerId = (int)param.get("serverId");

        boolean isPager = param.containsKey("pageSize") && !StringUtils.isBlank(param.get("pageSize").toString());

        int tableType = 0;
        if (tableName.equals("rechargelog") || tableName.equals("changerolenamelog")) {
            tableType = TableType.No;
        } else {
            tableType = TableType.Month;
        }
        //时间通用 判断
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        start.setTime(DateUtils.parseDate(startDate));
        end.setTime(DateUtils.parseDate(endDate));
        long startTime = start.getTimeInMillis() / 1000;
        long endTime = end.getTimeInMillis() / 1000;

        //时间
        where.append(" and time >=").append(startTime).append(" and time<=").append(endTime);
        where.append(" order by time");

        List<TServer> tServerList = DBServerMgr.getInstance().checkHeFu(selectServerId, DateUtils.parseDate(startDate), DateUtils.parseDate(endDate));
        List<String> realTables = DBServerMgr.getInstance().getQueryTables(tableName, tableType, startDate, endDate);
        List<T> resultList = new ArrayList<>();
        boolean isFull = false;
        int totalCount = 0;

        for (TServer db : tServerList) {
            List<String> tableList = DBServerMgr.getInstance().queryTables(db, tableName);
            if (tableList != null) {
                realTables.retainAll(tableList);
            }
            if (realTables.isEmpty()) {
                continue;
            }
            Collections.sort(realTables);
            for (String realTableName : realTables) {
                DBClient dbClient = DBServerMgr.getInstance().getLogDBClient(db);
                if (dbClient == null) {
                    continue;
                }
                if (!isFull) {
                    List<T> dataList = dbClient.selectList("select * from " + realTableName + where.toString(), clazz);
                    if (dataList != null) {
                        resultList.addAll(dataList);
                    }
                }
                int count = dbClient.qryTotalCount("select count(*) from " + realTableName + where.toString()); //QueryUtil.getInstance().queryCount(db, countSql, realTable);
                totalCount += count;

                //如果有日志条数限制，判断是否达到指定条数
                if (isPager && totalCount > Integer.parseInt(param.get("pageSize").toString())) {
                    isFull = true;
                    if (resultList.size() > Integer.parseInt(param.get("pageSize").toString())) {
                        resultList.subList(Integer.parseInt(param.get("pageSize").toString()), resultList.size()).clear();
                    }
                }

            }
        }
        return resultList;
    }

    /**
     * 查询最终合服后的游戏服日志
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> getHeFuLogDataList(Class<T> clazz,Map<String, Object> param) {
        String tableName = param.get("tableName").toString();

        StringBuilder where = (StringBuilder)param.get("where") ;
        int selectServerId = (int)param.get("serverId");

        //开始时间 条件限制
        if (param.containsKey("startDate")) {
            //时间通用 判断
            String startDate = param.get("startDate").toString();
            String endDate = param.get("endDate").toString();

            Calendar start = Calendar.getInstance();
            Calendar end = Calendar.getInstance();
            start.setTime(DateUtils.parseDate(startDate));
            end.setTime(DateUtils.parseDate(endDate));
            long startTime = start.getTimeInMillis() / 1000;
            long endTime = end.getTimeInMillis() / 1000;
            //时间
            where.append(" and time >=").append(startTime).append(" and time<=").append(endTime);
            where.append(" order by time");
        }
        TServer finalHeFuServer = DBServerMgr.getInstance().getFinalHeFuServer(selectServerId);
        DBClient dbClient = DBServerMgr.getInstance().getLogDBClient(finalHeFuServer);
        if (dbClient == null) {
            return null;
        }
        int count = dbClient.qryTotalCount("select count(*) from " + tableName + where.toString());
        param.put("count",count);

        //分页数据
        if(param.containsKey("pageNum")){
            int pageNum = (int)param.get("pageNum");
            int pageSize = (int)param.get("pageSize");
            where.append(" limit "+(pageNum-1)*pageSize+","+pageSize+"");
        }

        List<T> dataList = dbClient.selectList("select * from " + tableName + where.toString(), clazz);
        return  dataList;
    }
}
