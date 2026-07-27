package com.gm.project.gmquerydata.rechargerank.service.impl;

import com.gm.project.common.utils.StatUtil;
import com.gm.project.gamelog.rolestate.domain.RoleState;
import com.gm.project.gamelog.rolestate.service.IRoleStateService;
import com.gm.project.gmquerydata.rechargerank.dao.IRechargeRankDao;
import com.gm.project.gmquerydata.rechargerank.domain.RechargeRankBean;
import com.gm.project.gmquerydata.rechargerank.service.IRechargeRankService;
import com.gm.project.gmtool.utils.DateUtil;
import com.gm.project.gmtool.utils.TimeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
public class RechargeRankServiceImpl implements IRechargeRankService {

    /**
     * 数据库相关操作
     */
    @Autowired
    public IRechargeRankDao rechargeRankDao;
    @Autowired
    public IRoleStateService roleStateService;
    /**
     * @param groupName
     * @param selectServerIdList
     * @param channelNames
     * @param startDate
     * @param endDate
     * @param type
     */
    public List<RechargeRankBean> rechargeRank(String groupName, String selectServerIdList, String channelNames, String startDate, String endDate, Integer type, Map<String, Object> param) throws ParseException {
        List<RechargeRankBean> list = new ArrayList<>();
        //渠道名称
        if (!StringUtils.isBlank(channelNames)) {
            channelNames = "'" + channelNames + "'";
            channelNames = channelNames.replace(",", "','");
        }

        Date start = StatUtil.sdfhm.parse(startDate);
        Date end = StatUtil.sdfhm.parse(endDate);
        List<Map<String, Object>> dataList = null;
        //按照角色去重复
        if (type == 0) {
            dataList = this.rechargeRankDao.rechargeRankByRoleIdDataList(start.getTime() / 1000, end.getTime() / 1000, channelNames, selectServerIdList, param);
        }else if(type == 1){
            dataList = this.rechargeRankDao.rechargeRankByUserIdDataList(start.getTime() / 1000, end.getTime() / 1000, channelNames, selectServerIdList, param);

        }
        if (dataList == null || dataList.size() == 0) {
            return list;
        }
        //到原来的服务器查询 角色信息
        Map<Integer,Set<String>> roleServerMap = new HashMap<>();
        for (int i = 0; i < dataList.size(); i++) {
            Map<String, Object> obj = dataList.get(i);
            int createSid = Integer.parseInt(obj.get("sid").toString());
            Set<String> roleList;
            //查询角色实时信息
            if(roleServerMap.containsKey(createSid))
            {
                roleList = roleServerMap.get(createSid);
            }else {
                roleList = new HashSet<>();
                roleServerMap.put(createSid,roleList);
            }
            roleList.add(String.valueOf(obj.get("roleId")));
        }
        Map<Long,RoleState> roleStateMap = new HashMap<>();
        if(roleServerMap.size()>0){
            for(int serverId : roleServerMap.keySet()){
               String roles = StringUtils.join(roleServerMap.get(serverId),",");
                List<RoleState> roleStateList = roleStateService.selectRoleStateList(serverId,roles);
                if(roleStateList!=null && roleStateList.size()>0){
                    for(int j = 0;j<roleStateList.size();j++){
                        roleStateMap.put(roleStateList.get(j).getRoleId(),roleStateList.get(j));
                    }
                }
            }
        }

        int pageNum = (int)param.get("pageNum");
        int pageSize = (int)param.get("pageSize");
        int rank = (pageNum-1)*pageSize;
        for (int i = 0; i < dataList.size(); i++) {
            Map<String, Object> obj = dataList.get(i);
            RechargeRankBean vo = new RechargeRankBean();
            vo.setUserId(String.valueOf(obj.get("userId")));
            vo.setRoleId(String.valueOf(obj.get("roleId")));
            vo.setTotalRecharge(Integer.parseInt(obj.get("totalRecharge").toString()));
            vo.setMaxRecharge(Integer.parseInt(obj.get("maxRecharge").toString()));
            vo.setMinRecharge(Integer.parseInt(obj.get("minRecharge").toString()));
            vo.setRechargeCount(Integer.parseInt(obj.get("rechargeCount").toString()));
            vo.setAvgRecharge(Float.parseFloat(obj.get("avgRecharge").toString()));
            vo.setCreateSid(Integer.parseInt(obj.get("sid").toString()));
            vo.setRank(rank+i+1);
            if(roleStateMap.containsKey(Long.parseLong(vo.getRoleId()))){
                RoleState roleState = roleStateMap.get(Long.parseLong(vo.getRoleId()));
                vo.setRoleName(roleState.getRoleName());
                vo.setLevel(roleState.getLevel());
                vo.setCareer(roleState.getCareer());
                vo.setOnlineTime(roleState.getOnlineTime());
                vo.setRechargeGold(roleState.getRechargeGold());
                vo.setRemainGold(roleState.getGold());
                long lastLoginTimeBig =roleState.getLastLoginTime() * 1000L;
                vo.setLastLoginTime(TimeUtils.format2string(lastLoginTimeBig));
                vo.setCreateTime(roleState.getCreateTime());


                //long lastRechargeTime =  Long.parseLong(obj.get("time").toString());
                vo.setLastRechargeTime(obj.get("time").toString());

            }
            list.add(vo);
        }

//        Map<Object, List<Map<String, Object>>> tempMap;
//        if (type == 0) {
//            tempMap = dataList.stream().collect(Collectors.groupingBy(n -> n.get("roleId")));
//        } else {
//            tempMap = dataList.stream().collect(Collectors.groupingBy(n -> n.get("userId")));
//        }
//        for (Map.Entry<Object, List<Map<String, Object>>> entry : tempMap.entrySet()) {
//            RechargeRankBean vo = new RechargeRankBean();
//            if (!entry.getValue().isEmpty()) {
//                List<Map<String, Object>> mapList = entry.getValue();
//                Map<String, Object> obj = mapList.get(0);
//
//
//                vo.setUserId(String.valueOf(obj.get("userId")));
//                vo.setRoleId(String.valueOf(obj.get("roleId")));
//                vo.setRoleName((String) obj.get("roleName"));
//                vo.setLevel((int)obj.get("level"));
//                //vo.setSex(Integer.valueOf((String)obj.get("sex")));
//                vo.setCareer((int)obj.get("career"));
//                vo.setRechargeGold((long)obj.get("rechargeGold"));
//                vo.setRemainGold((long)obj.get("gold"));
//                vo.setCreateTime(obj.get("createTime").toString());
//                vo.setOnlineTime((int)obj.get("onlineTime"));
//                int lastLoginTime = (int)obj.get("lastLoginTime");
//                long lastLoginTimeBig = lastLoginTime * 1000L;
//                vo.setLastLoginTime(TimeUtils.format2string(lastLoginTimeBig));
//                vo.setCreateSid((int)obj.get("createsid"));
//                Stream<Integer> totalFee = mapList.stream().map(n -> Integer.valueOf(n.get("totalFee").toString()));
//                vo.setTotalRecharge(totalFee.reduce(0, Integer::sum));
//                Stream<Integer> totalFee1 = mapList.stream().map(n -> Integer.valueOf(n.get("totalFee").toString()));
//                vo.setMaxRecharge(totalFee1.reduce(0, Integer::max));
//                Stream<Integer> totalFee2 = mapList.stream().map(n -> Integer.valueOf(n.get("totalFee").toString()));
//                vo.setMinRecharge(totalFee2.reduce(Integer.MAX_VALUE, Integer::min));
//                vo.setRechargeCount(mapList.size());
//                Stream<Long> rechargeTime = mapList.stream().map(n ->  (long)n.get("timesec"));
//                long lastRechargeTime = rechargeTime.reduce(0L, Long::max);
//                vo.setLastRechargeTime(StatUtil.sdfhm.format(new Date(lastRechargeTime * 1000)));
//                float avgRecharge = vo.getTotalRecharge() / vo.getRechargeCount();
//                vo.setAvgRecharge(avgRecharge);
//                list.add(vo);
//            }
//        }
//        list.sort(Comparator.comparingInt(RechargeRankBean::getTotalRecharge).reversed());
//
//        if(list!=null && list.size()>0){
//            for(int i = 0;i<list.size();i++){
//                list.get(i).setRank(i+1);
//            }
//        }

        return list;
    }


}
