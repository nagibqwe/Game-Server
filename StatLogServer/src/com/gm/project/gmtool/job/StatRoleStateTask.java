package com.gm.project.gmtool.job;
import com.gm.common.dbclient.DBClient;
import com.gm.common.utils.DateUtils;
import com.gm.project.gmtool.config.StatConfigService;
import com.gm.project.gmtool.service.StatService;
import java.util.Date;
import java.util.List;
import java.util.Map;
/**
 * 获取角色 信息表
 */
public class StatRoleStateTask extends BaseTask {
    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(StatService.class);

    public StatRoleStateTask() {
        this.tableName = "rolestate";
        this.interval = Integer.parseInt(StatConfigService.getInstance().getValue("stat.roleState.log.corn"))* 1000;
    }

    /**
     * 拉取服务器日志
     *
     * @param tDblogBeanMap
     */
    public void pullServerLog(Map<String, Object> tDblogBeanMap, DBClient targetDBClient, DBClient dbClientSTAT_LOG) {
        try {
            String serverId = tDblogBeanMap.get("serverId").toString();
            //得到所有日志表
            long src_time = this.getLastInsertTime(dbClientSTAT_LOG, serverId, this.tableName);
            int m = 0;
            long last_insert_Time = 0;
            while (true) {
                String src_time_Time = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, new Date(src_time));
                String sqlBuilder = "select userId,roleId,roleName,createsid,createTime,platUserId,clientOS,machineCode,level,sex,career,rechargeGold,gold,onlineTime,lastLoginTime from rolestate where createTime >= '" + src_time_Time + "' order by createTime asc limit " + m * PAGE_SIZE + "," + PAGE_SIZE + " ";
                List<Map<String, Object>> logList = targetDBClient.selectList(sqlBuilder);
                if (logList != null && logList.size() > 0) {
                    StringBuilder insert_sql = new StringBuilder("insert ignore into stat_role(userId,roleId,roleName,createsid,machineCode,level,sex,career,rechargeGold,gold,onlineTime,lastLoginTime,createTime) values");
                    for (int j = 0; j < logList.size(); j++) {
                        Map<String, Object> logBean = logList.get(j);
                        insert_sql.append("(");
                        insert_sql.append("'").append(logBean.get("userId")).append("',");
                        insert_sql.append("'").append(logBean.get("roleId")).append("',");
                        insert_sql.append("'").append(logBean.get("roleName")).append("',");
                        insert_sql.append("'").append(logBean.get("createsid")).append("',");
                        insert_sql.append("'").append(logBean.get("machineCode")).append("',");

                        insert_sql.append("'").append(logBean.get("level")).append("',");
                        insert_sql.append("'").append(logBean.get("sex")).append("',");
                        insert_sql.append("'").append(logBean.get("career")).append("',");
                        insert_sql.append("'").append(logBean.get("rechargeGold")).append("',");
                        insert_sql.append("'").append(logBean.get("gold")).append("',");
                        insert_sql.append("'").append(logBean.get("onlineTime")).append("',");
                        insert_sql.append("'").append(logBean.get("lastLoginTime")).append("',");


                        insert_sql.append("'").append(logBean.get("createTime")).append("'");
                        //insert_sql.append("'").append(this.toDateTime(logBean.get("createTime"))).append("'");
                        if (j == logList.size() - 1) {
                            insert_sql.append(")");
                        } else {
                            insert_sql.append("),");
                        }
                        last_insert_Time = DateUtils.parseDate(logBean.get("createTime").toString()).getTime();
                    }
                    dbClientSTAT_LOG.executeUpdate(insert_sql.toString());
                } else {
                    break;
                }
                m += 1;
            }
            if (src_time == 0) {
                StringBuilder insert_stat_last_insert_sql = new StringBuilder("insert  into stat_last_insert(sid,src_table,src_time,src_Id) values(?,?,?,?)");
                dbClientSTAT_LOG.executeUpdate(insert_stat_last_insert_sql.toString(), serverId, this.tableName, last_insert_Time, 0);
            } else {
                String last_sql = "update stat_last_insert set src_time= " + last_insert_Time + " ,src_Id= " + 0 + " where sid=" + serverId + " and src_table='" + this.tableName + "'";
                dbClientSTAT_LOG.executeUpdate(last_sql);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.toString());
        }
    }
}
