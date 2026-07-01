/**
 * Auto generated, do not edit it
 *
 * Rank_base配置表
 */
package com.data.bean;

	
public class Cfg_Rank_base_Bean{
    /**
     * ID
     */
    private final int id;
    /**
     * ID
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 排行榜名称
     */
    private final String name;
    /**
     * 排行榜名称
     * @return
     */
    public final String getName(){
        return name;
    }
    /**
     * 大类型分类
     */
    private final int type;
    /**
     * 大类型分类
     * @return
     */
    public final int getType(){
        return type;
    }
    /**
     * 排序优先级
     */
    private final int sort;
    /**
     * 排序优先级
     * @return
     */
    public final int getSort(){
        return sort;
    }
    /**
     * 大类型的名字
     */
    private final String type_name;
    /**
     * 大类型的名字
     * @return
     */
    public final String getType_name(){
        return type_name;
    }
    /**
     * 服务器枚举值
     */
    private final int serverEnum;
    /**
     * 服务器枚举值
     * @return
     */
    public final int getServerEnum(){
        return serverEnum;
    }
    /**
     * UI上数值的名称
     */
    private final String uiValueDes;
    /**
     * UI上数值的名称
     * @return
     */
    public final String getUiValueDes(){
        return uiValueDes;
    }
    /**
     * 显示模型类型(1.玩家,2.坐骑,3.翅膀,4.宠物,5.器灵,6精灵球,7魂甲)
     */
    private final int modelType;
    /**
     * 显示模型类型(1.玩家,2.坐骑,3.翅膀,4.宠物,5.器灵,6精灵球,7魂甲)
     * @return
     */
    public final int getModelType(){
        return modelType;
    }
    /**
     * 排行类型枚举
     */
    private final String typeCode;
    /**
     * 排行类型枚举
     * @return
     */
    public final String getTypeCode(){
        return typeCode;
    }
    /**
     * 排行榜数据清理时间（0表示不清理，1表示周1，2表示周2，4表示周3，8表示周4，16表示周5，32表示周6，64表示周日）
     */
    private final int cleanWeek;
    /**
     * 排行榜数据清理时间（0表示不清理，1表示周1，2表示周2，4表示周3，8表示周4，16表示周5，32表示周6，64表示周日）
     * @return
     */
    public final int getCleanWeek(){
        return cleanWeek;
    }
    /**
     * 进入排行榜的最大人数，用于决定最大的人数(排行榜功能显示用）
     */
    private final int rank_num;
    /**
     * 进入排行榜的最大人数，用于决定最大的人数(排行榜功能显示用）
     * @return
     */
    public final int getRank_num(){
        return rank_num;
    }
    /**
     * 服务器实际统计人数（用于服务器统计的数据），主要是其他功能在使用
     */
    private final int rank_truthNum;
    /**
     * 服务器实际统计人数（用于服务器统计的数据），主要是其他功能在使用
     * @return
     */
    public final int getRank_truthNum(){
        return rank_truthNum;
    }
    /**
     * 是否在排行榜内显示
1显示。0不显示
     */
    private final int isShow;
    /**
     * 是否在排行榜内显示
1显示。0不显示
     * @return
     */
    public final int getIsShow(){
        return isShow;
    }
    /**
     * 0不显示模型
1显示人物模型
2宠物模型
3坐骑模型
4法宝模型
5魂甲模型
     */
    private final int isShoweModel;
    /**
     * 0不显示模型
1显示人物模型
2宠物模型
3坐骑模型
4法宝模型
5魂甲模型
     * @return
     */
    public final int getIsShoweModel(){
        return isShoweModel;
    }
    /**
     * 0不显示装备
1人物装备
2圣装
3仙甲
4宠物装备
5灵体装备

     */
    private final int isShoweEqup;
    /**
     * 0不显示装备
1人物装备
2圣装
3仙甲
4宠物装备
5灵体装备

     * @return
     */
    public final int getIsShoweEqup(){
        return isShoweEqup;
    }

    public Cfg_Rank_base_Bean(int id,String name,int type,int sort,String type_name,int serverEnum,String uiValueDes,int modelType,String typeCode,int cleanWeek,int rank_num,int rank_truthNum,int isShow,int isShoweModel,int isShoweEqup){
        this.id = id;
        this.name = name;
        this.type = type;
        this.sort = sort;
        this.type_name = type_name;
        this.serverEnum = serverEnum;
        this.uiValueDes = uiValueDes;
        this.modelType = modelType;
        this.typeCode = typeCode;
        this.cleanWeek = cleanWeek;
        this.rank_num = rank_num;
        this.rank_truthNum = rank_truthNum;
        this.isShow = isShow;
        this.isShoweModel = isShoweModel;
        this.isShoweEqup = isShoweEqup;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("name:").append(name).append(";");
        str.append("type:").append(type).append(";");
        str.append("sort:").append(sort).append(";");
        str.append("type_name:").append(type_name).append(";");
        str.append("serverEnum:").append(serverEnum).append(";");
        str.append("uiValueDes:").append(uiValueDes).append(";");
        str.append("modelType:").append(modelType).append(";");
        str.append("typeCode:").append(typeCode).append(";");
        str.append("cleanWeek:").append(cleanWeek).append(";");
        str.append("rank_num:").append(rank_num).append(";");
        str.append("rank_truthNum:").append(rank_truthNum).append(";");
        str.append("isShow:").append(isShow).append(";");
        str.append("isShoweModel:").append(isShoweModel).append(";");
        str.append("isShoweEqup:").append(isShoweEqup).append(";");
        return str.toString();
    }
}
