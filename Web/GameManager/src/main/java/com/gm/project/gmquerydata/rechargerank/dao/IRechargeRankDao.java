package com.gm.project.gmquerydata.rechargerank.dao;


import java.util.List;
import java.util.Map;

public interface IRechargeRankDao {

    public List<Map<String, Object>> rechargeRankByRoleIdDataList(long start, long end, String channelNames,String selectServerIds,Map<String,Object> param);

    public List<Map<String, Object>> rechargeRankByUserIdDataList(long start, long end, String channelNames,String selectServerIds,Map<String,Object> param);
}
