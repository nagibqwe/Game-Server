package com.game.db.bean;

import game.core.db.BaseBean;

public class ActivityConfigBean extends BaseBean {

    private int id; //活动唯一ID
    private int type; //活动类型
    private int minLv; //最小开放等级
    private int maxLv; //最大开放等级
    private byte tag; //标签(用于区分展示在哪个活动标签下)
    private byte sort; //活动排序
    private String name; //活动名称
    private long beginTime; //活动开始时间
    private long endTime; //活动结束时间
    private byte isDelete; //是否删除(1：是，0：否)
    private String custom; //自定义配置活动数据
    private byte state; //活动状态：0预发布 1进行中(从后台过来时覆盖掉正在进行的活动)
    private long startRecordTime; //开始记录抓取时间
    private long endRecordTime; //结束记录抓取时间
    private byte isOpenServer;//是否是新服活动 0否 1是

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getMinLv() {
        return minLv;
    }

    public void setMinLv(int minLv) {
        this.minLv = minLv;
    }

    public int getMaxLv() {
        return maxLv;
    }

    public void setMaxLv(int maxLv) {
        this.maxLv = maxLv;
    }

    public byte getTag() {
        return tag;
    }

    public void setTag(byte tag) {
        this.tag = tag;
    }

    public byte getSort() {
        return sort;
    }

    public void setSort(byte sort) {
        this.sort = sort;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(long beginTime) {
        this.beginTime = beginTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public byte getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(byte isDelete) {
        this.isDelete = isDelete;
    }

    public String getCustom() {
        return custom;
    }

    public void setCustom(String custom) {
        this.custom = custom;
    }

    public byte getState() {
        return state;
    }

    public void setState(byte state) {
        this.state = state;
    }

    public long getStartRecordTime() {
        return startRecordTime;
    }

    public void setStartRecordTime(long startRecordTime) {
        this.startRecordTime = startRecordTime;
    }

    public long getEndRecordTime() {
        return endRecordTime;
    }

    public void setEndRecordTime(long endRecordTime) {
        this.endRecordTime = endRecordTime;
    }

    public byte getIsOpenServer() {
        return isOpenServer;
    }

    public void setIsOpenServer(byte isOpenServer) {
        this.isOpenServer = isOpenServer;
    }

    @Override
    public String toString() {
        return "ActivityConfigBean{" +
                "id=" + id +
                ", type=" + type +
                ", minLv=" + minLv +
                ", maxLv=" + maxLv +
                ", tag=" + tag +
                ", sort=" + sort +
                ", name='" + name + '\'' +
                ", beginTime=" + beginTime +
                ", endTime=" + endTime +
                ", isDelete=" + isDelete +
                ", custom='" + custom + '\'' +
                ", state=" + state +
                ", startRecordTime=" + startRecordTime +
                ", endRecordTime=" + endRecordTime +
                ", isOpenServer=" + isOpenServer +
                '}';
    }
}
