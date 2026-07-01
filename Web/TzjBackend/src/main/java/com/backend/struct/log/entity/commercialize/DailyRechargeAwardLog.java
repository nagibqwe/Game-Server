package com.backend.struct.log.entity.commercialize;

import com.backend.annotation.log.FieldDesc;
import com.backend.annotation.log.Table;
import com.backend.struct.log.CommonLogBean;
import com.backend.struct.log.IConvertor;
import com.backend.struct.log.TableType;

import java.util.Map;

/**
 * 每日累充日志
 */
@Table(name = "dailyrechargeawardlog", tableType = TableType.Month)
public class DailyRechargeAwardLog extends CommonLogBean implements IConvertor {

    @FieldDesc
    private int awardId; //奖励id

    @FieldDesc
    private String award; //奖励

    @Override
    public Map<String, String> convert(Map<String, String> data) {
        return data;
    }

    public int getAwardId() {
        return awardId;
    }

    public void setAwardId(int awardId) {
        this.awardId = awardId;
    }

    public String getAward() {
        return award;
    }

    public void setAward(String award) {
        this.award = award;
    }
}
