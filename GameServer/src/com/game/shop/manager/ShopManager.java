package com.game.shop.manager;

import com.data.CfgManager;
import com.data.bean.Cfg_Shop_Maket_Bean;
import com.game.db.bean.ShopBean;
import com.game.db.dao.ShopDao;
import com.game.manager.Manager;
import com.game.script.structs.ScriptEnum;
import com.game.shop.script.*;
import game.core.script.IScript;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 商店，商城购买系统

 */
public class ShopManager {
    private static final Logger log = LogManager.getLogger(ShopManager.class);

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {

        INSTANCE;
        ShopManager processor;

        Singleton() {
            this.processor = new ShopManager();
        }

        ShopManager getProcessor() {
            return processor;
        }
    }

    public static ShopManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    private ShopDao dao = new ShopDao();

    // 商店类型、标签、商品列表(包含商城与商店所有标签)
    private final ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, ArrayList<Integer>>> shopLabelSellIDList = new ConcurrentHashMap<>();

    public ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, ArrayList<Integer>>> getShopLabelSellIDList() {
        return shopLabelSellIDList;
    }

    // 商品列表
    private final ConcurrentHashMap<Integer, ShopBean> shopItems = new ConcurrentHashMap<>();

    public ConcurrentHashMap<Integer, ShopBean> getShopItems() {
        return shopItems;
    }

    /**
     * 通过商品ID获得商品配置
     * @param sellId
     * @return
     */
    public ShopBean getShopBean(int sellId) {
        return getShopItems().get(sellId);
    }

    /**
     * 通过商店类型与标签，返回商品列表
     * @param shopID
     * @param labelID
     * @return
     */
    public ArrayList<Integer> getSellIDList(int shopID, int labelID) {
        ConcurrentHashMap<Integer, ArrayList<Integer>> map = shopLabelSellIDList.getOrDefault(shopID, null);
        if (map == null)
            return null;

        return map.getOrDefault(labelID, null);
    }

    /**
     * 加载商城数据
     */
    public void load() {
        // 先加载商城数据
        List<ShopBean> list = dao.selectAll();
        if (list.size() == 0) {
            initMall();
            list = dao.selectAll();
        }

        shopItems.clear();
        shopLabelSellIDList.clear();

        for (ShopBean shopBean : list) {
            shopBean.setMall(true);
            insertCache(shopBean);
        }

        // 加载商店数据
        init();
        log.info("加载商城数据成功");
    }

    /**
     * 重置商城数据
     */
    public void reset() {
        dao.deleteAll();
        load();
        log.info("重置商城数据成功");
    }

    /**
     * 从缓存中删除
     * @param sellId
     */
    public void deleteCache(int sellId) {
        ShopBean oldBean = getShopBean(sellId);
        if (oldBean == null)
            return;

        shopItems.remove(sellId);

        ConcurrentHashMap<Integer, ArrayList<Integer>> shopMap = shopLabelSellIDList.get(oldBean.getShopid());
        if (shopMap != null) {
            ArrayList<Integer> sellIdList = shopMap.get(oldBean.getLabelid());
            if (sellIdList != null)
                sellIdList.remove(oldBean.getId());
        }
    }

    /**
     * 插入缓存
     * @param bean
     */
    public void insertCache(ShopBean bean) {
        shopItems.put(bean.getId(), bean);

        ConcurrentHashMap<Integer, ArrayList<Integer>> shopMap = shopLabelSellIDList.get(bean.getShopid());
        if (shopMap == null) {
            shopMap = new ConcurrentHashMap<>();
            shopLabelSellIDList.put(bean.getShopid(), shopMap);
        }

        ArrayList<Integer> sellIdList = shopMap.computeIfAbsent(bean.getLabelid(), k -> new ArrayList<>());
        if (!sellIdList.contains(bean.getId()))
            sellIdList.add(bean.getId());
    }

    public ShopDao getDao() {
        return dao;
    }

