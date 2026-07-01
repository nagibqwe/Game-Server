package com.backend.struct.log.entity.boss;

import com.backend.annotation.log.FieldDesc;
import com.backend.annotation.log.Table;
import com.backend.struct.log.IConvertor;
import com.backend.struct.log.TableType;

import java.util.Map;

@Table(name = "bossdierelivelog", tableType = TableType.Month)
public class BossDieReliveLog implements IConvertor {

    @FieldDesc(selectKey = true)
    private long bossId;        //bossID

    @FieldDesc(selectKey = true)
    private long mapId;         //地图ID

    @FieldDesc(selectKey = true)
    private int type;           //0死亡 1复活

    @FieldDesc(selectKey = true)
    private long param;         //参数，死亡为击杀者ID

    @FieldDesc(desc = "logentity.commonlogbean.time")
    private long time;              //时间

    public long getBossId() {
        return bossId;
    }

    public void setBossId(long bossId) {
        this.bossId = bossId;
    }

    public long getMapId() {
        return mapId;
    }

    public void setMapId(long mapId) {
        this.mapId = mapId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getParam() {
        return param;
    }

    public void setParam(long param) {
        this.param = param;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public Map<String, String> convert(Map<String, String> data) {
        return data;
    }
}
