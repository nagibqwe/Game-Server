package com.gm.project.gamelog.bossdierelivelog.domain;

import com.gm.framework.aspectj.lang.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gm.framework.web.domain.BaseEntity;


/**
 * 首领死亡复活日志对象 log_bossdierelivelog
 * 
 * @author gm
 * @date 2021-09-10
 */
public class Bossdierelivelog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /**  */
    private Long id;

    /** 时间 */
    @Excel(name = "时间")
    private Long time;

    /** 类型 */
    @Excel(name = "类型")
    private Long type;

    /** bossId */
    @Excel(name = "bossId")
    private Long bossId;

    /** 地图ID */
    @Excel(name = "地图ID")
    private Long mapId;

    /** 击杀者 */
    @Excel(name = "击杀者")
    private Long param;

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
    public void setType(Long type)
    {
        this.type = type;
    }

    public Long getType()
    {
        return type;
    }
    public void setBossId(Long bossId)
    {
        this.bossId = bossId;
    }

    public Long getBossId()
    {
        return bossId;
    }
    public void setMapId(Long mapId)
    {
        this.mapId = mapId;
    }

    public Long getMapId()
    {
        return mapId;
    }
    public void setParam(Long param)
    {
        this.param = param;
    }

    public Long getParam()
    {
        return param;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("time", getTime())
            .append("type", getType())
            .append("bossId", getBossId())
            .append("mapId", getMapId())
            .append("param", getParam())
            .toString();
    }
}
