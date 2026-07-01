package com.backend.struct.log.entity.serverstore;

import com.backend.annotation.log.FieldDesc;
import com.backend.annotation.log.Table;
import com.backend.struct.log.IConvertor;
import com.backend.struct.log.TableType;

import java.util.Map;

/**
 * 服务器仓库清理日志
 */
@Table(name = "serverstoreclearlog", tableType = TableType.Month)
public class ServerStoreClearLog implements IConvertor {

    @FieldDesc
    private String clearItem;   //清理掉的装备

    @FieldDesc
    private int size;           //当前仓库剩余容量

    @FieldDesc(desc = "logentity.commonlogbean.time")
    private long time;

    @Override
    public Map<String, String> convert(Map<String, String> data) {
        return data;
    }

    public String getClearItem() {
        return clearItem;
    }

    public void setClearItem(String clearItem) {
        this.clearItem = clearItem;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
