package com.kits.project.serverList.dao;

import com.kits.common.dbclient.DBClient;
import com.kits.project.serverList.BaseDao;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class QueryNoticeDao extends BaseDao {

    public static List<Map<String, Object>> selectNotice(DBClient dbClient, String channelId, String date){
        if (null == dbClient)
            return null;
        long currentTimeMillis = System.currentTimeMillis();
        String sql = "SELECT sn.auto,sn.notice_content,sn.notice_name,sn.notice_type FROM sdk_notice sn WHERE sn.status = 1 AND sn.channel LIKE '%"+channelId+"%' AND sn.end_time >= '"+date+"'";
        List<Map<String, Object>> noticeList = dbClient.selectList(sql);
        if (null != noticeList)
            info("selectNotice: " + noticeList.size() + " num " + getDurationToNow(currentTimeMillis) + "ms");
        return noticeList;
    }
}
