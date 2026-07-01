package com.gm.project.gmtool.job;

import com.gm.common.dbclient.DBClient;
import com.gm.common.dbclient.DBServerMgr;
import com.gm.common.utils.DateUtils;

import com.gm.project.gmtool.config.StatConfigService;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.List;
import java.util.Map;
 ;

/**
 * 每5分钟抓一次登录日志
 */
public class StatLoginLogTask extends BaseTask {
    private static org.apache.log4j.Logger logger = Logger.getLogger(StatLoginLogTask.class);
    public StatLoginLogTask(){
        this.tableName = "roleloginlog";
        this.interval = Integer.parseInt(StatConfigService.getInstance().getValue("stat.login.log.corn")) * 1000;
    }

    /**
     *  拉取服务器登录日志
     * @param tDblogBeanMap 日志库信息
     * @param targetDBClient 目标拉取日志库组件
     * @param dbClientSTAT_LOG gm日志库
     */
    public void pullServerLog(Map<String, Object> tDblogBeanMap,DBClient targetDBClient,DBClient dbClientSTAT_LOG){
        try {


            //服务器id
            String serverId = tDblogBeanMap.get("serverId").toString();


            //得到所有日志表
            List<String> tableList = DBServerMgr.getInstance().getTableList(targetDBClient,tDblogBeanMap.get("dblogName").toString(),this.tableName);
            if(tableList != null && tableList.size()>0){
                tableList.sort(null);
                for(int i = 0;i<tableList.size();i++){
                    String tableName = tableList.get(i);
                    //拉取日志记录id
                  //  int src_Id = this.getLastInsertId(dbClientSTAT_LOG,serverId,tableName);
                    //得到所有日志表
                    long src_time = this.getLastInsertTime(dbClientSTAT_LOG, serverId, tableName);

                    // 数据拉取到某一页
                    int m = 0;
                    long last_insert_Time = 0;
                    int last_insert_id = 0;
                    while (true){
                        String src_time_Time = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, new Date(src_time));
                        String pullSql = "select id,sid,time,platformName,userId,roleId,machineCode,sid,level from "+tableName+" where createTime >= '" + src_time_Time + "' order by createTime asc limit "+m*PAGE_SIZE+","+PAGE_SIZE+" ";
                        List<Map<String, Object>> loginLogList = targetDBClient.selectList(pullSql.toString());
                        //拉取数据列表
                        if(loginLogList != null && loginLogList.size()>0) {
                            //插入汇总库
                            StringBuilder insert_sql = new StringBuilder("insert ignore into stat_login(srcId,platId,serverId,userId,machineCode,roleId,timeLogin,time,sid,level,platformName) values");
                            for (int j = 0; j < loginLogList.size(); j++) {
                                Map<String, Object> loginLog = loginLogList.get(j);
                                insert_sql.append("(");
                                insert_sql.append("'").append(Integer.parseInt(loginLog.get("id").toString())).append("',");
                                insert_sql.append("'").append(loginLog.get("platformName")).append("',");  //平台名称
                                insert_sql.append("'").append(loginLog.get("sid")).append("',"); //服务器id
                                insert_sql.append("'").append(loginLog.get("userId")).append("',"); //用户id
                                insert_sql.append("'").append(loginLog.get("machineCode")).append("',"); //
                                insert_sql.append("'").append(loginLog.get("roleId")).append("',"); //角色id
                                Date date = new Date(Long.parseLong(loginLog.get("time").toString())*1000);
                                insert_sql.append("'").append(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS,date)).append("',");//时间
                                insert_sql.append("'").append(loginLog.get("time")).append("',"); //时间
                                insert_sql.append("'").append(loginLog.get("sid")).append("',"); //原服务器id
                                insert_sql.append("'").append(loginLog.get("level")).append("',"); //
                                insert_sql.append("'").append(loginLog.get("platformName")).append("'"); //
                                if(j == loginLogList.size()-1){
                                    insert_sql.append(")");
                                }else {
                                    insert_sql.append("),");
                                }
                                //记录最后一条日志的时间
                                last_insert_Time = Long.parseLong(loginLog.get("time").toString())*1000;
                                //记录最后一条日志的id
                                last_insert_id = Integer.parseInt(loginLog.get("id").toString());
                            }
                            //拉取日志 执行sql到 统计平台日志库
                            dbClientSTAT_LOG.executeUpdate(insert_sql.toString());
                        }
                        else {
                            break;
                        }
                        m+=1;
                    }
                    if(last_insert_id!=0){
                        if(src_time == 0){
                            StringBuilder insert_stat_last_insert_sql = new StringBuilder("insert  into stat_last_insert(sid,src_table,src_time,src_Id) values(?,?,?,?)");
                            dbClientSTAT_LOG.executeUpdate(insert_stat_last_insert_sql.toString(),serverId,tableName,last_insert_Time,last_insert_id);
                        }else{
                            String last_sql = "update stat_last_insert set src_time= "+last_insert_Time+" ,src_Id= "+last_insert_id+" where sid="+serverId+" and src_table='"+tableName+"'";
                            dbClientSTAT_LOG.executeUpdate(last_sql);
                        }
                    }
                      //logger.error("拉取完毕"+tableName);
                }
            }
        } catch (Exception e) {
            //logger.error(e.toString());
        }
    }
}
