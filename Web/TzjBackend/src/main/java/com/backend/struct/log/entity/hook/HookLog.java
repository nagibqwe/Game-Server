package com.backend.struct.log.entity.hook;

import com.backend.annotation.log.FieldDesc;
import com.backend.annotation.log.Table;
import com.backend.struct.log.CommonLogBean;
import com.backend.struct.log.IConvertor;
import com.backend.struct.log.TableType;

import java.util.Map;

/**
 * 离线挂机日志
 */
@Table(name = "hooklog", tableType = TableType.Month)
public class HookLog extends CommonLogBean implements IConvertor {

    @FieldDesc
    private long lastOfflineTime;       //上次离线时间

    @FieldDesc
    private long onlineTime;            //上线时间

    @FieldDesc
    private int lastLv;                 //离线时等级

    @FieldDesc
    private int currentLv;              //当前等级

    @FieldDesc
    private long addExp;                //增加经验值

    @FieldDesc
    private String items;               //获得物品

    @Override
    public Map<String, String> convert(Map<String, String> data) {
        return data;
    }

    public long getLastOfflineTime() {
        return lastOfflineTime;
    }

    public void setLastOfflineTime(long lastOfflineTime) {
        this.lastOfflineTime = lastOfflineTime;
    }

    public long getOnlineTime() {
        return onlineTime;
    }

    public void setOnlineTime(long onlineTime) {
        this.onlineTime = onlineTime;
    }

    public int getLastLv() {
        return lastLv;
    }

    public void setLastLv(int lastLv) {
        this.lastLv = lastLv;
    }

    public int getCurrentLv() {
        return currentLv;
    }

    public void setCurrentLv(int currentLv) {
        this.currentLv = currentLv;
    }

    public long getAddExp() {
        return addExp;
    }

    public void setAddExp(long addExp) {
        this.addExp = addExp;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }
}