    private void initMall() {
        for (Cfg_Shop_Maket_Bean bean : CfgManager.getCfg_Shop_Maket_Container().getValuees()) {
            if (isMall(bean.getType())) {
                dao.insert(convert(bean));
            }
        }
        log.info("重置商城数据成功！");
    }

    private void init() {
        for (Cfg_Shop_Maket_Bean bean : CfgManager.getCfg_Shop_Maket_Container().getValuees()) {
            if (isMall(bean.getType())) continue;
            if (shopItems.containsKey(bean.getID())) continue;

            // 设置为商店数据
            ShopBean shopBean = convert(bean);
            shopBean.setMall(false);
            insertCache(shopBean);
        }
        log.info("重置商店数据成功！");
    }

    public IShopScript deal() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.ShopScript);
        if (is instanceof IShopScript) {
            return (IShopScript) is;
        } else {
            return null;
        }
    }

    public ILimitShopScript limitShop() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.LimitShopScript);
        if (is instanceof ILimitShopScript) {
            return (ILimitShopScript) is;
        } else {
            return null;
        }
    }
    public IMysteryShop mysteryShop() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.MysteryShopScript);
        if (is instanceof IMysteryShop) {
            return (IMysteryShop) is;
        } else {
            return null;
        }
    }

    public IFreeShop freeShop() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.FreeShopScript);
        if (is instanceof IFreeShop) {
            return (IFreeShop) is;
        } else {
            return null;
        }
    }

    public INewFreeShop newFreeShop(){
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.NewFreeShopScript);
        if (is instanceof INewFreeShop) {
            return (INewFreeShop) is;
        } else {
            return null;
        }

    }


    /**
     * 配置转换ShopBean
     * @param bean
     * @return
     */
    private ShopBean convert(Cfg_Shop_Maket_Bean bean) {
        ShopBean shopBean = new ShopBean();
        shopBean.setId(bean.getID());
        shopBean.setItemid(bean.getItemID());
        shopBean.setShopid(bean.getShopID());
        shopBean.setLabelid(bean.getLabelID());
        shopBean.setLevel(bean.getLevel());
        shopBean.setMilitarylevel(bean.getMilitaryLevel());
        shopBean.setGuildlevel(bean.getGuildLevel());
        shopBean.setGuildshoplvlstart(bean.getGuildShopLvlStart());
        shopBean.setGuildshoplvlend(bean.getGuildShopLvlEND());
        shopBean.setWorldlvlstart(bean.getWorldLvlStart());
        shopBean.setWorldlvlend(bean.getWorldLvlEND());
        shopBean.setIsdiscount(bean.getIsDiscount());
        shopBean.setViplevel(bean.getVipLevel());
        shopBean.setOccupation(bean.getOccupation());
        shopBean.setLimittype(bean.getLimitType());
        shopBean.setBuynum(bean.getBuyNum());
        shopBean.setCurrencyid(bean.getCurrencyID());
        shopBean.setPrice(bean.getPrice());
        shopBean.setDiscountprice(bean.getDiscountPrice());
        shopBean.setDiscount(bean.getDiscount());
        shopBean.setPromotion(bean.getPromotion());
        shopBean.setSort(bean.getSort());
        shopBean.setUptime(bean.getUpTime());
        shopBean.setDowntime(bean.getDownTime());
        shopBean.setOverdue(bean.getOverdue());
        shopBean.setDuration(bean.getDuration());
        shopBean.setBind(bean.getBind());
        shopBean.setRefreshcurrency(bean.getRefreshCurrency());
        shopBean.setRefreshnum(bean.getRefreshNum());
        shopBean.setShoptype(bean.getShopType());
        shopBean.setCountdiscount(bean.getCountDiscount());
        shopBean.setOpenday(bean.getOpenDay());
        shopBean.setCloseday(bean.getCloseDay());
        return shopBean;
    }

    /**
     * 返回是否商城的数据
     * @param type
     * @return
     */
    private boolean isMall(int type) {
        return type == 1;
    }
}
