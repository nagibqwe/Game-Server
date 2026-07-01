package com.backend.struct.log.entity.godbook;

import com.backend.annotation.log.FieldDesc;
import com.backend.annotation.log.Table;
import com.backend.struct.log.CommonLogBean;
import com.backend.struct.log.IConvertor;
import com.backend.struct.log.TableType;

import java.util.Map;

/**
 * 符咒激活日志
 */
@Table(name = "amuletactivelog", tableType = TableType.Month)
public class AmuletActiveLog extends CommonLogBean implements IConvertor {

    @FieldDesc
    private int amuletId;               //符咒id

    @Override
    public Map<String, String> convert(Map<String, String> data) {
        return data;
    }

    public int getAmuletId() {
        return amuletId;
    }

    public void setAmuletId(int amuletId) {
        this.amuletId = amuletId;
    }
}
