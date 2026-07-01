package com.backend.struct.log.entity.title;

import com.backend.annotation.log.FieldDesc;
import com.backend.annotation.log.Table;
import com.backend.struct.log.CommonLogBean;
import com.backend.struct.log.IConvertor;
import com.backend.struct.log.TableType;

import java.util.Map;

/**
 * 称号日志
 */
@Table(name = "titlechangelog", tableType = TableType.Month)
public class TitleChangeLog extends CommonLogBean implements IConvertor {

    @FieldDesc
    private long roleId;            //角色id

    @FieldDesc
    private int titleId;            //称号id

    @FieldDesc
    private int operateType;        //0激活称号，1延长称号过期时间

    @FieldDesc
    private String item;            //消耗的物品

    @FieldDesc
    private int lastExpirationTime; //操作前称号过期时间，激活称号是-1

    @FieldDesc
    private int nowExpirationTime;  //称号过期时间

    @Override
    public Map<String, String> convert(Map<String, String> data) {
        return data;
    }

    @Override
    public long getRoleId() {
        return roleId;
    }

    @Override
    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public int getTitleId() {
        return titleId;
    }

    public void setTitleId(int titleId) {
        this.titleId = titleId;
    }

    public int getOperateType() {
        return operateType;
    }

    public void setOperateType(int operateType) {
        this.operateType = operateType;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public int getLastExpirationTime() {
        return lastExpirationTime;
    }

    public void setLastExpirationTime(int lastExpirationTime) {
        this.lastExpirationTime = lastExpirationTime;
    }

    public int getNowExpirationTime() {
        return nowExpirationTime;
    }

    public void setNowExpirationTime(int nowExpirationTime) {
        this.nowExpirationTime = nowExpirationTime;
    }
}
