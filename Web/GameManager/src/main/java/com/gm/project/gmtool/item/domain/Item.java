package com.gm.project.gmtool.item.domain;

import com.gm.framework.aspectj.lang.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gm.framework.web.domain.BaseEntity;


/**
 * 道具装备对象 t_item
 * 
 * @author gm
 * @date 2021-08-31
 */
public class Item extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 物品Id */
    @Excel(name = "物品Id")
    private Integer itemId;

    /** 物品名 */
    @Excel(name = "物品名")
    private String itemName;

    /** 物品类型 */
    @Excel(name = "物品类型")
    private Integer itemType;

    /** 物品颜色 */
    @Excel(name = "物品颜色")
    private Integer color;

    public void setItemId(Integer itemId)
    {
        this.itemId = itemId;
    }

    public Integer getItemId()
    {
        return itemId;
    }
    public void setItemName(String itemName)
    {
        this.itemName = itemName;
    }

    public String getItemName()
    {
        return itemName;
    }
    public void setItemType(Integer itemType)
    {
        this.itemType = itemType;
    }

    public Integer getItemType()
    {
        return itemType;
    }
    public void setColor(Integer color)
    {
        this.color = color;
    }

    public Integer getColor()
    {
        return color;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("itemId", getItemId())
            .append("itemName", getItemName())
            .append("itemType", getItemType())
            .append("color", getColor())
            .toString();
    }
}
