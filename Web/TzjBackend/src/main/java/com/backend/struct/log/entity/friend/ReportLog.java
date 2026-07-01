package com.backend.struct.log.entity.friend;

import com.backend.annotation.log.FieldDesc;
import com.backend.annotation.log.Table;
import com.backend.struct.log.CommonLogBean;
import com.backend.struct.log.IConvertor;
import com.backend.struct.log.TableType;

import java.util.Map;

/**
 * 举报日志
 */
@Table(name = "reportlog", tableType = TableType.Year)
public class ReportLog extends CommonLogBean implements IConvertor {

    @FieldDesc
    private long otherId;       //被举报玩家ID

    @FieldDesc
    private int type;           //举报类型

    @FieldDesc
    private String content;     //内容

    @Override
    public Map<String, String> convert(Map<String, String> data) {
        return data;
    }

    public long getOtherId() {
        return otherId;
    }

    public void setOtherId(long otherId) {
        this.otherId = otherId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
