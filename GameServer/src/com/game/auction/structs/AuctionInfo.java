package com.game.auction.structs;

import com.game.backpack.structs.Item;
import com.game.db.bean.AuctionBean;
import game.core.util.JsonUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * @Description
 * @auther lw
 * @create 2019-10-08 11:47
 */
public class AuctionInfo {
    //竞拍ID
    private long id;

    //竞拍道具
    private Item item;

    //竞拍类型
    private long guildId;

    //上架者 0为系统
    private long ownId;

    //上架时间
    private long time;

    //当前竞拍价格
    private int price;

    //当前竞拍着
    private long roleId;

    private transient Set<Long> roleIds = new HashSet<>();

    //密码
    private String password;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public long getGuildId() {
        return guildId;
    }

    public void setGuildId(long guildId) {
        this.guildId = guildId;
    }

    public long getOwnId() {
        return ownId;
    }

    public void setOwnId(long ownId) {
        this.ownId = ownId;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public Set<Long> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(Set<Long> roleIds) {
        this.roleIds = roleIds;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public AuctionBean toAuctionBean() {
        AuctionBean bean = new AuctionBean();
        bean.setAuctionId(id);
        bean.setAuctionGuild(guildId);
        bean.setAuctionOwnId(ownId);
        bean.setAuctionPrice(price);
        bean.setAuctionRoleId(roleId);
        bean.setAuctionTime(time);
        bean.setAuctionItem(JsonUtils.toJSONString(item));
        bean.setPassword(password);
        bean.setWhere(id);
        return bean;
    }

}
