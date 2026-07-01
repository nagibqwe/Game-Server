package com.backend.bean;

import org.nutz.dao.entity.annotation.*;

@Table("t_model")
public class Model {
    @Id
    @Column
    @Comment("ID")
    private int id;

    @Column
    @Comment("职业")
    @ColDefine(type=ColType.VARCHAR, width=500)
    private String career;

    @Column
    @Comment("模型ID")
    @ColDefine(type=ColType.VARCHAR, width=500)
    private String modelId;

    @Column
    @Comment("模型大小倍数")
    @ColDefine(type=ColType.VARCHAR, width=500)
    private String scale;

    @Column
    @Comment("对应的旋转参数x")
    @ColDefine(type=ColType.VARCHAR, width=500)
    private String rotX;

    @Column
    @Comment("对应的旋转参数y")
    @ColDefine(type=ColType.VARCHAR, width=500)
    private String rotY;

    @Column
    @Comment("对应的旋转参数z")
    @ColDefine(type=ColType.VARCHAR, width=500)
    private String rotZ;

    @Column
    @Comment("对应的位置参数x")
    @ColDefine(type=ColType.VARCHAR, width=500)
    private String posX;

    @Column
    @Comment("对应的位置参数y")
    @ColDefine(type=ColType.VARCHAR, width=500)
    private String posY;

    @Column
    @Comment("模型库的备注说明")
    private String tips;

    @Column
    @Comment("发送给服务器的模型库数据")
    @ColDefine(type=ColType.VARCHAR, width=1024)
    private String modelData;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCareer() {
        return career;
    }

    public void setCareer(String career) {
        this.career = career;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getScale() {
        return scale;
    }

    public void setScale(String scale) {
        this.scale = scale;
    }

    public String getRotX() {
        return rotX;
    }

    public void setRotX(String rotX) {
        this.rotX = rotX;
    }

    public String getRotY() {
        return rotY;
    }

    public void setRotY(String rotY) {
        this.rotY = rotY;
    }

    public String getRotZ() {
        return rotZ;
    }

    public void setRotZ(String rotZ) {
        this.rotZ = rotZ;
    }

    public String getPosX() {
        return posX;
    }

    public void setPosX(String posX) {
        this.posX = posX;
    }

    public String getPosY() {
        return posY;
    }

    public void setPosY(String posY) {
        this.posY = posY;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public String getModelData() {
        return modelData;
    }

    public void setModelData(String modelData) {
        this.modelData = modelData;
    }
}
