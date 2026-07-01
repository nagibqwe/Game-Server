package com.gm.project.gamelog.backgmcmdlog.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.gm.framework.aspectj.lang.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gm.framework.web.domain.BaseEntity;


/**
 * 后台指令日志对象 log_backgmcmdlog
 * 
 * @author gm
 * @date 2021-09-10
 */
public class Backgmcmdlog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /**  */
    private Long id;

    /** 时间 */
    @Excel(name = "时间")
    private Long time;

    /** backUser */
    @Excel(name = "backUser")
    private String backUser;

    /** cmd */
    @Excel(name = "cmd")
    private String cmd;

    /** result */
    @Excel(name = "result")
    private String result;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setTime(Long time)
    {
        this.time = time;
    }

    public Long getTime()
    {
        return time;
    }
    public void setBackUser(String backUser)
    {
        this.backUser = backUser;
    }

    public String getBackUser()
    {
        return backUser;
    }
    public void setCmd(String cmd)
    {
        this.cmd = cmd;
    }

    public String getCmd()
    {
        return cmd;
    }
    public void setResult(String result)
    {
        this.result = result;
    }

    public String getResult()
    {
        return result;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("time", getTime())
            .append("backUser", getBackUser())
            .append("cmd", getCmd())
            .append("result", getResult())
            .toString();
    }
}
