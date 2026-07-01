/**
 * Auto generated, do not edit it
 *
 * fashion_total配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
import com.data.struct.ReadIntegerArray; 
	
public class Cfg_Fashion_total_Bean{
    /**
     * 时装ID(type*100000000+（0，时装；1，化形养成）*10000000+化形ID(衣服武器用优先级排序））
     */
    private final int id;
    /**
     * 时装ID(type*100000000+（0，时装；1，化形养成）*10000000+化形ID(衣服武器用优先级排序））
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 类型（1，衣服；2武器；3，背饰；4坐骑；5，宠物；6法宝；7魂甲；11头像；12头像框；13气泡）
     */
    private final int type;
    /**
     * 类型（1，衣服；2武器；3，背饰；4坐骑；5，宠物；6法宝；7魂甲；11头像；12头像框；13气泡）
     * @return
     */
    public final int getType(){
        return type;
    }
    /**
     * 细分类型（0，时装；1，化形养成）
     */
    private final int type_2;
    /**
     * 细分类型（0，时装；1，化形养成）
     * @return
     */
    public final int getType_2(){
        return type_2;
    }
    /**
     * 优先级排序（数字越小优先级越高）
     */
    private final int order;
    /**
     * 优先级排序（数字越小优先级越高）
     * @return
     */
    public final int getOrder(){
        return order;
    }
    /**
     * 资源
     */
    private final ReadIntegerArrayEs res;
    /**
     * 资源
     * @return
     */
    public final ReadIntegerArrayEs getRes(){
        return res;
    }
    /**
     * 条件（填写FunctionVariable表配置）
     */
    private final ReadIntegerArray Variable_ID;
    /**
     * 条件（填写FunctionVariable表配置）
     * @return
     */
    public final ReadIntegerArray getVariable_ID(){
        return Variable_ID;
    }
    /**
     * 是否有新获得时装的展示
     */
    private final int isShow;
    /**
     * 是否有新获得时装的展示
     * @return
     */
    public final int getIsShow(){
        return isShow;
    }
    /**
     * 是否需要【新】标记（0，需要 ；1，不需要）
     */
    private final int if_New;
    /**
     * 是否需要【新】标记（0，需要 ；1，不需要）
     * @return
     */
    public final int getIf_New(){
        return if_New;
    }
    /**
     * 是否需要在上传头像界面里隐藏，1是隐藏
     */
    private final int if_headup;
    /**
     * 是否需要在上传头像界面里隐藏，1是隐藏
     * @return
     */
    public final int getIf_headup(){
        return if_headup;
    }

    public Cfg_Fashion_total_Bean(int id,int type,int type_2,int order,String resStr,String Variable_IDStr,int isShow,int if_New,int if_headup){
        this.id = id;
        this.type = type;
        this.type_2 = type_2;
        this.order = order;
        this.res = new ReadIntegerArrayEs(resStr,"}",",");
        this.Variable_ID = new ReadIntegerArray(Variable_IDStr,",");
        this.isShow = isShow;
        this.if_New = if_New;
        this.if_headup = if_headup;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("type:").append(type).append(";");
        str.append("type_2:").append(type_2).append(";");
        str.append("order:").append(order).append(";");
        str.append("res:").append(res).append(";");
        str.append("Variable_ID:").append(Variable_ID).append(";");
        str.append("isShow:").append(isShow).append(";");
        str.append("if_New:").append(if_New).append(";");
        str.append("if_headup:").append(if_headup).append(";");
        return str.toString();
    }
}
