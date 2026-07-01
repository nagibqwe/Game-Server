package com.gm.project.gmtool.hefu.domain;

import com.gm.framework.aspectj.lang.annotation.Excel;
import com.gm.project.gmtool.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gm.framework.web.domain.BaseEntity;

import java.util.ArrayList;
import java.util.List;


/**
 * 合服对象 sys_hefu
 * 
 * @author gm
 * @date 2021-09-08
 */
public class Hefu extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 源服务器 */
    @Excel(name = "源服务器")
    private String fromServer;

    /** 目标服务器 */
    @Excel(name = "目标服务器")
    private Integer toServer;

    /** 合服状态0新建1合服中2成功3失败 */
    @Excel(name = "合服状态0新建1合服中2成功3失败4取消")
    private Integer status;

    /** cn:国内，tw:台湾，kor:韩国，yn:越南，thai:泰国，ros:新马,en:英语 */
    @Excel(name = "cn:国内，tw:台湾，kor:韩国，yn:越南，thai:泰国，ros:新马,en:英语")
    private String language;

    /** 当前进度 */
    @Excel(name = "当前进度")
    private Integer step;

    /**记录信息*/
    private String record;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setFromServer(String fromServer)
    {
        this.fromServer = fromServer;
    }

    public String getFromServer()
    {
        return fromServer;
    }
    public void setToServer(Integer toServer)
    {
        this.toServer = toServer;
    }

    public Integer getToServer()
    {
        return toServer;
    }
    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public Integer getStatus()
    {
        return status;
    }
    public void setLanguage(String language)
    {
        this.language = language;
    }

    public String getLanguage()
    {
        return language;
    }
    public void setStep(Integer step)
    {
        this.step = step;
    }

    public Integer getStep()
    {
        return step;
    }

    public String getRecord() {
        return record;
    }

    public List<String> getRecordList() {
        if(record != null){
            return JsonUtils.parseArray(record, String.class);
        }
        return null;
    }

    public void setRecord(String record) {
        this.record = record;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("fromServer", getFromServer())
            .append("toServer", getToServer())
            .append("status", getStatus())
            .append("language", getLanguage())
            .append("step", getStep())
            .toString();
    }

    public List<Integer> getFromServers(){
        List<Integer> arr = new ArrayList<>();
        String[] ids = fromServer.split(",|，");
        for(String id : ids){
            if(StringUtils.isNoneEmpty(id)){
                arr.add(Integer.parseInt(id));
            }
        }
        return arr;
    }

}
