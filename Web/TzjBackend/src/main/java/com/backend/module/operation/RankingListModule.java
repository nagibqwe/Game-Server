package com.backend.module.operation;

import com.backend.bean.Dblog;
import com.backend.bean.Server;
import com.backend.filter.MenuFilter;
import com.backend.gm.GameServerRequestUtil;
import com.backend.manager.DbLogListManager;
import com.backend.manager.ServerListManager;
import com.backend.struct.RankListGrid;
import com.backend.struct.RechargeRankVo;
import com.backend.utils.JsonUtils;
import com.backend.utils.QueryUtil;
import com.backend.utils.Toolkit;
import com.backend.utils.TypeReference;
import org.apache.log4j.Logger;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.util.NutMap;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@IocBean
@At("/rank")
@Ok("json")
@Fail("http:500")
public class RankingListModule {
	
	private static final Logger log = Logger.getLogger(RankingListModule.class);
	private static DateFormat sdfhm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private static final String dataTimeFormatter = "yyyy-MM-dd HH:mm:ss";


    private static final ExecutorService executor = Executors.newCachedThreadPool();

    @At
	@Ok("forward:${obj}")
	@Filters(@By(type = MenuFilter.class,args={"USERMENUS","/noauthority.jsp"}))
	public String getPage(int type, HttpServletRequest request) {
		switch (type) {
			case 2:
				return "/WEB-INF/jsp/rank/level.jsp";
			case 8:
				request.setAttribute("nowDate", sdfhm.format(new Date()));
				return "/WEB-INF/jsp/rank/pay.jsp";
			default:
				return "/404.jsp";
		}
	}
	
	@At
	public Object getRank(int serverId, int rankType){
		Server server = ServerListManager.getInstance().getServer(serverId);
		if (server == null) {
			log.error("服务器获取失败！sid=" + serverId);
			return Toolkit.outResult(false, Mvcs.getMessages(Mvcs.getReq()).get("log.serverfail"));
		}
		NutMap resultMap = GameServerRequestUtil.gmQueryRankList(server,rankType);

		if (!resultMap.getBoolean("ok")) {
			log.error(serverId + "排行榜获取失败！");
			return Toolkit.outResult(false, resultMap.get("msg"));
		}
		List<LinkedHashMap<String,Object>> list= JsonUtils.parseObject(resultMap.get("data").toString(), new TypeReference<List<LinkedHashMap<String,Object>>>(){});
		List<RankListGrid> grids = new ArrayList<>();
		for (LinkedHashMap<String,Object> linkedHashMap:list){
		    RankListGrid grid = new RankListGrid();
            for(Map.Entry<String, Object> entry : linkedHashMap.entrySet()) {
              if (entry.getKey().equals("rank")){
                  grid.setRank(String.valueOf(entry.getValue()));
              }else if (entry.getKey().equals("roleId")){
                  grid.setRoleId(String.valueOf(entry.getValue()));
              }else if (entry.getKey().equals("roleName")){
                  grid.setRoleName(String.valueOf(entry.getValue()));
              }else if (entry.getKey().equals("rankData")){
                  grid.setRankData(String.valueOf(entry.getValue()));
              }
            }
            grids.add(grid);
        }

        return Toolkit.outResult(true, grids);
	}

