package com.backend.struct.log.entity.backpack;

import com.backend.annotation.log.FieldDesc;
import com.backend.annotation.log.Table;
import com.backend.struct.log.CommonLogBean;
import com.backend.struct.log.IConvertor;
import com.backend.struct.log.TableType;

import java.util.Map;

/**
 * 掉落经验变化日志
 */
@Table(name = "dropexpchangelog", tableType = TableType.Month)
public class DropExpChangeLog extends CommonLogBean implements IConvertor {

    @FieldDesc
    private int lastTime;       //上一个时间点

    @FieldDesc
    private long changeNum;     //变化值

    @FieldDesc
    private int mapId;          //地图ID值

    @Override
    public Map<String, String> convert(Map<String, String> data) {
        return data;
    }

    public int getLastTime() {
        return lastTime;
    }

    public void setLastTime(int lastTime) {
        this.lastTime = lastTime;
    }

    public long getChangeNum() {
        return changeNum;
    }

    public void setChangeNum(long changeNum) {
        this.changeNum = changeNum;
    }

    public int getMapId() {
        return mapId;
    }

    public void setMapId(int mapId) {
        this.mapId = mapId;
    }
}
