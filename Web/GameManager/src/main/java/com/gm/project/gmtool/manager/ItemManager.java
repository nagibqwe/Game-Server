package com.gm.project.gmtool.manager;

import com.gm.common.utils.StringUtils;
import com.gm.common.utils.spring.SpringUtils;
import com.gm.project.gmtool.item.domain.Item;
import com.gm.project.gmtool.item.service.IItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

@Service
public class ItemManager {

    private final static Logger log = LoggerFactory.getLogger(ItemManager.class);

    @Autowired
    private IItemService itemService;

    public static ItemManager getInstance() {
        return   (ItemManager) SpringUtils.getBean("itemManager");
    }


    /**
     * 游戏服语言包 ：nameMap : <clone_map_id,str1000>
     */
    private Map<String, String> nameMap = new HashMap<>();

    private Map<Integer, Item> itemList = new HashMap<>();

    @PostConstruct
    public void init() {
        //启动时从数据库加载一次道具物品信息
        loadData();
    }

    public void loadData() {
        itemList.clear();
        List<Item> items = itemService.selectItemList(new Item());
        items.forEach(n -> itemList.put(n.getItemId(), n));
    }

    public String getItemName(Integer modelId) {
        if (itemList.containsKey(modelId)) {
            return itemList.get(modelId).getItemName() + "[" + modelId + "]";
        }
        return "null[" + modelId + "]";
    }

//    /**
//     * 根据id获取对应名称说明
//     */
//    public String getStr(String id, String nameType) {
//        String nameStr = nameMap.get(nameType + "_" + id);
//        if (nameStr == null) {
//            return "";
//        }
//        return id + "[" + Mvcs.getMessage(Mvcs.getReq(), nameStr) + "]";
//    }

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
        if (StringUtils.isBlank(items)) {
            return false;
        }
        String[] itemArray = items.split(";");
        for (String s : itemArray) {
            String[] item = s.split(",");
            if (item.length < 3) {
                return false;
            }
            if (!StringUtils.isNumeric(item[0])) {
                return false;
            }
            if (!StringUtils.isNumeric(item[1])) {
                return false;
            }
            if (!StringUtils.isNumeric(item[2])) {
                return false;
            }
        }
        return true;
    }
}
