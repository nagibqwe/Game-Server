package com.backend.struct.log.entity.copymap;

import com.backend.annotation.log.FieldDesc;
import com.backend.annotation.log.Table;
import com.backend.struct.log.CommonLogBean;
import com.backend.struct.log.IConvertor;
import com.backend.struct.log.TableType;

import java.util.Map;

/**
 * 副本进入日志
 */
@Table(name = "copymapenterlog", tableType = TableType.Month)
public class CopyMapEnterLog extends CommonLogBean implements IConvertor {

    @FieldDesc
    private int cloneId;            //副本ID

    @FieldDesc
    private int auto;               //是否扫荡

    @FieldDesc
    private int level;              //玩家等级

    @FieldDesc
    private String cloneName;       //副本名字

    @Override
    public Map<String, String> convert(Map<String, String> data) {
        return data;
    }

    public int getCloneId() {
        return cloneId;
    }

    public void setCloneId(int cloneId) {
        this.cloneId = cloneId;
    }

    public int getAuto() {
        return auto;
    }

    public void setAuto(int auto) {
        this.auto = auto;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getCloneName() {
        return cloneName;
    }

    public void setCloneName(String cloneName) {
        this.cloneName = cloneName;
    }
}
