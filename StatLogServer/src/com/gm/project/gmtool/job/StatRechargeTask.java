package com.gm.project.gmtool.job;

import com.gm.common.dbclient.DBClient;
import com.gm.common.utils.DateUtils;
import com.gm.project.gmtool.config.StatConfigService;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 每5分钟抓一次充值日志
 */
public class StatRechargeTask extends BaseTask {
    private static Logger logger = Logger.getLogger(StatRechargeTask.class);

    public StatRechargeTask() {
        this.tableName = "rechargelog";
        this.interval = Integer.parseInt(StatConfigService.getInstance().getValue("stat.recharge.log.corn"))* 1000;
    }

    /**
     * 拉取服务器充值日志
     *
     * @param tDblogBeanMap    日志库信息
     * @param targetDBClient   目标拉取日志库组件
     * @param dbClientSTAT_LOG gm日志库
     */
    public void pullServerLog(Map<String, Object> tDblogBeanMap, DBClient targetDBClient, DBClient dbClientSTAT_LOG) {
        try {
            //服务器id
            String serverId = tDblogBeanMap.get("serverId").toString();

            //得到所有日志表
            long src_time = this.getLastInsertTime(dbClientSTAT_LOG, serverId, this.tableName);
            // 数据拉取到某一页
            int m = 0;
            long last_insert_Time = 0;
            int last_insert_id = 0;
            while (true) {
                String src_time_Time = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, new Date(src_time));
                String pullSql = "select id,roleId,userId,sid,orderNo,gameMoney,status,statusReason,itemId,totalFee,addTime,time,platformName,src,goodsId from " + this.tableName + " where addTime >= '" + src_time_Time + "' order by addTime asc limit " + m * PAGE_SIZE + "," + PAGE_SIZE + " ";
                List<Map<String, Object>> logList = targetDBClient.selectList(pullSql.toString());
                //拉取数据列表
                if (logList != null && logList.size() > 0) {
                    //插入汇总库
                    StringBuilder insert_sql = new StringBuilder("insert ignore into stat_recharge(srcId,roleId,userId,sid,orderNo,gameMoney,status,statusReason,itemId,totalFee,platformName,src,goodsId,timesec,addTime,time) values");
                    for (int j = 0; j < logList.size(); j++) {
                        Map<String, Object> logBean = logList.get(j);
                        insert_sql.append("(");
                        insert_sql.append("'").append(Integer.parseInt(logBean.get("id").toString())).append("',");
                        insert_sql.append("'").append(logBean.get("roleId")).append("',");
                        insert_sql.append("'").append(logBean.get("userId")).append("',");
                        insert_sql.append("'").append(logBean.get("sid")).append("',");
                        insert_sql.append("'").append(logBean.get("orderNo")).append("',");
                        insert_sql.append("'").append(logBean.get("gameMoney")).append("',");
                        insert_sql.append("'").append(logBean.get("status")).append("',");
                        insert_sql.append("'").append(logBean.get("statusReason")).append("',");
                        insert_sql.append("'").append(logBean.get("itemId")).append("',");
                        insert_sql.append("'").append(logBean.get("totalFee")).append("',");
                        insert_sql.append("'").append(logBean.get("platformName")).append("',");
                        insert_sql.append("'").append(logBean.get("src")).append("',");
                        insert_sql.append("'").append(logBean.get("goodsId")).append("',");

                        insert_sql.append("'").append(logBean.get("time")).append("',"); //时间毫秒
                        insert_sql.append("'").append(this.milliscondToDateTime(logBean.get("addTime"))).append("',");
                        insert_sql.append("'").append(this.secondToDateTime(logBean.get("time"))).append("'");
                        if (j == logList.size() - 1) {
                            insert_sql.append(")");
                        } else {
                            insert_sql.append("),");
                        }
                        //记录最后一条日志的时间
                        last_insert_Time = Long.parseLong(logBean.get("addTime").toString());
                        //记录最后一条日志的id
                        last_insert_id = Integer.parseInt(logBean.get("id").toString());
                    }
                    //拉取日志 执行sql到 统计平台日志库
                    dbClientSTAT_LOG.executeUpdate(insert_sql.toString());
                } else {
                    break;
                }
                m += 1;
            }
            if (last_insert_id != 0) {
                if (src_time == 0) {
                    StringBuilder insert_stat_last_insert_sql = new StringBuilder("insert  into stat_last_insert(sid,src_table,src_time,src_Id) values(?,?,?,?)");
                    dbClientSTAT_LOG.executeUpdate(insert_stat_last_insert_sql.toString(), serverId, this.tableName, last_insert_Time, last_insert_id);
                } else {
                    String last_sql = "update stat_last_insert set src_time= " + last_insert_Time + " ,src_Id= " + last_insert_id + " where sid=" + serverId + " and src_table='" + tableName + "'";
                    dbClientSTAT_LOG.executeUpdate(last_sql);
                }
            }
        } catch (Exception e) {
            logger.error(e.toString());
        }
    }
}
