package com.gm.project.gmtool.activityModel.domain;

import com.gm.framework.aspectj.lang.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gm.framework.web.domain.BaseEntity;


/**
 * 运营活动模型库对象 t_activity_model
 * 
 * @author gm
 * @date 2021-09-14
 */
public class ActivityModel extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Integer id;

    /** 职业 */
    @Excel(name = "职业")
    private String career;

    /** 模型ID */
    @Excel(name = "模型ID")
    private String modelId;

    /** 模型大小倍数 */
    @Excel(name = "模型大小倍数")
    private String scale;

    /** 对应的旋转参数x */
    @Excel(name = "对应的旋转参数x")
    private String rotX;

    /** 对应的旋转参数y */
    @Excel(name = "对应的旋转参数y")
    private String rotY;

    /** 对应的旋转参数z */
    @Excel(name = "对应的旋转参数z")
    private String rotZ;

    /** 对应的位置参数x */
    @Excel(name = "对应的位置参数x")
    private String posX;

    /** 对应的位置参数y */
    @Excel(name = "对应的位置参数y")
    private String posY;

    /** 模型库的备注说明 */
    @Excel(name = "模型库的备注说明")
    private String tips;

    /** 发送给服务器的模型库数据 */
    @Excel(name = "发送给服务器的模型库数据")
    private String modelData;

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getId()
    {
        return id;
    }
    public void setCareer(String career)
    {
        this.career = career;
    }

    public String getCareer()
    {
        return career;
    }
    public void setModelId(String modelId)
    {
        this.modelId = modelId;
    }

    public String getModelId()
    {
        return modelId;
    }
    public void setScale(String scale)
    {
        this.scale = scale;
    }

    public String getScale()
    {
        return scale;
    }
    public void setRotX(String rotX)
    {
        this.rotX = rotX;
    }

    public String getRotX()
    {
        return rotX;
    }
    public void setRotY(String rotY)
    {
        this.rotY = rotY;
    }

    public String getRotY()
    {
        return rotY;
    }
    public void setRotZ(String rotZ)
    {
        this.rotZ = rotZ;
    }

    public String getRotZ()
    {
        return rotZ;
    }
    public void setPosX(String posX)
    {
        this.posX = posX;
    }

    public String getPosX()
    {
        return posX;
    }
    public void setPosY(String posY)
    {
        this.posY = posY;
    }

    public String getPosY()
    {
        return posY;
    }
    public void setTips(String tips)
    {
        this.tips = tips;
    }

    public String getTips()
    {
        return tips;
    }
    public void setModelData(String modelData)
    {
        this.modelData = modelData;
    }

    public String getModelData()
    {
        return modelData;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("career", getCareer())
            .append("modelId", getModelId())
            .append("scale", getScale())
            .append("rotX", getRotX())
            .append("rotY", getRotY())
            .append("rotZ", getRotZ())
            .append("posX", getPosX())
            .append("posY", getPosY())
            .append("tips", getTips())
            .append("modelData", getModelData())
            .toString();
    }
}
