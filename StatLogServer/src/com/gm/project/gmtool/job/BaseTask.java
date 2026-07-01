package com.gm.project.gmtool.job;

import com.gm.common.dbclient.DBClient;
import com.gm.common.dbclient.DBServerMgr;
import com.gm.common.utils.DateUtils;
import com.gm.common.utils.StringUtils;
import com.gm.project.gmtool.service.StatService;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 基础任务
 */
public abstract class BaseTask extends Worker {


    private static Logger logger = Logger.getLogger(BaseTask.class);
    protected String tableName;
    /**
     * 每次拉取多少条数据
     */
    public static int PAGE_SIZE = 2000;
    /**
     * 下次执行时间
     */
    public long nextExecuteTime = 0;
    /**
     * 执行间隔
     */
    public int interval = 0;
    public void execute() throws JobExecutionException{
        long start = System.currentTimeMillis();
        if(start>nextExecuteTime){
            this.execute(null);
            nextExecuteTime = start + interval;
        }
    }
    /**
     * 定时器执行入口
     *
     * @param arg0
     * @throws JobExecutionException
     */
    public void execute(JobExecutionContext arg0) throws JobExecutionException {

        long start = System.currentTimeMillis();

//        if (StatService.getInstance().getIsRunMap().containsKey(this.getClass().getSimpleName())) {
//            if (StatService.getInstance().getIsRunMap().get(this.getClass().getSimpleName())) {
//                logger.debug("===========统计中 线程名字:" + Thread.currentThread().getName() + " 任务名：" + this.getClass().getSimpleName() + "正在统计中... 日期:" + DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, new Date(start)));
//                return;
//            }
//        }
        try {
           // logger.debug("++++++++开始统计 线程名字:" + Thread.currentThread().getName() + " 任务名：" + this.getClass().getSimpleName() + " 日期:" + DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, new Date(start)));
           // StatService.getInstance().getIsRunMap().put(this.getClass().getSimpleName(), true);
            DBClient gm_dbClient = null;
            try {
                //获取gm日志库
                gm_dbClient = DBServerMgr.getInstance().getDBClient(DBServerMgr.DBServer.GM);
            } catch (Exception e) {
                //获取gm连接失败
                logger.error("get GM DBClient connect  error");
                return;
            }
            if (gm_dbClient == null) {
                //连接为空
                logger.error("dbClient is null");
                return;
            }

            //得到服务器日志地址
            List<Map<String, Object>> tDblogList = gm_dbClient.selectList("SELECT * FROM t_server");
            if (tDblogList != null && tDblogList.size() > 0) {
                for (int i = 0; i < tDblogList.size(); i++) {
                    //获取服务器对应的日志库信息
                    Map<String, Object> tDblogBeanMap = tDblogList.get(i);
                    if (tDblogBeanMap == null) {
                        logger.error("tDblogBeanMap is null");
                        continue;
                    }
                    int serverType = Integer.parseInt(tDblogBeanMap.get("serverType").toString());
                    //只拉取服务器日志库
                    if (serverType != 1 && serverType !=0) {
                        continue;
                    }

                    String dblogIp =  tDblogBeanMap.get("dblogIp").toString();
                    if (StringUtils.isBlank(dblogIp)) {
                        continue;
                    }
                    String dblogName =  tDblogBeanMap.get("dblogName").toString();
                    if (StringUtils.isBlank(dblogName)) {
                        continue;
                    }

                    String dblogUser =  tDblogBeanMap.get("dblogUser").toString();
                    if (StringUtils.isBlank(dblogUser)) {
                        continue;
                    }

                    String dblogPwd =  tDblogBeanMap.get("dblogPwd").toString();
                    if (StringUtils.isBlank(dblogPwd)) {
                        continue;
                    }


                    DBClient targetDBClient = null;
                    try {
                        targetDBClient = DBServerMgr.getInstance().getLogDBClient(tDblogBeanMap);
                    } catch (Exception e) {
                        logger.error("targetDBClient is connect error" + tDblogBeanMap.get("serverName").toString());
                        continue;
                    }
                    if (targetDBClient == null) {
                        logger.error("targetDBClient is null" + tDblogBeanMap.get("serverName").toString());
                        continue;
                    }
                    DBClient dbClientSTAT_LOG = DBServerMgr.getInstance().getDBClient(DBServerMgr.DBServer.STAT_LOG);
                    if (dbClientSTAT_LOG == null) {
                        logger.error("dbClientSTAT_LOG is connect error");
                        return;
                    }
                    //拉取日志
                 //   logger.error("pullServerLog begin -->serverId:" + tDblogBeanMap.get("serverId").toString() + " tableName:" + tableName);
                    long beginTime = System.currentTimeMillis();
                    this.pullServerLog(tDblogBeanMap, targetDBClient, dbClientSTAT_LOG);
                    logger.error("pullServerLog end-->serverId:" + tDblogBeanMap.get("serverId").toString() + " tableName:" + tableName+" time:"+(System.currentTimeMillis()-beginTime));
                }
            }
        } finally {
            //StatService.getInstance().getIsRunMap().put(this.getClass().getSimpleName(), false);
           // logger.debug("--------结束统计 线程名字:" + Thread.currentThread().getName() + " 任务名称" + this.getClass().getSimpleName() + " 消耗时间" + (System.currentTimeMillis() - start));
        }
    }

