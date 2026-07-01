package com.backend.manager;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.nutz.lang.Strings;

import com.backend.utils.PropertiesUtil;

/**
 * 货币转换人民币汇率
 */
public class CurrencyRateManager {

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {

        INSTANCE;
        CurrencyRateManager manager;

        Singleton() {
            this.manager = new CurrencyRateManager();
        }

        CurrencyRateManager getProcessor() {
            return manager;
        }
    }

    public static CurrencyRateManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    private final static Logger log = Logger.getLogger(CurrencyRateManager.class);

    private static Map<String, Double> currencyMap = new HashMap<>();

    public void init() {
        load();
    }

    public void load() {
        currencyMap.clear();
        String path = "/conf/currency.properties";
        Properties reasonProp = PropertiesUtil.getInstance().readProperties(path);

        for (Entry<Object, Object> entry : reasonProp.entrySet()) {
            currencyMap.put(entry.getKey().toString(), Double.parseDouble(entry.getValue().toString()));
        }
        log.info("货币汇率信息(currency.properties)加载完成，共" + currencyMap.size() + "条数据");
    }

    public Double getValue(String currencyType) {
        if (Strings.isBlank(currencyType)) {
            log.error("没有找到货币汇率,币种:" + currencyType);
            return 1.0;
        }
        if (currencyMap.containsKey(currencyType.trim().toUpperCase())) {
            return currencyMap.get(currencyType.trim().toUpperCase());
        }
        log.error("没有配置货币汇率,币种:" + currencyType);
        return 1.0;
    }
}
