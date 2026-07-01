package com.kits.project.serverList.job.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kits.common.dbclient.DBClient;
import com.kits.common.dbclient.DBServerMgr;
import com.kits.project.serverList.dao.QueryServerListDao;
import com.kits.common.utils.LogUtil;
import com.kits.common.utils.http.HttpUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 目前没有使用这个方法,使用的是框架自带定时器任务
 */
public class GetServerStateTask implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //向APIserver获取自动开服的服务器状态
        LogUtil.info("向APIserver获取自动开服的服务器状态");
        InputStream inStream = GetServerStateTask.class.getClassLoader().getResourceAsStream("jdbc.properties");
        Properties prop = new Properties();
        String getServerStateUrl = "";

        try {
            DBClient dbClient = DBServerMgr.getInstance().getDBClient(DBServerMgr.DBServer.GM);
            prop.load(inStream);
            getServerStateUrl = prop.getProperty("getServerStateUrl");
            String serverStateResult = HttpUtils.sendGet(getServerStateUrl, "serverId=0");
            JSONObject root = JSON.parseObject(serverStateResult);
            JSONArray dataJSONArray = root.getJSONArray("data");

            List<Map<String, Object>> serverIds = QueryServerListDao.selectServerIdsByIsBackup(dbClient);
            for(int i = 0; i < dataJSONArray.size(); i++){
                JSONObject jsonObject = dataJSONArray.getJSONObject(i);
                int serverId = jsonObject.getIntValue("serverId");
                int openState = jsonObject.getIntValue("openState");
                for (Map<String, Object> stringObjectMap:serverIds){
                    if (serverId == Integer.parseInt(stringObjectMap.get("server_id").toString())){
                        QueryServerListDao.updateServerOpenState(dbClient,openState,serverId);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
