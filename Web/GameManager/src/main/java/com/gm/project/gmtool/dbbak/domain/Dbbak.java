package com.gm.project.gmtool.dbbak.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.gm.framework.aspectj.lang.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gm.framework.web.domain.BaseEntity;


/**
 * Резервные копии БД对象 t_dbbak
 * 
 * @author gm
 * @date 2021-09-13
 */
public class Dbbak extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** ID сервера */
    @Excel(name = "ID сервера")
    private Integer serverId;

    /** Тип1游戏库 2Журнал库 */
    @Excel(name = "Тип1游戏库 2Журнал库")
    private Integer type;

    /** Путь к резервной копии */
    @Excel(name = "Путь к резервной копии")
    private String url;

    /** Размер файла */
    @Excel(name = "Размер файла")
    private Long size;

    /**备份中 1Да 0Нет*/
    private Integer baking;

    /**1文件存在 0不存在*/
    private Integer fileExist;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setServerId(Integer serverId)
    {
        this.serverId = serverId;
    }

    public Integer getServerId()
    {
        return serverId;
    }
    public void setType(Integer type)
    {
        this.type = type;
    }

    public Integer getType()
    {
        return type;
    }
    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getUrl()
    {
        return url;
    }
    public void setSize(Long size)
    {
        this.size = size;
    }

    public Long getSize()
    {
        return size;
    }

    public Integer getBaking() {
        return baking;
    }

    public void setBaking(Integer baking) {
        this.baking = baking;
    }

    public Integer getFileExist() {
        return fileExist;
    }

    public void setFileExist(Integer fileExist) {
        this.fileExist = fileExist;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("serverId", getServerId())
            .append("type", getType())
            .append("createTime", getCreateTime())
            .append("url", getUrl())
            .append("size", getSize())
            .toString();
    }
}
