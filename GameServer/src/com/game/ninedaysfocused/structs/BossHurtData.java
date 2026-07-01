package com.game.ninedaysfocused.structs;

/**
 * Created by CLC on 2019/7/29.
 */
public class BossHurtData {

    public long uid = 0;//唯一ID

    private int modelId = 0;// 模板ID

    private long hurtA = 0;//正营A伤害

    private long hurtB = 0;//正营B伤害

    private long maxH = 0;//总血量


    public void setUid(long uid){this.uid =uid;}

    public long getUid(){return  uid;}

    public void setModelId(int modelId){this.modelId = modelId;}

    public int getModelId(){return modelId;}

    public void setHurtA(long hurtA){this.hurtA = hurtA;}

    public long getHurtA(){return hurtA;}

    public void  setHurtB(long hurtB){this.hurtB = hurtB;}

    public long getHurtB(){return hurtB;}

    public void  setMaxH(long maxH){this.maxH = maxH;}

    public long getMaxH(){return maxH;}

}
