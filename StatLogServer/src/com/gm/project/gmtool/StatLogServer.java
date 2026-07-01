package com.gm.project.gmtool;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.gm.common.dbclient.DBClient;
import com.gm.common.dbclient.DBServerMgr;
import com.gm.common.utils.StringUtils;
import com.gm.project.gmtool.job.StatLoginLogTask;
import com.gm.project.gmtool.job.StatRechargeTask;
import com.gm.project.gmtool.job.StatRoleStateTask;
import com.gm.project.gmtool.job.Worker;
import com.gm.project.httpServer.HttpServer;
import org.apache.log4j.Logger;

import com.gm.common.utils.PropertiesUtil;
import com.gm.project.gmtool.config.StatConfigService;
import com.gm.project.gmtool.service.StatService;
import org.apache.log4j.xml.DOMConfigurator;

/**
 * 统计日志服务器
 */
public class StatLogServer {
    private static Logger logger = Logger.getLogger(StatLogServer.class);
    public static void main(String[] args) {
        File f = new File(System.getProperty("user.dir"));
        String configPath = f.getPath() + File.separator +  "config" + File.separator + "config.properties";
        //初始化统计服务器配置
        Properties properties = PropertiesUtil.getInstance().readProperties(configPath);
        if(properties == null){
             System.exit(0);
        }

        logger.error("config path:" + configPath);
        StatConfigService.getInstance().init(properties);
        //监听一个服务器端口 防止重复启动
        HttpServer httpServer = new HttpServer(Integer.parseInt(StatConfigService.getInstance().getValue("gm.http.port")));
        try {
            httpServer.start();
        } catch (Exception e) {
            System.exit(0);
            e.printStackTrace();
        }
        String logPath = f.getPath() + File.separator +  "config" + File.separator + "log4j.xml";
        //System.setProperty("logFileName", "gate80");
        DOMConfigurator.configure(logPath);
        //开始统计服务
        //StatService.getInstance().startService();
        //测试db服务器
        testDB();
        //
        initWorker();
        logger.error("stat log server is start");
    }

    public static void start(Worker worker){
        worker.setSleepTime(1000);
        Thread thread = new Thread(worker);
        thread.start();
    }
    //日志收集定时任务
   public static  void initWorker(){
       StatLoginLogTask statLoginLogTask =  new StatLoginLogTask();
       start(statLoginLogTask);

       StatRechargeTask statRechargeTask =  new StatRechargeTask();
       start(statRechargeTask);

       StatRoleStateTask statRoleStateTask =  new StatRoleStateTask();
       start(statRoleStateTask);

       Runtime.getRuntime().addShutdownHook(new Thread(){
           @Override
           public void run() {
               statLoginLogTask.stop_wait();
               statRechargeTask.stop_wait();
               statRoleStateTask.stop_wait();
           }
       });

   }
    /**
     * 测试db服务器
     */
    public static void testDB(){

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
                if (serverType != 1 && serverType != 0) {
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
                logger.error("targetDBClient " + tDblogBeanMap.get("serverName").toString() + "连接成功！！！！");
            }
        }
    }
}
