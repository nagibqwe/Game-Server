package com.kits.project.photocheck.photoglobal.domain;

import com.kits.framework.aspectj.lang.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.kits.framework.web.domain.BaseEntity;


/**
 * 图片全局配置对象 t_photoglobal
 * 
 * @author gm
 * @date 2021-07-23
 */
public class TPhotoglobal extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** key值 */
    private String keyStr;

    /** value值 */
    @Excel(name = "value值")
    private String valueStr;

    public void setKeyStr(String keyStr)
    {
        this.keyStr = keyStr;
    }

    public String getKeyStr()
    {
        return keyStr;
    }
    public void setValueStr(String valueStr)
    {
        this.valueStr = valueStr;
    }

    public String getValueStr()
    {
        return valueStr;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("keyStr", getKeyStr())
            .append("valueStr", getValueStr())
            .toString();
    }
}
