package com.kits.project.serverList.dao;

import com.kits.common.dbclient.DBClient;
import com.kits.project.serverList.BaseDao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class QueryServerListDao extends BaseDao {
    /**
     * 根据渠道ID查询渠道信息
     * @param dbClient
     * @param channelId
     * @return
     */
    public static List<Map<String, Object>> selectSdkChannelList(DBClient dbClient,int channelId){
        if (null == dbClient)
            return null;
        long currentTimeMillis = System.currentTimeMillis();
        String sql = "SELECT channel_id FROM sdk_channel sc WHERE sc.channel_id=?";
        List<Map<String, Object>> channelList = dbClient.selectList2(sql,channelId);
        if (null != channelList)
            info("selectSdkChannelList: " + channelList.size() + " num " + getDurationToNow(currentTimeMillis) + "ms");
        return channelList;
    }

    /**
     * 根据渠道ID查询服务器列表信息
     * @param dbClient
     * @param channelId
     * @return
     */
    public static List<Map<String, Object>> selectSdkServerListByChannelId(DBClient dbClient,int channelId){
        if (null == dbClient)
            return null;
        long currentTimeMillis = System.currentTimeMillis();
        String sql = "SELECT `ssl`.id,`ssl`.login_server_ip,`ssl`.login_server_name,`ssl`.login_server_port FROM sdk_channelid_channelids scc JOIN sdk_server_list `ssl` ON scc.channel_ids= `ssl`.channel_ids WHERE scc.channel_id=?";
        List<Map<String, Object>> sdkServerLists = dbClient.selectList2(sql,channelId);
        if (null != sdkServerLists)
            info("selectSdkServerListByChannelId: " + sdkServerLists.size() + " num " + getDurationToNow(currentTimeMillis) + "ms");
        return sdkServerLists;
    }

    /**
     * 根据用户ID查询白名单信息(是否为白名单)
     * @param dbClient
     * @param user_id
     * @return
     */
    public static List<Map<String, Object>> selectWhiteByWhiteName(DBClient dbClient,String user_id){
        if (null == dbClient)
            return null;
        long currentTimeMillis = System.currentTimeMillis();
        String sql = "select white_name from sdk_white where white_name = ?";
        List<Map<String, Object>> sdkWhite = dbClient.selectList2(sql,user_id);
        if (null != sdkWhite)
            info("selectWhiteByWhiteName: " + sdkWhite.size() + " num " + getDurationToNow(currentTimeMillis) + "ms");
        return sdkWhite;
    }

    /**
     * 是白名单时全部满足条件的服务器列表都可以获取
     * @param dbClient
     * @param serverListId
     * @param appVersion
     * @param sceId
     * @return
     */
    public static List<Map<String, Object>> selectServerExtraBySceIdAddAppVersion(DBClient dbClient,long serverListId,Boolean isWhite,String appVersion,String sceId,int openState,int serverStatus,int serverStatus2){
        if (null == dbClient)
            return null;
        long currentTimeMillis = System.currentTimeMillis();
        String queryInfo = "SELECT ss.server_id, ss.server_name,ss.server_ip,ss.server_port,ss.open_state,ss.is_backup,sse.sort_id,sse.server_status,sse.group_type,sse.server_label" +
                " FROM sdk_server_extra sse JOIN sdk_server ss ON ss.server_id = sse.server_id WHERE";
        StringBuffer wheresql = new StringBuffer();
        StringBuffer wheresql2 = new StringBuffer();
        wheresql.append(" ss.is_backup = 0");
        wheresql2.append(" ss.is_backup = 1");
        if (serverListId != 0){
            wheresql.append(" AND sse.server_list_id="+serverListId+"");
            wheresql2.append(" AND sse.server_list_id="+serverListId+"");
        }
        if (null != appVersion && !appVersion.equals("")){
            wheresql.append(" AND (sse.appVersion='"+appVersion+"' or sse.appVersion='')");
            wheresql2.append(" AND (sse.appVersion='"+appVersion+"' or sse.appVersion='')");
        }else if (null != appVersion && appVersion.equals("")){
            wheresql.append(" AND sse.appVersion=''");
            wheresql2.append(" AND sse.appVersion=''");
        }
        if (null != sceId && !sceId.equals("")){
            wheresql.append(" AND (sse.sce_id like '%"+sceId+"%' or sse.sce_id='')");
            wheresql2.append(" AND (sse.sce_id like '%"+sceId+"%' or sse.sce_id='')");
        }else if (null != sceId && sceId.equals("")){
            wheresql.append(" AND sse.sce_id=''");
            wheresql2.append(" AND sse.sce_id=''");
        }
        if (!isWhite){//非白名单时只能查询open_state为1和server_status为1的服务器列表(状态为开启)和server_status为2的服务器
            wheresql.append(" AND ((sse.server_status="+serverStatus+") or (sse.server_status="+serverStatus2+"))");
            wheresql2.append(" AND ((ss.open_state="+openState+" AND sse.server_status="+serverStatus+") or (ss.open_state="+openState+" AND sse.server_status="+serverStatus2+"))");
        }

        List<Map<String, Object>> serverAndExtras = dbClient.selectList(queryInfo+wheresql);//服务器不为备服的查询结果
        List<Map<String, Object>> serverAndExtras2 = dbClient.selectList(queryInfo+wheresql2);//服务器为备服的查询结果
//        System.out.println("sql1:"+queryInfo+wheresql);
//        System.out.println("sql2:"+queryInfo+wheresql2);
        serverAndExtras.addAll(serverAndExtras2);
        if (null != serverAndExtras)
            info("selectServerExtraBySceIdAddAppVersion: " + serverAndExtras.size() + " num " + getDurationToNow(currentTimeMillis) + "ms");
        return serverAndExtras;
    }

    /**
     * 获取为备服的服务器
     * @param dbClient
     * @return
     */
    public static List<Map<String, Object>> selectServerIdsByIsBackup(DBClient dbClient){
        if (null == dbClient)
            return null;
        long currentTimeMillis = System.currentTimeMillis();
        String sql = "select server_id from sdk_server where is_backup = 1";//获取为备服的服务器
        List<Map<String, Object>> serverIds = dbClient.selectList(sql);
        if (null != serverIds)
            info("selectServerIdsByIsBackup: " + serverIds.size() + " num " + getDurationToNow(currentTimeMillis) + "ms");
        return serverIds;
    }

    /**
     * 更新服务器的open_state
     * @param dbClient
     * @param openState
     * @param serverId
     * @return
     */
    public static int updateServerOpenState(DBClient dbClient,int openState,int serverId){
        if (null == dbClient)
            return 0;
        long currentTimeMillis = System.currentTimeMillis();
        String sql = "update sdk_server set open_state = "+openState+" where server_id = "+serverId+"";//更新服务器的open_state
        int updateRow = 0;
        try {
            updateRow = dbClient.executeUpdate(sql);
            if (0 != updateRow)
                info("updateServerOpenState: " + updateRow + " num " + getDurationToNow(currentTimeMillis) + "ms");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return updateRow;
    }
}
