package com.backend.utils;


import com.backend.bean.Dblog;
import com.backend.annotation.log.FieldDesc;
import com.backend.annotation.log.Table;
import com.backend.struct.log.IConvertor;
import com.backend.struct.log.LogFieldEntity;
import com.backend.struct.log.LogRecord;
import com.backend.manager.ReasonManager;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.nutz.dao.pager.Pager;
import org.nutz.lang.Strings;
import org.nutz.mvc.Mvcs;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class LogUtil {

    public static DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    public static DateFormat sdfhms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static Logger log = LogManager.getLogger(LogUtil.class);

    public static LogFieldEntity getLogEntity(Class clazz) {
        Map<String, String> msg = Mvcs.getMessages(Mvcs.getReq());
        LogFieldEntity entity = new LogFieldEntity();
        Table table = (Table) clazz.getAnnotation(Table.class);
        entity.setCrossLogType(table.crossLogType());
        entity.setTable(table.name());
        entity.setTableType(table.tableType());
        entity.setClassname(clazz.getName());
        Field[] fields = clazz.getDeclaredFields();
        Field[] fields1 = clazz.getSuperclass().getDeclaredFields();
        List<Field> fieldList = new ArrayList<>(Arrays.asList(fields));
        fieldList.addAll(Arrays.asList(fields1));
        for (Field field : fieldList) {
            FieldDesc fieldDesc = field.getAnnotation(FieldDesc.class);
            if (fieldDesc.selectKey()) {
                entity.getConditions().add(field.getName());
            }
            if (fieldDesc.show()) {
                String fieldKey = !Strings.isBlank(fieldDesc.desc()) ?
                        fieldDesc.desc() : "logentity." + clazz.getSimpleName().toLowerCase() + "." + field.getName();
                entity.getFields().put(field.getName(), msg.getOrDefault(fieldKey, field.getName()));
            }
        }
        return entity;
    }

    public static LogRecord getLogEntityData(Class clazz, Map<String, Object> param) {
        LogRecord record = new LogRecord();
        boolean isPager = param.containsKey("pageSize") && !Strings.isBlank(param.get("pageSize").toString());
        try {
            if (!param.containsKey("serverId")) {
                log.error("服务器id参数错误");
            }
            Object startDate = param.getOrDefault("startDate", "1970-01-01 00:00:00");
            Object endDate = param.getOrDefault("endDate", sdfhms.format(new Date()));
            Calendar start = Calendar.getInstance();
            Calendar end = Calendar.getInstance();
            start.setTime(sdfhms.parse(startDate.toString()));
            end.setTime(sdfhms.parse(endDate.toString()));
            long startTime = start.getTimeInMillis() / 1000;
            long endTime = end.getTimeInMillis() / 1000;

            //构建查询sql和总记录数sql
            LogFieldEntity entity = getLogEntity(clazz);
            String fieldSql = buildSelectSql(entity, startTime, endTime, param);
            String countSql = buildCountSql(entity, startTime, endTime, param);

            //获取日志库信息和需要查询的表
            int serverId = Integer.parseInt(param.get("serverId").toString());
            List<Dblog> dbList = QueryUtil.getInstance().checkHeFu(serverId, start.getTime(), end.getTime());
            List<String> queryTables = QueryUtil.getInstance().getQueryTables(entity.getTable(), entity.getTableType(), start, end);

            boolean isFull = false;
            int totalCount = 0;
            List<Map<String, String>> resultList = new ArrayList<>();
            for (Dblog db : dbList) {

                //获取真实要查询的表
                List<String> realTables = QueryUtil.getInstance().queryTables(db, entity.getTable());
                realTables.retainAll(queryTables);
                if (realTables.isEmpty()) {
                    continue;
                }
                Collections.sort(realTables);

                for (String realTable : realTables) {
                    if (!isFull) {
                        List<Map<String, String>> tempList = QueryUtil.getInstance().query(db, fieldSql, realTable, param);
                        if (!tempList.isEmpty()) {
                            resultList.addAll(tempList);
                        }
                    }
                    int count = QueryUtil.getInstance().queryCount(db, countSql, realTable);
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

            for (Map<String, String> map : resultList) {
                map.put("time", formatData(map.get("time")));
            }

            //查询结果特殊处理
            if (IConvertor.class.isAssignableFrom(clazz)) {
                IConvertor convertor = (IConvertor) clazz.newInstance();
                List<Map<String, String>> list = new ArrayList<>();
                for (Map<String, String> map : resultList) {
                    if (map.containsKey("reason")) {
                        Integer reason = Integer.valueOf(map.get("reason"));
                        map.put("reason", ReasonManager.getInstance().getReasonMap().get(reason) + "[" + reason + "]");
                    }
                    list.add(convertor.convert(map));
                }
                resultList = list;
            }

            record.setFields(entity.getFields());
            record.setDatas(resultList);

            //没有记录条数限制，默认第一页，size为Integer.MAX_VALUE
            Pager pager = new Pager(1, Integer.parseInt(param.getOrDefault("pageSize", Integer.MAX_VALUE).toString()));
            pager.setRecordCount(totalCount);
            record.setPager(pager);
        } catch (Exception e) {
            log.error(e, e);
        }
        return record;
    }

    private static String buildSelectSql(LogFieldEntity entity, long sTime, long eTime, Map<String, Object> conditionMap) {
        Map<String, String> fieldMap = entity.getFields();
        StringBuilder sb = new StringBuilder("select ");

        for (String field : fieldMap.keySet()) {
            sb.append("t.").append(field).append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(" from $table as t");

        return buildWhereCondition(entity, sb.toString(), sTime, eTime, conditionMap);
    }

    private static String buildCountSql(LogFieldEntity entity, long sTime, long eTime, Map<String, Object> conditionMap) {
        return buildWhereCondition(entity, "select count(*) from $table as t", sTime, eTime, conditionMap);
    }

    private static String buildWhereCondition(LogFieldEntity entity, String sql, long sTime, long eTime, Map<String, Object> conditionMap) {
        StringBuilder sb = new StringBuilder(sql);
        if (!sql.contains("where")) {
            sb.append(" where ");
        } else {
            sb.append(" and ");
        }
        sb.append("t.time >=").append(sTime).append(" and t.time<=").append(eTime);
        if (conditionMap.containsKey("channelName")
                && !Strings.isBlank(conditionMap.get("channelName").toString())
                && entity.getFields().containsKey("platformName")) {
            Object channelNames = conditionMap.get("channelName");
            if (channelNames instanceof JSONArray) {
                channelNames = channelNames.toString().substring(1, channelNames.toString().length() - 1);
            } else {
                channelNames = "\"" + channelNames + "\"";
            }
            sb.append(" and t.platformName in (").append(channelNames).append(")");
        }
        for (String key : entity.getConditions()) {
            if (conditionMap.containsKey(key) && !Strings.isBlank(conditionMap.get(key).toString())) {
                sb.append(" and t.").append(key).append("=").append("'").append(conditionMap.get(key)).append("'");
            }
        }
        sb.append(" order by t.time");
        return sb.toString();
    }

    public static Map<String, Object> parseCondition(JSONObject condition) {
        String temp = condition.toString();
        if (temp.contains("[")) {
            temp.replace("[", "\"");
        }
        if (temp.contains("]")) {
            temp.replace("]", "\"");
        }

        condition = JsonUtils.parseObject(temp, JSONObject.class);
        Map<String, Object> paraMap = new LinkedHashMap<>();
        for (Object key : condition.keySet()) {
            paraMap.put(key.toString(), condition.get(key));
        }
        return paraMap;
    }

    /**
     * 时间格式化
     */
    public static String formatData(String time) {
        try {
            long t = Long.parseLong(time);
            if (t < Integer.MAX_VALUE) {
                t *= 1000;
            }
            return sdfhms.format(new Date(t));
        } catch (Exception e) {
            log.error(e ,e);
        }
        return time;
    }

}
