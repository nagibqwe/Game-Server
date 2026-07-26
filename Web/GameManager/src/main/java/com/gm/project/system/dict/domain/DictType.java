package com.gm.project.system.dict.domain;

import javax.validation.constraints.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gm.framework.aspectj.lang.annotation.Excel;
import com.gm.framework.aspectj.lang.annotation.Excel.ColumnType;
import com.gm.framework.web.domain.BaseEntity;

/**
 * Тип справочника表 sys_dict_type
 * 
 * @author ruoyi
 */
public class DictType extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 字典主键 */
    @Excel(name = "字典主键", cellType = ColumnType.NUMERIC)
    private Long dictId;

    /** 字典Название */
    @Excel(name = "字典Название")
    private String dictName;

    /** Тип справочника */
    @Excel(name = "Тип справочника")
    private String dictType;

    /** Статус（0Норма 1Отключено） */
    @Excel(name = "Статус", readConverterExp = "0=Норма,1=Отключено")
    private String status;

    public Long getDictId()
    {
        return dictId;
    }

    public void setDictId(Long dictId)
    {
        this.dictId = dictId;
    }

    @NotBlank(message = "字典Название不能为空")
    @Size(min = 0, max = 100, message = "Тип справочникаНазвание长度不能超过100个字符")
    public String getDictName()
    {
        return dictName;
    }

    public void setDictName(String dictName)
    {
        this.dictName = dictName;
    }

    @NotBlank(message = "Тип справочника不能为空")
    @Size(min = 0, max = 100, message = "Тип справочникаТип长度不能超过100个字符")
    public String getDictType()
    {
        return dictType;
    }

    public void setDictType(String dictType)
    {
        this.dictType = dictType;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("dictId", getDictId())
            .append("dictName", getDictName())
            .append("dictType", getDictType())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
