package com.backend.manager;

import com.backend.bean.Item;
import com.backend.utils.PropertiesUtil;
import org.apache.log4j.Logger;
import org.nutz.dao.Dao;
import org.nutz.lang.Strings;
import org.nutz.mvc.Mvcs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class ItemManager {

    private final static Logger log = Logger.getLogger(ItemManager.class);

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {

        INSTANCE;
        ItemManager manager;

        Singleton() {
            this.manager = new ItemManager();
        }

        ItemManager getProcessor() {
            return manager;
        }
    }

    public static ItemManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    private Dao dao;

    /**
     * 游戏服语言包 ：nameMap : <clone_map_id,str1000>
     */
    private Map<String, String> nameMap = new HashMap<>();

    private Map<Integer, Item> itemList = new HashMap<>();

    public void init(Dao dao) {
        this.dao = dao;
        loadData();
    }

    public void loadData() {
        itemList.clear();
        List<Item> items = dao.query(Item.class, null);
        items.forEach(n -> itemList.put(n.getItemId(), n));
    }

    public String getItemName(Integer modelId) {
        if (itemList.containsKey(modelId)) {
            return itemList.get(modelId).getItemName() + "[" + modelId + "]";
        }
        return "null[" + modelId + "]";
    }

    /**
     * 根据id获取对应名称说明
     */
    public String getStr(String id, String nameType) {
        String nameStr = nameMap.get(nameType + "_" + id);
        if (nameStr == null) {
            return "";
        }
        return id + "[" + Mvcs.getMessage(Mvcs.getReq(), nameStr) + "]";
    }

    public Map<String, String> getNameMap() {
        return nameMap;
    }

    public Map<Integer, Item> getItemList() {
        return itemList;
    }

    public String getItemName(int itemId) {
        for (Item item : itemList.values()) {
            if (item.getItemId() == itemId) {
                return item.getItemName() + "[" + itemId + "]";
            }
        }
        return "未知物品[" + itemId + "]";
    }

    public boolean checkItems(String items) {
        if (Strings.isBlank(items)) {
            return false;
        }
        String[] itemArray = items.split(";");
        for (String s : itemArray) {
            String[] item = s.split(",");
            if (item.length < 3) {
                return false;
            }
            if (!Strings.isNumber(item[0])) {
                return false;
            }
            if (!Strings.isNumber(item[1])) {
                return false;
            }
            if (!Strings.isNumber(item[2])) {
                return false;
            }
        }
        return true;
    }
}
