package com.backend.struct.log.entity.gm;

import com.backend.annotation.log.FieldDesc;
import com.backend.annotation.log.Table;
import com.backend.struct.log.IConvertor;
import com.backend.struct.log.TableType;

import java.util.Map;

/**
 * 后台指令日志
 */
@Table(name = "backgmcmdlog", tableType = TableType.Month)
public class BackGMCmdLog implements IConvertor {

    @FieldDesc
    private String backUser;        //后台用户

    @FieldDesc
    private String cmd;             //指令

    @FieldDesc
    private String result;          //执行结果

    @FieldDesc(desc = "logentity.commonlogbean.time")
    private long time;              //时间

    @Override
    public Map<String, String> convert(Map<String, String> data) {
        return data;
    }

    public String getBackUser() {
        return backUser;
    }

    public void setBackUser(String backUser) {
        this.backUser = backUser;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
