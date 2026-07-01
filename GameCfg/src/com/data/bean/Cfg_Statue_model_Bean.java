/**
 * Auto generated, do not edit it
 *
 * statue_model配置表
 */
package com.data.bean;

	
public class Cfg_Statue_model_Bean{
    /**
     * Statue编号
     */
    private final int id;
    /**
     * Statue编号
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 类型（1为首席雕像、2为公会雕像、3为圣天城雕像）
     */
    private final int type;
    /**
     * 类型（1为首席雕像、2为公会雕像、3为圣天城雕像）
     * @return
     */
    public final int getType(){
        return type;
    }
    /**
     * 默认显示NPC
     */
    private final int npcid;
    /**
     * 默认显示NPC
     * @return
     */
    public final int getNpcid(){
        return npcid;
    }
    /**
     * 模型缩放（百分比）
     */
    private final int size_scale;
    /**
     * 模型缩放（百分比）
     * @return
     */
    public final int getSize_scale(){
        return size_scale;
    }
    /**
     * 坐标x
     */
    private final int x;
    /**
     * 坐标x
     * @return
     */
    public final int getX(){
        return x;
    }
    /**
     * 坐标y
     */
    private final int y;
    /**
     * 坐标y
     * @return
     */
    public final int getY(){
        return y;
    }
    /**
     * 地图ID
     */
    private final int mapid;
    /**
     * 地图ID
     * @return
     */
    public final int getMapid(){
        return mapid;
    }
    /**
     * 方向
     */
    private final int dirX;
    /**
     * 方向
     * @return
     */
    public final int getDirX(){
        return dirX;
    }
    /**
     * 方向
     */
    private final int dirY;
    /**
     * 方向
     * @return
     */
    public final int getDirY(){
        return dirY;
    }
    /**
     * 执笔之灵
     */
    private final int model_1;
    /**
     * 执笔之灵
     * @return
     */
    public final int getModel_1(){
        return model_1;
    }
    /**
     * 龙魂圣拳
     */
    private final int model_2;
    /**
     * 龙魂圣拳
     * @return
     */
    public final int getModel_2(){
        return model_2;
    }
    /**
     * 圣临战锤
     */
    private final int model_3;
    /**
     * 圣临战锤
     * @return
     */
    public final int getModel_3(){
        return model_3;
    }
    /**
     * 疾风剑客
     */
    private final int model_4;
    /**
     * 疾风剑客
     * @return
     */
    public final int getModel_4(){
        return model_4;
    }
    /**
     * 卡牌大师
     */
    private final int model_5;
    /**
     * 卡牌大师
     * @return
     */
    public final int getModel_5(){
        return model_5;
    }
    /**
     * 皇家枪手
     */
    private final int model_6;
    /**
     * 皇家枪手
     * @return
     */
    public final int getModel_6(){
        return model_6;
    }

    public Cfg_Statue_model_Bean(int id,int type,int npcid,int size_scale,int x,int y,int mapid,int dirX,int dirY,int model_1,int model_2,int model_3,int model_4,int model_5,int model_6){
        this.id = id;
        this.type = type;
        this.npcid = npcid;
        this.size_scale = size_scale;
        this.x = x;
        this.y = y;
        this.mapid = mapid;
        this.dirX = dirX;
        this.dirY = dirY;
        this.model_1 = model_1;
        this.model_2 = model_2;
        this.model_3 = model_3;
        this.model_4 = model_4;
        this.model_5 = model_5;
        this.model_6 = model_6;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("type:").append(type).append(";");
        str.append("npcid:").append(npcid).append(";");
        str.append("size_scale:").append(size_scale).append(";");
        str.append("x:").append(x).append(";");
        str.append("y:").append(y).append(";");
        str.append("mapid:").append(mapid).append(";");
        str.append("dirX:").append(dirX).append(";");
        str.append("dirY:").append(dirY).append(";");
        str.append("model_1:").append(model_1).append(";");
        str.append("model_2:").append(model_2).append(";");
        str.append("model_3:").append(model_3).append(";");
        str.append("model_4:").append(model_4).append(";");
        str.append("model_5:").append(model_5).append(";");
        str.append("model_6:").append(model_6).append(";");
        return str.toString();
    }
}
