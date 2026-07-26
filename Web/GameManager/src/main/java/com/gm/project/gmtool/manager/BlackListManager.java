package com.gm.project.gmtool.manager;

import com.gm.common.utils.spring.SpringUtils;
import com.gm.project.gmtool.blackuser.domain.Blackuser;
import com.gm.project.gmtool.blackuser.service.IBlackuserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BlackListManager {

    private static Logger log = LoggerFactory.getLogger(BlackListManager.class);

    @Autowired
    private IBlackuserService blackuserService;

    public static BlackListManager getInstance() {
        return   (BlackListManager) SpringUtils.getBean("blackListManager");
    }

    private List<Map<String, Object>> dataList = new ArrayList<>();

    public List<Map<String, Object>> getBlackList() {
        return this.dataList;
    }

    public void setDataList(List<Map<String, Object>> dataList) {
        this.dataList = dataList;
    }

    @PostConstruct
    public void init() {
        //启动时从Данные库加载一次Чёрный список配置Информация
        loadData();
    }

    public void loadData() {
        dataList.clear();
        List<Blackuser> blackusers = blackuserService.selectBlackuserList(new Blackuser());
        for (Blackuser blackuser:blackusers){
            Map<String, Object> map = new HashMap<>();
            map.put("userId", blackuser.getUserId());
            map.put("platform", blackuser.getPlatform());
            dataList.add(map);
        }
        log.info("Чёрный списокИнформация加载完毕,共" + dataList.size() + "条记录");
    }

    /**
     * 通过Платформа得到Чёрный список
     */
    public List<Map<String, Object>> getBlackList(String platformName) {
        List<Map<String, Object>> blackList = new ArrayList<>();
        for (Map<String, Object> map : dataList) {
            String platform = map.get("platform").toString();
            if (platformName.equals(platform.trim())) {
                blackList.add(map);
            }
        }
        return blackList;
    }

    /**
     * 通过Платформа得到Чёрный список
     */
    public List<Object> getBlackListUsers(String platformName) {
        List<Object> blackList = new ArrayList<>();
        for (Map<String, Object> map : this.dataList) {
            if (map.get("platform").toString().trim().equalsIgnoreCase(platformName)) {
                blackList.add(map.get("userId"));
            }
        }
        return blackList;
    }
}
