package com.backend.module.operation;

import com.backend.bean.Dblog;
import com.backend.filter.MenuFilter;
import com.backend.manager.BlackListManager;
import com.backend.manager.DbLogListManager;
import com.backend.manager.ItemManager;
import com.backend.utils.JsonUtils;
import com.backend.utils.QueryUtil;
import com.backend.utils.TypeReference;
import com.backend.utils.VersionUpdateUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.mvc.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@IocBean
@Ok("json")
@At("/packageSearch")
@Fail("http:500")
public class PackageModule {

	private DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	@At
	@Ok("jsp:jsp.operation.package")
	@Filters(@By(type = MenuFilter.class, args = { "USERMENUS","/noauthority.jsp" }))
	public void index(HttpServletRequest request){
		request.setAttribute("nowDate", sdf.format(new Date()));
	}
	
	@At
	public Object getPackage(String platfName , String serverIds , String channelNames ,boolean isblack , String selectType , String condition,String startDate , String endDate) throws Exception{
		List<Map<String, Object>> dataList = new ArrayList<>();
		if(!Strings.isBlank(channelNames)){
			channelNames = "'" + channelNames + "'";
			channelNames = channelNames.replace(",", "','");
		}
		String blackUserStr = "";//黑名单账号
		if(isblack){
			List<Map<String,Object>> blackList = BlackListManager.getInstance().getBlackList(platfName);
			Set<String> blackSet = new HashSet<>();
			for(Map<String,Object> map : blackList){
				blackSet.add(map.get("userId").toString());
			}
			blackUserStr = blackSet.toString();
			blackUserStr = blackUserStr.substring(1,blackUserStr.length()-1);
		}
		String[] serverId = serverIds.split(",");
		for (String s : serverId) {
			int finalServerId = QueryUtil.getInstance().getHeFuId(Integer.parseInt(s));
			Dblog dblog = DbLogListManager.getInstance().getDBServer(platfName, finalServerId);
			String ROLESTATE = "rolestate";
			String rolestateSqlStr = getRoleStateSql(ROLESTATE, s, channelNames, selectType, condition, blackUserStr, startDate, endDate);
			List<Map<String, Object>> dataMap = QueryUtil.getInstance().query(dblog, rolestateSqlStr);
			Set<String> roleSet = new HashSet<>();
			for (Map<String, Object> map : dataMap) {
				roleSet.add(map.get("roleId").toString());
			}
			String roleIdStr = roleSet.toString().replace("[", "").replace("]", "");
			String ROLEITEMS = "roleitems";
			String roleItemsSqlStr = getRoleItems(ROLEITEMS, roleIdStr);
			List<Map<String, Object>> roleItemsMap =  QueryUtil.getInstance().query(dblog, roleItemsSqlStr);
			for (Map<String, Object> map : roleItemsMap) {
				if (map.containsKey("bags") && map.get("bags") != null) {
					String bagsStr = "";
					String bags = VersionUpdateUtil.dataLoad(map.get("bags").toString());
					JSONObject bagsJson = JsonUtils.parseObject(bags, JSONObject.class);
					if (bagsJson != null) {
						for (Object key : bagsJson.keySet()) {
							// 获得key
							// 根据key获得value, value也可以是JSONObject,JSONArray,使用对应的参数接收即可
							String value = bagsJson.getString(key.toString());
							JSONObject valueJson = JsonUtils.parseObject(value, JSONObject.class);
							bagsStr += ItemManager.getInstance().getStr(valueJson.getString("itemModelId"), "item") + "_";
							bagsStr += valueJson.getString("num");
							bagsStr += ";";
						}
						if (bagsStr.length() != 0) {
							bagsStr = bagsStr.substring(0, bagsStr.length() - 1);
						}
					}
					map.put("bags", bagsStr);

				}
				if (map.containsKey("stores") && map.get("stores") != null) {
					String storesStr = "";
					String stores = VersionUpdateUtil.dataLoad(map.get("stores").toString());
					JSONObject storesJson = JsonUtils.parseObject(stores, JSONObject.class);
					if (storesJson != null) {
						for (Object key : storesJson.keySet()) {
							// 根据key获得value, value也可以是JSONObject,JSONArray,使用对应的参数接收即可
							String value = storesJson.getString(key.toString());
							JSONObject valueJson = JsonUtils.parseObject(value, JSONObject.class);
							storesStr += ItemManager.getInstance().getStr(valueJson.getString("itemModelId"), "item") + "_";
							storesStr += valueJson.getString("num");
							storesStr += ";";
						}
						if (storesStr.length() != 0) {
							storesStr = storesStr.substring(0, storesStr.length() - 1);
						}
					}
					map.put("stores", storesStr);
				}
				if (map.containsKey("equips") && map.get("equips") != null) {
					String equipsStr = "";
					String equips = VersionUpdateUtil.dataLoad(map.get("equips").toString());
					List<HashMap<String,Object>> equipsJson = JsonUtils.parseObject(equips,new TypeReference<List<HashMap<String,Object>>>(){});
					for (int i = 0; i < equipsJson.size(); i++) {
						HashMap<String,Object> json = equipsJson.get(i);
						if (json != null) {
							equipsStr += ItemManager.getInstance().getStr(json.get("itemModelId").toString(), "item") + "_";
							equipsStr += json.get("num");
							equipsStr += ";";
						}
					}
					if (equipsStr.length() != 0) {
						equipsStr = equipsStr.substring(0, equipsStr.length() - 1);
					}
					map.put("equips", equipsStr);
				}
			}
			dataList.addAll(roleItemsMap);
		}
		return dataList ;
	}
	
	//查询出roleId(分两种情况，1是查出所在的服务器以及渠道的所有roleId,2是根据角色名查询roleId)
	private String getRoleStateSql(String table , String serverId , String platformNameStr , String selectType , String condition , String blackUserStr , String startDate , String endDate){
		String sqlStr = " SELECT roleId ";
		sqlStr += " FROM " + table ;
		sqlStr += " WHERE createsid IN ('" + serverId + "') ";
		if(!platformNameStr.equals("")){
			sqlStr += "AND platformName IN (" + platformNameStr + ")";
		}
		if(selectType.equals("1")){
			sqlStr += " AND roleName LIKE '%" + condition + "%'";
		}else if(selectType.equals("0")){
			sqlStr += " AND roleId IN (" + condition + ")";
		}else if(selectType.equals("-1")){
			sqlStr += " AND UNIX_TIMESTAMP(createTime) BETWEEN UNIX_TIMESTAMP('" + startDate + " 00:00:00')  AND UNIX_TIMESTAMP('" + endDate + " 23:59:59')";
		}
		if(!blackUserStr.equals("")){
			sqlStr += " AND userId NOT IN (" + blackUserStr + ")";
		}
		return sqlStr;
	}
	
	//根据roleId查询背包的物品以及仓库物品，已经穿上的装备
	private String getRoleItems(String table , String roleId){
		String sqlStr = " SELECT roleId,bags,stores,equips";
		sqlStr += " FROM " + table ;
		sqlStr += " WHERE roleId IN (" + roleId + ")";
		return sqlStr ;
	}
}
