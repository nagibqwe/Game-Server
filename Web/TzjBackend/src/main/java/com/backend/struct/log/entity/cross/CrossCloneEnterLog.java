package com.backend.struct.log.entity.cross;

import com.backend.annotation.log.FieldDesc;
import com.backend.annotation.log.Table;
import com.backend.struct.log.CommonLogBean;
import com.backend.struct.log.IConvertor;
import com.backend.struct.log.TableType;

import java.util.Map;

/**
 * 跨服进入日志
 */
@Table(name = "crosscloneenterlog", tableType = TableType.Month, crossLogType = 1)
public class CrossCloneEnterLog extends CommonLogBean implements IConvertor {

    @FieldDesc
    private long fightId;           //房间ID

    @FieldDesc
    private String platSid;         //服务器ID

    @FieldDesc
    private int campNo;             //阵营

    @FieldDesc
    private int cloneId;            //副本ID

    @FieldDesc
    private String cloneName;       //副本名字

    @FieldDesc
    private int level;              //玩家等级

    @FieldDesc(desc = "logentity.commonlogbean.time")
    private long time;

    @Override
    public Map<String, String> convert(Map<String, String> data) {
        return data;
    }

    public long getFightId() {
        return fightId;
    }

    public void setFightId(long fightId) {
        this.fightId = fightId;
    }

    public String getPlatSid() {
        return platSid;
    }

    public void setPlatSid(String platSid) {
        this.platSid = platSid;
    }

    public int getCampNo() {
        return campNo;
    }

    public void setCampNo(int campNo) {
        this.campNo = campNo;
    }

    public int getCloneId() {
        return cloneId;
    }

    public void setCloneId(int cloneId) {
        this.cloneId = cloneId;
    }

    public String getCloneName() {
        return cloneName;
    }

    public void setCloneName(String cloneName) {
        this.cloneName = cloneName;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public long getTime() {
        return time;
    }

    @Override
    public void setTime(long time) {
        this.time = time;
    }
}
