package com.backend.struct.log.entity.fashion;

import com.backend.annotation.log.FieldDesc;
import com.backend.annotation.log.Table;
import com.backend.struct.log.CommonLogBean;
import com.backend.struct.log.IConvertor;
import com.backend.struct.log.TableType;

import java.util.Map;

/**
 * 时装激活日志
 */
@Table(name = "fashionlog", tableType = TableType.Month)
public class FashionLog extends CommonLogBean implements IConvertor {

    @FieldDesc
    private int fashionId;      //时装id

    @FieldDesc
    private int fashionLv;      //时装等级

    @FieldDesc
    private int actType;        //操作类型 0：激活

    @Override
    public Map<String, String> convert(Map<String, String> data) {
        return data;
    }

    public int getFashionId() {
        return fashionId;
    }

    public void setFashionId(int fashionId) {
        this.fashionId = fashionId;
    }

    public int getFashionLv() {
        return fashionLv;
    }

    public void setFashionLv(int fashionLv) {
        this.fashionLv = fashionLv;
    }

    public int getActType() {
        return actType;
    }

    public void setActType(int actType) {
        this.actType = actType;
    }
}
