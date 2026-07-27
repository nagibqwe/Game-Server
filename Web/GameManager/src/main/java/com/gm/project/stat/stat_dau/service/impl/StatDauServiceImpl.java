package com.gm.project.stat.stat_dau.service.impl;

import com.game.util.ListToStringUtil;
import com.gm.common.utils.DateUtils;
import com.gm.project.gmtool.manager.BlackListManager;
import com.gm.project.stat.stat_dau.dao.IStatDauDao;
import com.gm.project.stat.stat_dau.domain.StatDauBean;
import com.gm.project.stat.stat_dau.service.IStatDauService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
@Service
public class StatDauServiceImpl implements IStatDauService {

    /**
     * 数据库相关操作
     */
    @Autowired
    public IStatDauDao statDauDao;

    public List<StatDauBean> statDau(String selectGroupName, String selectServerIds, String channelNames, String startDate, String endDate, Boolean isBlack, int level) {
        //黑名单排除
        String blackUsers = "";
        if (isBlack!=null && isBlack) {
            List<Object> blackList = BlackListManager.getInstance().getBlackListUsers(selectGroupName);
            if (blackList.isEmpty()) {
                isBlack = false;
            } else {
                blackUsers = ListToStringUtil.listToString(blackList);
            }
        }
        List<StatDauBean> dataList = new ArrayList<>();
        //获取时间列表
        List<String> dayList = DateUtils.getDateList(startDate,endDate);
        for(int i = 0;i<dayList.size();i++){
            List<Map<String, Object>> dayDAUDataList = statDauDao.getDayDAUDataList("stat_login",channelNames,selectServerIds,blackUsers,dayList.get(i), dayList.get(i),level);
            StatDauBean statDauBean = new StatDauBean();
            statDauBean.setDay(dayList.get(i));
            if(dayDAUDataList!=null && dayDAUDataList.size()>0){
                statDauBean.setDauNum(dayDAUDataList.size());
            }else {
                statDauBean.setDauNum(0);
            }
            dataList.add(statDauBean);
        }
        return dataList;
    }
}