    /**
     * 拉取日志逻辑  具体拉取不同的日志库 对应不同逻辑
     *
     * @param tDblogBeanMap
     * @param targetDBClient
     * @param dbClientSTAT_LOG
     */
    public abstract void pullServerLog(Map<String, Object> tDblogBeanMap, DBClient targetDBClient, DBClient dbClientSTAT_LOG);


    /**
     * 查询拉取记录时间
     *
     * @param dbClientGM
     * @param serverId
     * @param tableName
     * @return
     * @throws SQLException
     */
    public long getLastInsertTime(DBClient dbClientGM, String serverId, String tableName) throws SQLException {
        String stat_last_insert_sql = "select src_time from stat_last_insert where sid = " + serverId + " and src_table = '" + tableName + "' order by src_time desc limit 1";
        long src_time = 0;
        List<Map<String, Object>> srcinfo = dbClientGM.selectList(stat_last_insert_sql);
        if (srcinfo == null || srcinfo.size() <= 0) {
            src_time = 0;

        } else {
            String serverStr = srcinfo.get(0).get("src_time").toString();
            src_time = Long.parseLong(serverStr) - 600;
        }
        return src_time;
    }

    /**
     * 查询拉取 下标记录
     *
     * @param dbClientGM
     * @param serverId
     * @param tableName
     * @return
     * @throws SQLException
     */
    public int getLastInsertId(DBClient dbClientGM, String serverId, String tableName) throws SQLException {
        String stat_last_insert_sql = "select src_id from stat_last_insert where sid = " + serverId + " and src_table = '" + tableName + "' order by src_id desc limit 1";
        int src_id = 0;
        List<Map<String, Object>> srcinfo = dbClientGM.selectList(stat_last_insert_sql);
        if (srcinfo == null || srcinfo.size() <= 0) {
            src_id = 0;
        } else {
            String src_idStr = srcinfo.get(0).get("src_id").toString();
            src_id = Integer.parseInt(src_idStr);
        }
        return src_id;
    }

    public String toDateTime(Object time){
        Date date = DateUtils.parseDate(time.toString());
        return DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, date);
    }
    public String  secondToDateTime(Object time){
        Date date = new Date(Long.parseLong(time.toString()) * 1000);
        return DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, date);
    }
    public String  milliscondToDateTime(Object time){
        Date date = new Date(Long.parseLong(time.toString()));
        return DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, date);
    }


}
