package com.backend.manager;

import org.apache.log4j.Logger;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlackListManager {

    private final static Logger log = Logger.getLogger(BlackListManager.class);

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {

        INSTANCE;
        BlackListManager manager;

        Singleton() {
            this.manager = new BlackListManager();
        }

        BlackListManager getProcessor() {
            return manager;
        }
    }

    public static BlackListManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    private Dao dao;

    private List<Map<String, Object>> dataList = new ArrayList<>();

    public List<Map<String, Object>> getBlackList() {
        return this.dataList;
    }

    public void setDataList(List<Map<String, Object>> dataList) {
        this.dataList = dataList;
    }

    public void init(Dao dao) {
        this.dao = dao;
        loadData();
    }

    public void loadData() {
        dataList.clear();
        Sql sql = Sqls.create("SELECT userNumber,platform FROM t_blackuser");
        sql.setCallback((Connection conn, ResultSet rs, Sql sql1) -> {
            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                map.put("userId", rs.getLong(1));
                map.put("platform", rs.getString(2));
                dataList.add(map);
            }
            return dataList;
        });
        dao.execute(sql);
        log.info("黑名单信息加载完毕,共" + dataList.size() + "条记录");
    }

    /**
     * 通过平台名得到黑名单
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
     * 通过平台名得到黑名单
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
