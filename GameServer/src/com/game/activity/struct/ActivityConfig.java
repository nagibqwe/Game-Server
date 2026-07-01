package com.game.activity.struct;

import com.game.db.bean.ActivityConfigBean;
import game.core.util.TimeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 活动基础配置数据
 */
public class ActivityConfig {

    public static final Logger LOGGER = LogManager.getLogger(ActivityConfig.class);
    /**
     * ID
     */
    private int id;
    /**
     * 类型
     */
    private int type;
    /**
     * 最小开放等级
     */
    private int minLv;
    /**
     * 最大开放等级
     */
    private int maxLv;
    /**
     * 标签(用于区分展示在哪个活动标签下)
     */
    private byte tag;
    /**
     * 活动排序
     */
    private byte sort;
    /**
     * 名称
     */
    private String name;
    /**
     * 开始时间
     */
    private long beginTime;
    /**
     * 结束时间
     */
    private long endTime;
    /**
     * 是否删除(1：是，0：否)
     */
    private byte isDelete;
    /**
     * 自定义数据
     */
    private String custom;
    /**
     * 自定义配置参数  服务器使用数据的KEY：server, 客户端使用数据的KEY：client
     */
    private ConcurrentHashMap<String, Object> customCfgMap = new ConcurrentHashMap<>();

    //开始记录抓取时间
    private long startRecordTime;

    //结束记录抓取时间
    private long endRecordTime;

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

    public ConcurrentHashMap<String, Object> getCustomCfgMap() {
        return customCfgMap;
    }

    public void setCustomCfgMap(ConcurrentHashMap<String, Object> customCfgMap) {
        this.customCfgMap = customCfgMap;
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

    /**
     * 活动是否正在进行中
     * @return boolean
     */
    public boolean isActiviting() {
        long nowTime = TimeUtils.Time();
        return isActiviting(nowTime);
    }

    private boolean isActiviting(long nowTime) {
        if (isDelete == 1) {
            //活动已删除
            return false;
        }
        if (nowTime < beginTime) {
            return false;
        }

        return nowTime < endTime;
    }

    /**
     * 是否在记录时间范围
     */
    public boolean isRecordTime(){
        long nowTime = TimeUtils.Time();
        if (nowTime >= this.startRecordTime && nowTime <= this.endRecordTime){
            return true;
        }
        return false;
    }

    public void beanToActivityBaseConfig(ActivityConfigBean bean) {
        this.id = bean.getId();
        this.type = bean.getType();
        this.minLv = bean.getMinLv();
        this.maxLv = bean.getMaxLv();
        this.tag = bean.getTag();
        this.sort = bean.getSort();
        this.name = bean.getName();
        this.beginTime = bean.getBeginTime();
        this.endTime = bean.getEndTime();
        this.custom = bean.getCustom();
        this.startRecordTime = bean.getStartRecordTime();
        this.endRecordTime = bean.getEndRecordTime();
    }
}
