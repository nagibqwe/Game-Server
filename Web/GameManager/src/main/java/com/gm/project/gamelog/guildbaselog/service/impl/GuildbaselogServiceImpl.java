package com.gm.project.gamelog.guildbaselog.service.impl;

import java.util.*;

import com.gm.common.dbclient.DBClient;
import com.gm.common.dbclient.DBServerMgr;
import com.gm.project.gmtool.server.domain.TServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gm.project.gamelog.guildbaselog.domain.Guildbaselog;
import com.gm.project.gamelog.guildbaselog.service.IGuildbaselogService;
import com.gm.common.utils.text.Convert;
import com.gm.project.common.utils.GameLogUtil;
/**
 * 公会基础信息Service业务层处理
 * 
 * @author gm
 * @date 2021-12-06
 */
@Service
public class GuildbaselogServiceImpl implements IGuildbaselogService 
{


    /**
     * 查询公会基础信息列表
     * 
     * @param guildbaselog 公会基础信息
     * @return 公会基础信息
     */
    @Override
    public List<Guildbaselog> selectGuildbaselogList(Guildbaselog guildbaselog, Map<String, Object> param)
    {
        Integer serverId = (Integer)param.get("serverId");
        Integer queryType = (Integer)param.get("queryType");
        String tableName = param.get("tableName").toString();

        String guildIdStr = guildbaselog.getGuildId()==null?"":" and guildId="+guildbaselog.getGuildId();
        String sqlStr = "";
        if(queryType == 0){//公会基础信息
            //1：创建公会，7：公会改名
            sqlStr = "select guildId, guildName from "+tableName+" where `type` in (1, 7) " + guildIdStr;
        }else if(queryType == 1){//公会会员信息
            sqlStr = "select guildId, guildName, roleId, roleName, 1 as isJoin, `time` from "+tableName+" where type in (1, 3) "+ guildIdStr + " union all " +
                    "select guildId, guildName, roleId, roleName, 0 as isJoin, `time` from "+tableName+" where type = 4 " + guildIdStr + " union all " +
                    "select guildId, guildName, actId, actName, 0 as isJoin, `time` from "+tableName+" where type in (2, 5) "+guildIdStr;
        }else if(queryType == 2){//公会动态信息
            sqlStr = "select * from " + tableName + " where 1 = 1 " + guildIdStr + " ORDER BY `time` DESC LIMIT 1000";
        }

        TServer finalHeFuServer = DBServerMgr.getInstance().getFinalHeFuServer(serverId);
        DBClient dbClient = DBServerMgr.getInstance().getLogDBClient(finalHeFuServer);
        if (dbClient == null) {
            return null;
        }

//        int count = dbClient.qryTotalCount(sqlStr);
//        param.put("count",count);

        //分页数据
//        if(param.containsKey("pageNum")){
//            int pageNum = (int)param.get("pageNum");
//            int pageSize = (int)param.get("pageSize");
//            where.append(" limit "+(pageNum-1)*pageSize+","+pageSize+"");
//        }

        List<Guildbaselog> guildbaselogs = new ArrayList<>();
        if(queryType == 1){
            List<Map<String, Object>> resultList= dbClient.selectList(sqlStr);

            //筛选出最终存在在公会的成员
            Map<String, Object> roleMap = new HashMap<>();
            Map<String, Map<String, Object>> memberMap = new HashMap<>();
            for (Map<String, Object> tempMap : resultList) {
                String roleId = tempMap.get("roleId").toString();
                String join = tempMap.get("isJoin").toString();
                long time = (long)tempMap.get("time");
                if (!roleMap.containsKey(roleId)) {
                    if ("1".equals(join)) {
                        memberMap.put(roleId, tempMap);
                    }
                    roleMap.put(roleId, time);
                } else {
                    if ("1".equals(join) && time > (long)roleMap.get(roleId)) {
                        memberMap.put(roleId, tempMap);
                        roleMap.put(roleId, time);
                    } else {
                        if (time > (long)roleMap.get(roleId)) {
                            memberMap.remove(roleId);
                            roleMap.put(roleId, time);
                        }
                    }
                }
            }

            //转化为公会对象
            Guildbaselog gb;
            for (Map<String, Object> map:memberMap.values()) {
                long guildId = (long)map.get("guildId");
                String guildName = map.get("guildName").toString();
                long roleId = (long)map.get("roleId");
                String roleName = map.get("roleName").toString();

                gb = new Guildbaselog();
                gb.setGuildId(guildId);
                gb.setGuildName(guildName);
                gb.setRoleId(roleId);
                gb.setRoleName(roleName);
                guildbaselogs.add(gb);
            }
        }else{
            List<Guildbaselog> resultList= dbClient.selectList(sqlStr);
            if(resultList!=null){
                guildbaselogs.addAll(resultList);
            }
        }

        return guildbaselogs;
    }
}