	@At
	public Object getRechargeRank(String[] serverId, String[] channelName, String startDate, String endDate, int type) {
        List<RechargeRankVo> list = new ArrayList<>();
        if (serverId == null || serverId.length == 0) {
            return Toolkit.outResult(false, "未选择服务器");
        }
        try {
            Date start = sdfhm.parse(startDate);
            Date end = sdfhm.parse(endDate);

            String table = "rechargelog";
            List<Map<String, Object>> result = new ArrayList<>();
            for (String sid : serverId) {
                Map<String, List<String>> hefuTableMap = QueryUtil.getInstance().getHefuTable(sid, table, start, end);
                for (String key : hefuTableMap.keySet()) {
                    Dblog dblog = DbLogListManager.getInstance().getDblog(key);
                    List<String> tableList = hefuTableMap.get(key);
                    for (String tb : tableList) {
                        String sql = buildRechargeRankSql(tb, start.getTime() / 1000, end.getTime() / 1000, channelName);
                        List<Map<String, Object>> data = QueryUtil.getInstance().query(dblog, sql);
                        result.addAll(data);
                    }
                }
            }
            if (result.isEmpty()) {
                return Toolkit.outResult(false, "无充值记录");
            }

            Map<Object, List<Map<String, Object>>> tempMap;
            if (type == 0) {
                tempMap = result.stream().collect(Collectors.groupingBy(n -> n.get("roleId")));
            } else {
                tempMap = result.stream().collect(Collectors.groupingBy(n -> n.get("userId")));
            }
            for (Map.Entry<Object, List<Map<String, Object>>> entry : tempMap.entrySet()) {
                RechargeRankVo vo = new RechargeRankVo();
                if (!entry.getValue().isEmpty()) {
                    List<Map<String, Object>> mapList = entry.getValue();
                    Map<String, Object> obj = mapList.get(0);
                    vo.setUserId(String.valueOf(obj.get("userId")));
                    vo.setRoleId(String.valueOf(obj.get("roleId")));
                    vo.setRoleName((String) obj.get("roleName"));
                    vo.setLevel(Integer.valueOf((String) obj.get("level")));
                    vo.setSex(Integer.valueOf((String)obj.get("sex")));
                    vo.setCareer(Integer.valueOf((String)obj.get("career")));
                    vo.setRechargeGold(Integer.valueOf((String)obj.get("rechargeGold")));
                    vo.setRemainGold(Integer.valueOf((String)obj.get("gold")));
                    vo.setCreateTime(obj.get("createTime").toString());
                    vo.setOnlineTime(Integer.valueOf((String)obj.get("onlineTime")));
                    Long lastLoginTime = Long.valueOf((String) obj.get("lastLoginTime"));
                    vo.setLastLoginTime(sdfhm.format(new Date(lastLoginTime * 1000)));
                    vo.setCreateSid(Integer.valueOf((String)obj.get("createsid")));
                    Stream<Integer> totalFee = mapList.stream().map(n -> Integer.valueOf(n.get("totalFee").toString()));
                    vo.setTotalRecharge(totalFee.reduce(0, Integer::sum));
                    Stream<Integer> totalFee1 = mapList.stream().map(n -> Integer.valueOf(n.get("totalFee").toString()));
                    vo.setMaxRecharge(totalFee1.reduce(0, Integer::max));
                    Stream<Integer> totalFee2 = mapList.stream().map(n -> Integer.valueOf(n.get("totalFee").toString()));
                    vo.setMinRecharge(totalFee2.reduce(Integer.MAX_VALUE, Integer::min));
                    vo.setRechargeCount(mapList.size());
                    Stream<Integer> rechargeTime = mapList.stream().map(n -> Integer.valueOf(n.get("time").toString()));
                    long lastRechargeTime = rechargeTime.reduce(0, Integer::max);
                    vo.setLastRechargeTime(sdfhm.format(new Date(lastRechargeTime * 1000)));
                    vo.setAvgRecharge(vo.getTotalRecharge() / vo.getRechargeCount());
                    list.add(vo);
                }
            }
        } catch (Exception e) {
            log.error(e, e);
            return Toolkit.outResult(false, "统计排行榜错误");
        }
        list.sort(Comparator.comparingInt(RechargeRankVo::getTotalRecharge).reversed());
        return Toolkit.outResult(true, list);
    }

    private String buildRechargeRankSql(String table, long start, long end, String[] channelNames) {
        String sql = "select re.roleId, r.userId, r.roleName, r.level, r.sex, r.career, r.rechargeGold, r.gold, " +
                "r.createTime, r.onlineTime, r.lastLoginTime, r.createsid, re.totalFee, re.time from " +
                table + " re " +
                "left join rolestate r on re.roleId = r.roleId " +
                "where re.totalFee > 10 and re.statusReason = 7 and re.time >= " + start + " and re.time <= " + end;
        StringBuilder builder = new StringBuilder(sql);
        if (channelNames != null) {
            builder.append(" and r.platformName in (");
            for (int i = 0; i < channelNames.length; i++) {
                builder.append(channelNames[i]);
                if (i != channelNames.length - 1) {
                    builder.append(",");
                }
            }
            builder.append(")");
        }
        return builder.toString();
    }

}
