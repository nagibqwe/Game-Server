package com.game.shop.structs;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @explain: desc
 * @time Created on 2020/2/5 14:49.
 * @author: cxl
 */
public class LimitShop {
	/**
	 * 商品ID，结束时间ms
	 */
	private ConcurrentHashMap<Integer, Long> shops = new ConcurrentHashMap<>();

    /**
     * 离线阶段过期商品的时间
     */
	private ConcurrentHashMap<Integer, Long> expiredShops = new ConcurrentHashMap<>();

	/**
	 * 已经买过的商品
	 */
	private List<Integer> buys = new ArrayList<>();
    /**
     * 购买仙甲寻宝条件商品的时间
     */
	private long buySpecialShopTime;

	public static LimitShop newLimitShop() {
		LimitShop shop = new LimitShop();
		shop.setBuys(new ArrayList<>());
		shop.setShops(new ConcurrentHashMap<>());
		return shop;
	}

	public ConcurrentHashMap<Integer, Long> getShops() {
		return this.shops;
	}

	public void setShops(ConcurrentHashMap<Integer, Long> shops) {
		this.shops = shops;
	}

	public List<Integer> getBuys() {
		return this.buys;
	}

    public ConcurrentHashMap<Integer, Long> getExpiredShops() {
        return expiredShops;
    }

    public void setExpiredShops(ConcurrentHashMap<Integer, Long> expiredShops) {
        this.expiredShops = expiredShops;
    }

    public void setBuys(List<Integer> buys) {
		this.buys = buys;
	}

    public long getBuySpecialShopTime() {
        return buySpecialShopTime;
    }

    public void setBuySpecialShopTime(long buySpecialShopTime) {
        this.buySpecialShopTime = buySpecialShopTime;
    }
}
