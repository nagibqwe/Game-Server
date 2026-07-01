package com.backend.struct.log.entity.fashion;

import com.backend.annotation.log.FieldDesc;
import com.backend.annotation.log.Table;
import com.backend.struct.log.CommonLogBean;
import com.backend.struct.log.IConvertor;
import com.backend.struct.log.TableType;

import java.util.Map;

/**
 * 时装星辰日志
 */
@Table(name = "fashionstarlog", tableType = TableType.Month)
public class FashionStarLog extends CommonLogBean implements IConvertor {

    @FieldDesc
    private int currStar;               //当前星数

    @FieldDesc
    private int beforeStar;             //之前的星数

    @FieldDesc
    private String upItems;             //消耗材料

    @FieldDesc
    private long actionId;              //关联ID

    @Override
    public Map<String, String> convert(Map<String, String> data) {
        return data;
    }

    public int getCurrStar() {
        return currStar;
    }

    public void setCurrStar(int currStar) {
        this.currStar = currStar;
    }

    public int getBeforeStar() {
        return beforeStar;
    }

    public void setBeforeStar(int beforeStar) {
        this.beforeStar = beforeStar;
    }

    public String getUpItems() {
        return upItems;
    }

    public void setUpItems(String upItems) {
        this.upItems = upItems;
    }

    public long getActionId() {
        return actionId;
    }

    public void setActionId(long actionId) {
        this.actionId = actionId;
    }
}
