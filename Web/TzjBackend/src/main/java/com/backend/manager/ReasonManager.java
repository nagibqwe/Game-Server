package com.backend.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import com.backend.bean.ChangeReason;
import org.apache.log4j.Logger;

import com.backend.utils.PropertiesUtil;
import org.nutz.dao.Dao;
import org.nutz.dao.impl.NutDao;

public class ReasonManager {

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {

        INSTANCE;
        ReasonManager manager;

        Singleton() {
            this.manager = new ReasonManager();
        }

        ReasonManager getProcessor() {
            return manager;
        }
    }

    public static ReasonManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    private Dao dao;

    public Map<Integer, String> reasonMap = new HashMap<>();

    public void init(Dao dao) {
        this.dao = dao;
        loadReasons();
    }

    public void loadReasons() {
        reasonMap.clear();
        List<ChangeReason> reasonList = dao.query(ChangeReason.class, null);
        for (ChangeReason reason : reasonList) {
            reasonMap.put(reason.getId(), reason.getName());
        }
    }

    public Map<Integer, String> getReasonMap() {
        return reasonMap;
    }

    public void setReasonMap(Map<Integer, String> reasonMap) {
        this.reasonMap = reasonMap;
    }
}
