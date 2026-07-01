package com.game.kaoshangling.struct;

import com.game.kaoshangling.manager.KaoShangLingManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  犒赏令 荒古令数据
 *
 */
public class KaoShangLingData {
    /**
     * 犒赏令列表
     */
    public Map<Integer,KaoShangLingBean> KaoShangLingMap = new HashMap<>();

    public Map<Integer, KaoShangLingBean> getKaoShangLingMap() {
        return KaoShangLingMap;
    }

    public void setKaoShangLingMap(Map<Integer, KaoShangLingBean> kaoShangLingMap) {
        KaoShangLingMap = kaoShangLingMap;
    }

    public KaoShangLingData(){
        if(!KaoShangLingMap.containsKey(KaoShangLingManager.EnKaoShangLingHorse)){
            KaoShangLingMap.put(KaoShangLingManager.EnKaoShangLingHorse,new KaoShangLingBean());
        }
    }
}
