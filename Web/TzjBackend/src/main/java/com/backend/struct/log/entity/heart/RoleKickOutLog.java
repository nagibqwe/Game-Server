package com.backend.struct.log.entity.heart;

import com.backend.annotation.log.FieldDesc;
import com.backend.annotation.log.Table;
import com.backend.struct.log.CommonLogBean;
import com.backend.struct.log.IConvertor;
import com.backend.struct.log.TableType;

import java.util.Map;

@Table(name = "rolekickoutlog", tableType = TableType.Month)
public class RoleKickOutLog extends CommonLogBean implements IConvertor {

    @FieldDesc
    private int reason;             //玩家踢下线原因，0心跳过快，1没有心跳

    @FieldDesc
    private String context;         //玩家发生时的文字记录进去，方便分析

    @Override
    public Map<String, String> convert(Map<String, String> data) {
        return data;
    }

    public int getReason() {
        return reason;
    }

    public void setReason(int reason) {
        this.reason = reason;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }
}
